"""Solutions for day 23."""
from __future__ import annotations

import re
from heapq import heappop, heappush
from itertools import permutations
from queue import Queue
from typing import Generator, Iterable, Sequence


class State(list):
    """The state of the amphipods.

    The state items correspond to the following positions marked by letters:

        #############
        #ab.c.d.e.fg#
        ###h#i#j#k###
          #l#m#n#o#
          #  ...  #
          #########

    Where a=0, b=1, and so forth. The state is then a list of states of
    each position. For example, stata[3] = 4 means that position 3 (=d) is
    occupied by a D-type (=4) amphipod, and state[5] = 0 means that the
    position 5 (=f) is empty.
    """

    char_to_int = {".": 0, "A": 1, "B": 2, "C": 3, "D": 4}
    int_to_char = {0: ".", 1: "A", 2: "B", 3: "C", 4: "D"}
    energy = {0: 0, 1: 1, 2: 10, 3: 100, 4: 1000}

    def __init__(self, values: Iterable[int], adj: list[list[int]]) -> None:
        """Initialise the state.

        Args:
            values: The state of each point. The points are labeled as
              described above. For example, if values = `[0, 3, 1, ...]` then
              position 0 is emtpy, position 1 contains a C-amphipod (=3),
              position 2 contains an A-amphipod (=1) etc.
            adj: The adjacency matrix with the distances between all points.
              If the `values` parameter has length N, then `adj` should be
              an N by N matrix.
        """
        super().__init__(values)
        self.adj = adj
        # The number of amphipods of the same kind. Remove 7 hallway points
        # and divide by the 4 kinds of amphipods.
        self.n = (len(self) - 7) // 4
        # The home slots for each amphipod kind
        self.slots = {
            kind: [kind + 6 + 4 * i for i in range(self.n)] for kind in range(1, 5)
        }

    def copy(self) -> State:
        """Make a copy of the state."""
        values = super().copy()
        state = State(values, self.adj)
        return state

    def move(self, pos: int, dest: int) -> int:
        """Move an amphipod to a new position and compute the energy cost."""
        if not self[pos]:
            raise ValueError("Can't move empty space")
        self[pos], self[dest] = self[dest], self[pos]
        return self.energy[self[dest]] * self.adj[pos][dest]

    @property
    def done(self) -> bool:
        """Check if the amphipods are all in their correct positions."""
        return all(self[i : i + 4] == [1, 2, 3, 4] for i in range(7, len(self), 4))

    def energy_bound(self) -> int:
        """Compute the minimal energy required to solve the current state."""
        # The minimal solution is for all amphipods to directly go home
        # disregarding the collisions.

        # For each amphipod kind compute the minimal energy cost
        energy = 0
        for kind in range(1, 5):
            # Find the positions of the amphipods of the given kind
            pos = [idx for idx, value in enumerate(self) if value == kind]

            # Note that each amphipod has many possible destinations, so we'll
            # have to consider all permutations.
            dist = min(
                sum(self.adj[x][y] for x, y in zip(pos, slots))
                for slots in permutations(self.slots[kind])
            )
            energy += dist * self.energy[kind]

        return energy


def iter_moves(state: State, pos: int) -> Generator[int, None, None]:
    """Iterate over all possible moves from a given position."""
    if not (kind := state[pos]):
        return

    def go_home(target: int) -> Generator[int, None, None]:
        """Try and go into one of the home slots.

        Args:
            target: The target top slot position of a home side room of
              the current amphipod. The value of `target` can be
              arbitrary, but if it's not the correct slot for the current
            amphipod kind, then this function is a no-op.

        Yields:
            Either one position in the home room if target points to the
            correct room and if a move there is possible, otherwise nothing
            is yielded.
        """
        # Target slot is not our home, don't go there
        if target != state.slots[kind][0]:
            return

        # For any other slot we can only go there if it's filled with the
        # correct amphipods up to there already.
        # Start with the lowest slot.
        for slot in reversed(state.slots[kind]):
            # Go to the first emtpy slot
            if not state[slot]:
                yield slot
                return
            # An amphipod of a different kind is in our home. Don't go there.
            if state[slot] != kind:
                return

    # It only makes sense to either move from a room to one of the points
    # in the hallway, from hallway to hallway, or from hallway to the farthest
    # point in a room. Moves within a room don't make sense as they don't
    # make any progress.

    if pos > 6:  # MOVE FROM SLOT TO HALLWAY OR ANOTHER SLOT
        # Amphipod already in final position, so it can't move
        if pos in state.slots[kind] and all(
            state[slot] == kind for slot in state.slots[kind] if slot > pos
        ):
            return

        # Amphipod in the bottom slot can only move if not blocked
        # by another amphipod in the top slot.
        if pos > 10 and state[pos - 4]:
            return

        # Start above the room and try to go left
        # First move to the upper slot...
        dest = pos
        while dest > 10:
            dest -= 4
        # The go out in the hallway up and left
        dest -= 6
        # dest = pos - 10 if pos > 10 else pos - 6
        while dest >= 0 and not state[dest]:
            yield dest  # hallway
            yield from go_home(dest + 5)  # room
            dest -= 1

        # Start above the room and try to go right
        # First move to the upper slot...
        dest = pos
        while dest > 10:
            dest -= 4
        # The go out in the hallway up and right
        dest -= 5
        while dest < 7 and not state[dest]:
            yield dest
            yield from go_home(dest + 6)
            dest += 1
    else:  # MOVE FROM HALLWAY TO SLOT
        # Going left
        dest = pos
        while dest >= 0 and (not state[dest] or dest == pos):
            yield from go_home(dest + 5)
            dest -= 1
        # Going right
        dest = pos
        while dest < 7 and (not state[dest] or dest == pos):
            yield from go_home(dest + 6)
            dest += 1


def parse_state(lines: Sequence[str]) -> State:
    """Parse the state from the puzzle input lines."""
    x = [0] * 7  # The unoccupied points in the hallway
    pattern = re.compile(r"[# ]*#(\w)#(\w)#(\w)#(\w)#[# ]*")
    for line in lines:
        if match := pattern.fullmatch(line):
            x += [State.char_to_int[c] for c in match.groups()]

    adj = compute_adj(lines)

    return State(x, adj)


def compute_adj(lines: Sequence[str]) -> list[list[int]]:
    """Compute the adjacency matrix of distances in the map.

    The map is the puzzle input with the "#" characters denoting walls,
    and "." denoting emtpy spaces. Only points in the map where an
    amphipod can stop will be considered. These are exactly the same
    locations that are used in the `State` class. See its description
    for more information about the location of these points.

    Args:
        lines: The lines of the puzzle input.

    Returns:
        The adjacency matrix with distances between the locations
        in the puzzle map.
    """

    def bfs(start_row: int, start_col: int) -> dict[tuple[int, int], int]:
        """Find all distances from the point (start_row, start_col)."""
        dist = {}
        q: Queue[tuple[int, int, int]] = Queue()
        q.put((start_row, start_col, 0))
        while not q.empty():
            i, j, d = q.get()
            dist[(i, j)] = d
            for x, y in [(i + 1, j), (i, j + 1), (i - 1, j), (i, j - 1)]:
                if (x, y) in dist or lines[x][y] == "#":
                    continue
                q.put((x, y, d + 1))
        return dist

    # Translate state indices to 2d locations in the burrow. The index of
    # the rows and columns in the adj refers to the state indices.
    points = [(1, j) for j in [1, 2, 4, 6, 8, 10, 11]]  # hallway points
    depth = len(lines) - 3
    for row in range(2, 2 + depth):
        for col in [3, 5, 7, 9]:
            points.append((row, col))

    adj = []
    for row, col in points:
        distances = bfs(row, col)
        adj.append([distances[end] for end in points])

    return adj


def solve(lines: Sequence[str]) -> int:
    """Solve the puzzle.

    We apply the A* pathfinding algorithm to the sequences of amphipod
    states. The distance metric is the total energy cost to reach the
    given state. This implementation in itself is straightforward, but
    the main complexity is hidden in `iter_moves` that computes all
    possible next moves from the given state, as well as the
    `State.energy_bound` method that provides the A* distance heuristic.

    Args:
        lines: The lines of the puzzle input.

    Returns:
        The minimal energy required for all amphipods to optimally
        organise themselves.
    """
    start = parse_state(lines)
    queue = [(0, 0, start)]  # = (total_energy_estimate, energy, state)
    seen = {tuple(start)}
    while queue:
        _, prev_energy, prev_state = heappop(queue)
        for pos in range(len(prev_state)):
            for dest in iter_moves(prev_state, pos):
                state = prev_state.copy()
                energy = prev_energy + state.move(pos, dest)
                if tuple(state) in seen:
                    continue
                if state.done:
                    return energy
                seen.add(tuple(state))
                heappush(queue, (energy + state.energy_bound(), energy, state))
    return -1


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    lines = data_s.splitlines()

    part1 = solve(lines)
    part2 = solve(lines[:-2] + ["  #D#C#B#A#", "  #D#B#A#C#"] + lines[-2:])

    return part1, part2

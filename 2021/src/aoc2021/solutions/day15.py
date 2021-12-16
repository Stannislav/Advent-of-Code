"""Solutions for day 15."""
from collections.abc import Generator
from functools import cached_property
from typing import NamedTuple


class Point(NamedTuple):
    """Coordinates of a point in the cave."""

    i: int
    j: int


class Cave:
    """The cave."""

    def __init__(self, data: list[list[int]], factor: int = 1) -> None:
        """Initialise the cave.

        Args:
            data: A two-dimensional grid of risk values.
            factor: Tiling factor for both directions. For example, a factor
              of 3 means that the cave data will be tiled on a 3x3 grid. As
              per puzzle instructions the risk values for each tile increase
              by their L1 distance from the original tile. The risk values
              wrap around to stay in the interval [1, 9].
        """
        self.data = data
        self.ni = len(data)
        self.nj = len(data[0])
        self.factor = factor

    def __getitem__(self, p: Point) -> int:
        """Get the risk value at a given point."""
        val = self.data[p.i % self.ni][p.j % self.nj] + p.i // self.ni + p.j // self.nj
        return (val - 1) % 9 + 1

    @cached_property
    def end(self) -> Point:
        """Get the bottom right point of the cave."""
        return Point(self.ni * self.factor - 1, self.nj * self.factor - 1)

    @cached_property
    def size(self) -> Point:
        """Get the cave dimensions accounting for the tiling factor."""
        return Point(self.ni * self.factor, self.nj * self.factor)

    def neighbours(self, p: Point) -> Generator[Point, None, None]:
        """Iterate over all neighbours of a given point."""
        lim_i, lim_j = self.size
        for x, y in [(p.i + 1, p.j), (p.i, p.j + 1), (p.i - 1, p.j), (p.i, p.j - 1)]:
            if 0 <= x < lim_i and 0 <= y < lim_j:
                yield Point(x, y)


def dijkstra(cave: Cave) -> int:
    """Find the shortest distance through the cave.

    The path goes from the top left corner to the bottom right corner.

    This implementation is still slow since almost all points are explored.
    Because we can't travel diagonally the distance metric is L1 and therefore
    the end point is further away than any other point in the cave.
    """
    start = Point(0, 0)
    seen = set()
    q = {start}
    dist = {start: 0}

    current = start
    while current != cave.end:
        # Update neighbours' distances
        for p in cave.neighbours(current):
            if p not in seen:
                d = dist[current] + cave[p]
                if d <= dist.get(p, d):
                    dist[p] = d
                    q.add(p)

        # Choose the next one
        q.remove(current)
        seen.add(current)
        current = min(q, key=lambda p_: dist[p_])

    return dist[cave.end]


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    data = [[int(x) for x in line] for line in data_s.splitlines()]
    part1 = dijkstra(Cave(data, factor=1))
    part2 = dijkstra(Cave(data, factor=5))

    return part1, part2

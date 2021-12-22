"""Solutions for day 19."""
from __future__ import annotations

from dataclasses import dataclass
from functools import cached_property
from itertools import chain, combinations, islice
from queue import Queue
from typing import Generator, Sequence


# Subclassing NamedTuple leads to type problems in overridden dunder methods:
# __add__ needs to return a tuple (see LSP), but we return Vec.
@dataclass(frozen=True)
class Vec:
    """A three-dimensional vector."""

    x: int = 0
    y: int = 0
    z: int = 0

    def __len__(self) -> int:
        """Get the number of vector components, which is always 3."""
        return 3

    def __getitem__(self, item: int) -> int:
        """Get the value of a vector component."""
        if item == 0:
            return self.x
        if item == 1:
            return self.y
        if item == 2:
            return self.z
        raise IndexError

    def __iter__(self) -> Generator[int, None, None]:
        """Iterate over the vector components."""
        yield self.x
        yield self.y
        yield self.z

    def __repr__(self) -> str:
        """Get the string representation."""
        return f"v({self.x}, {self.y}, {self.z})"

    def __add__(self, other: Vec) -> Vec:
        """Add another vector."""
        return Vec(self.x + other.x, self.y + other.y, self.z + other.z)

    def __sub__(self, other: Vec) -> Vec:
        """Subtract another vector."""
        return Vec(self.x - other.x, self.y - other.y, self.z - other.z)


class Rot:
    """A 3D rotation in multiples of 90 degrees.

    Because each rotation is a multiple of 90 degrees it effectively
    amounts to a permutation of axes and changes of signs. For example,
    a 2D points with coordinates (x, y) rotated four times counter-clockwise
    by 90 degrees will cycle through the coordinates:

        (x, y) -> (-y, x) -> (-x, -y) -> (y, -x) -> (x, y) -> etc.

    So to do the bookkeeping we track the axes permutation and the sign
    changes.
    """

    def __init__(
        self, *, pos: Sequence[int] = (0, 1, 2), sign: Sequence[int] = (1, 1, 1)
    ) -> None:
        """Initialise the rotation. The default is no rotation."""
        self.pos = list(pos)
        self.sign = list(sign)

    def _rot(self, i: int, j: int) -> None:
        """Rotate in the i-j plane by 90 degrees."""
        self.pos[i], self.pos[j] = self.pos[j], self.pos[i]
        self.sign[i], self.sign[j] = -self.sign[j], self.sign[i]

    def x(self, v: Vec) -> Vec:
        """Rotate a vector by 90 degrees around the x-axis and update itself."""
        self._rot(1, 2)
        return Vec(v.x, -v.z, v.y)

    def y(self, v: Vec) -> Vec:
        """Rotate a vector by 90 degrees around the y-axis and update itself."""
        self._rot(2, 0)
        return Vec(v.z, v.y, -v.x)

    def z(self, v: Vec) -> Vec:
        """Rotate a vector by 90 degrees around the z-axis and update itself."""
        self._rot(0, 1)
        return Vec(-v.y, v.x, v.z)

    def __call__(self, v: Vec) -> Vec:
        """Apply the total rotation to a vector."""
        s0, s1, s2 = self.sign
        p0, p1, p2 = self.pos
        return Vec(s0 * v[p0], s1 * v[p1], s2 * v[p2])

    def __matmul__(self, other: Rot) -> Rot:
        """Compose with another rotation. The other rotation applies first."""
        return Rot(
            pos=[
                other.pos[self.pos[0]],
                other.pos[self.pos[1]],
                other.pos[self.pos[2]],
            ],
            sign=[*self(Vec(*other.sign))],
        )


class Scanner:
    """A beacon scanner.

    A scanner can see a number of beacons, and it provides their coordinates
    in its own coordinate frame. A scanner can be rotated and shifted in
    space, which also affects the effective beacon coordinates it sees.
    """

    def __init__(self, beacons: Sequence[Vec]) -> None:
        """Initialise a scanner."""
        self.beacons = beacons
        self.rot = Rot()
        self.pos = Vec()

    def __iter__(self) -> Generator[Vec, None, None]:
        """Iterate over all beacons and get their coordinates."""
        for i in range(len(self)):
            yield self[i]

    def __len__(self) -> int:
        """Get the number of beacons the scanner locates."""
        return len(self.beacons)

    def __getitem__(self, item: int) -> Vec:
        """Get the given beacon in the scanner's current coordinate frame."""
        return self.rot(self.beacons[item]) + self.pos

    @cached_property
    def adj(self) -> list[list[int]]:
        """Compute the adjacency matrix of beacon distances."""
        n = len(self.beacons)
        adj = [[0] * n for _ in range(n)]
        for i, j in combinations(range(n), 2):
            adj[i][j] = adj[j][i] = dist(self.beacons[i], self.beacons[j])

        return adj

    def move(self, rot: Rot, shift: Vec) -> None:
        """Rotate and shift the scanner."""
        self.rot = rot @ self.rot
        self.pos = rot(self.pos) + shift


def dist(p1: Vec, p2: Vec) -> int:
    """The Manhattan distance of two points/vectors."""
    return sum(abs(x - y) for x, y in zip(p1, p2))


def matching_beacons(s1: Scanner, s2: Scanner) -> list[tuple[Vec, Vec]]:
    """Find the matching beacons in two scanners.

    Since the scanners are rotated we can't compare the points directly.
    What's invariant under rotations and shifts are the distances between
    the beacons. The adjacency matrix is exactly the matrix of distances
    between beacons.

    According to the problem statement two scanners shall only be considered
    as having overlapping beacons if at least 12 of them match. So for each
    pair of rows in the two adjacency matrices we check that at least 11
    entries are equal, which means that the beacons corresponding to these
    rows both have 11 other beacons at matching distances. If we find at
    least 12 such pairs then the overlap is found.

    Args:
        s1: The first scanner.
        s2: The second scanner.

    Returns:
        A list of matching beacon pairs, each still in the coordinate
        frame of the corresponding scanner.
    """
    pairs = []
    for p1, line1 in zip(s1, s1.adj):
        dist1 = set(line1)
        for p2, line2 in zip(s2, s2.adj):
            dist2 = set(line2)
            if len(dist1 & dist2) >= 11:
                pairs.append((p1, p2))

    if len(pairs) >= 12:
        return pairs
    else:
        return []


def find_rot(v1: Vec, v2: Vec) -> Rot:
    """Find the rotation that maps the vector v2 to v1.

    Args:
        v1: The fixed vector.
        v2: The moving vector that needs to be mapped to v1.

    Returns:
        The rotation that maps v2 to v1.
    """
    rot = Rot()
    # x-rotations until one of (y, z) match
    while v1.y != v2.y and v1.z != v2.z:
        v2 = rot.x(v2)

    # rotations around the axis that matched until everything matches
    if v1.y == v2.y:
        # y found, y-rotations till x-z match
        while v1.x != v2.x:
            v2 = rot.y(v2)
    else:  # v1.z == v2.z
        # z found, z- rotations until x-y match
        while v1.x != v2.x:
            v2 = rot.z(v2)

    return rot


def align(fixed: Scanner, moving: Scanner) -> bool:
    """Align the moving scanner to the fixed one.

    Args:
        fixed: The fixed (aligned) scanner.
        moving: The moving scanner (to be aligned).

    Returns:
        Whether the scanners could be aligned. The scanners can only
        be aligned if they have overlapping beacons.
    """
    pairs = matching_beacons(fixed, moving)
    if not pairs:
        return False

    # Two pairs of matching points are enough to align.
    (p1, q1), (p2, q2) = pairs[:2]

    # First rotate the moving scanner so that the vectors p1 -> p2 match
    rot = find_rot(p1 - p2, q1 - q2)

    # Then shift so that the points match
    shift = p1 - rot(q1)

    # Update the moving scanner
    moving.move(rot, shift)

    return True


def parse_input(data_s: str) -> list[Scanner]:
    """Parse the input data."""
    scanners = []
    for block in data_s.split("\n\n"):
        beacons = []
        # Skip the first line that contains the name of the scanner
        for line in islice(block.splitlines(), 1, None):
            x, y, z = line.split(",")
            beacons.append(Vec(int(x), int(y), int(z)))
        scanners.append(Scanner(beacons))

    return scanners


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse input
    scanners = parse_input(data_s)

    # Align all scanners to scanner 0. Start with scanner 0 and find all
    # other scanners that overlap with it. Align them to scanner 0, then
    # find all other scanners that overlap with them. Repeat.
    seen = set()
    queue: Queue[Scanner] = Queue()
    queue.put(scanners[0])
    while not queue.empty():
        fixed = queue.get()
        seen.add(fixed)
        for moving in scanners:
            if moving not in seen and align(fixed, moving):
                # Now "moving" is aligned to fixed, adn therefore
                # also to scanner 0
                queue.put(moving)

    # Find unique beacons
    beacons = set(chain.from_iterable(scanners))

    part1 = len(beacons)
    part2 = max(dist(s1.pos, s2.pos) for s1, s2 in combinations(scanners, 2))

    return part1, part2

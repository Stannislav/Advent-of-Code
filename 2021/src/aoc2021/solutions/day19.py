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
    x: int = 0
    y: int = 0
    z: int = 0

    def __len__(self) -> int:
        return 3

    def __getitem__(self, item: int) -> int:
        if item == 0:
            return self.x
        if item == 1:
            return self.y
        if item == 2:
            return self.z
        raise IndexError

    # match item:
    # case 0: return self.x
    # case 1: return self.y
    # case 2: return self.z
    # case _: raise IndexError

    def __iter__(self) -> Generator[int, None, None]:
        yield self.x
        yield self.y
        yield self.z

    def __repr__(self) -> str:
        return f"v({self.x}, {self.y}, {self.z})"

    def __add__(self, other: Vec) -> Vec:
        return Vec(self.x + other.x, self.y + other.y, self.z + other.z)

    def __sub__(self, other: Vec) -> Vec:
        return Vec(self.x - other.x, self.y - other.y, self.z - other.z)


class Rot:
    def __init__(
        self, *, pos: Sequence[int] | None = None, sign: Sequence[int] | None = None
    ) -> None:
        self.pos = list(pos) if pos else [0, 1, 2]
        self.sign = list(sign) if sign else [1, 1, 1]

    def _rot(self, i: int, j: int) -> None:
        self.pos[i], self.pos[j] = self.pos[j], self.pos[i]
        self.sign[i], self.sign[j] = -self.sign[j], self.sign[i]

    def x(self, v: Vec) -> Vec:
        self._rot(1, 2)
        return Vec(v.x, -v.z, v.y)

    def y(self, v: Vec) -> Vec:
        self._rot(2, 0)
        return Vec(v.z, v.y, -v.x)

    def z(self, v: Vec) -> Vec:
        self._rot(0, 1)
        return Vec(-v.y, v.x, v.z)

    def __call__(self, v: Vec) -> Vec:
        s0, s1, s2 = self.sign
        p0, p1, p2 = self.pos
        return Vec(s0 * v[p0], s1 * v[p1], s2 * v[p2])

    def __matmul__(self, other: Rot) -> Rot:
        return Rot(
            pos=[
                other.pos[self.pos[0]],
                other.pos[self.pos[1]],
                other.pos[self.pos[2]],
            ],
            sign=[*self(Vec(*other.sign))],
        )


class Scanner:
    def __init__(self, beacons: Sequence[Vec]) -> None:
        self.beacons = beacons
        self.rot = Rot()
        self.pos = Vec()

    def __iter__(self) -> Generator[Vec, None, None]:
        for i in range(len(self)):
            yield self[i]

    def __len__(self) -> int:
        return len(self.beacons)

    def __getitem__(self, item: int) -> Vec:
        return self.rot(self.beacons[item]) + self.pos

    @cached_property
    def adj(self) -> list[list[int]]:
        n = len(self.beacons)
        adj = [[0] * n for _ in range(n)]
        for i, j in combinations(range(n), 2):
            adj[i][j] = adj[j][i] = dist(self.beacons[i], self.beacons[j])

        return adj

    def move(self, rot: Rot, shift: Vec) -> None:
        self.rot = rot @ self.rot
        self.pos = rot(self.pos) + shift


def dist(p1: Vec, p2: Vec, degree: int = 2) -> int:
    return sum(abs(x - y) ** degree for x, y in zip(p1, p2))


def overlapping_points(s1: Scanner, s2: Scanner) -> list[tuple[Vec, Vec]]:
    """Compare adj matrices."""
    pairs = []
    for p1, line1 in zip(s1, s1.adj):
        dist1 = set(line1)
        for p2, line2 in zip(s2, s2.adj):
            dist2 = set(line2)
            if len(dist1 & dist2) >= 12:
                pairs.append((p1, p2))

    return pairs


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
    pairs = overlapping_points(fixed, moving)
    if not pairs:
        return False

    # Two pairs of matching points are enough to align.
    (p1, q1), (p2, q2) = pairs[:2]

    # First rotate the moving scanner so that the vectors p1 -> p2 match
    rot = find_rot(p1 - p2, q1 - q2)

    # Then shift so that the points match
    shift = p1 - rot(q1)

    # Upate the moving scanner
    moving.move(rot, shift)

    return True


def parse_input(data_s: str) -> list[Scanner]:
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
    part2 = max(dist(s1.pos, s2.pos, degree=1) for s1, s2 in combinations(scanners, 2))

    return part1, part2

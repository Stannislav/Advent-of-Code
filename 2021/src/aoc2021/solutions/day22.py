"""Solutions for day 22."""
from __future__ import annotations

import re
from dataclasses import dataclass
from itertools import product
from typing import Generator


@dataclass
class Interval:
    """A one dimensional interval with integer bounds."""

    left: int
    right: int

    @property
    def size(self) -> int:
        """Compute the length of the interval."""
        return self.right - self.left

    @staticmethod
    def empty() -> Interval:
        """Construct an empty interval of length 0."""
        return Interval(0, 0)

    def __bool__(self) -> bool:
        """Check if the interval is of zero length."""
        return self.size > 0

    def __and__(self, other: Interval) -> Interval:
        """Compute the intersection with another interval."""
        left = max(self.left, other.left)
        right = min(self.right, other.right)

        if left < right:
            return Interval(left, right)
        else:
            return Interval.empty()

    def __truediv__(self, other: Interval) -> Generator[Interval, None, None]:
        """Split at intersection points with another interval.

        For example, (1, 8) / (3, 10) would give (1, 3), (3, 8), and
        (1, 10) / (5, 7) would give (1, 5), (5, 7), (7, 10).
        """
        points = [self.left]
        if self.left < other.left < self.right:
            points.append(other.left)
        if self.left < other.right < self.right:
            points.append(other.right)
        points.append(self.right)

        for left, right in zip(points, points[1:]):
            yield Interval(left, right)


@dataclass
class Cuboid:
    """A cuboid on a three-dimensional integer grid."""

    x: Interval
    y: Interval
    z: Interval

    @property
    def size(self) -> int:
        """Compute the volume of the cuboid."""
        return self.x.size * self.y.size * self.z.size

    def __bool__(self) -> bool:
        """Check if the cuboid is zero-volume."""
        return self.size > 0

    def __truediv__(self, other: Cuboid) -> Generator[Cuboid, None, None]:
        """Split the cuboid at intersection lines with another cuboid.

        The split is such that the other cuboid only intersects with
        one of the resulting small cuboids.

        For example, if only one corner of another cuboid happens to
        lie inside the cuboid, then it splits into 4 small cuboids.
        """
        for x, y, z in product(self.x / other.x, self.y / other.y, self.z / other.z):
            yield Cuboid(x, y, z)

    def __and__(self, other: Cuboid) -> Cuboid:
        """Compute the intersection with another cuboid."""
        return Cuboid(self.x & other.x, self.y & other.y, self.z & other.z)

    def __sub__(self, other: Cuboid) -> Generator[Cuboid, None, None]:
        """Compute sub-cuboids that don't intersect with another cuboid.

        Given two cuboids c1 and c2 this effectively computes
        (c1 / c2) - (c1 & c2). In other words, we first split the cuboid
        into smaller ones and only return those that don't lie in the
        other cuboid.
        """
        if intersection := self & other:
            for piece in self / other:
                if piece != intersection:
                    yield piece
        else:
            yield self


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse
    pattern = re.compile(
        r"(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)"
    )
    actions = []
    for line in data_s.splitlines():
        match = pattern.fullmatch(line)
        if not match:
            raise RuntimeError(f"Malformed line: {line}")
        cmd, *points = match.groups()
        x1, x2, y1, y2, z1, z2 = map(int, points)
        cuboid = Cuboid(
            Interval(x1, x2 + 1), Interval(y1, y2 + 1), Interval(z1, z2 + 1)
        )
        actions.append((cmd, cuboid))

    # Solve
    cuboids: list[Cuboid] = []
    for cmd, new_cuboid in actions:
        # Subtract the new cuboid from all existing cuboids. Upon intersection
        # cuboids split into smaller cuboid pieces.
        cuboids = [piece for cuboid in cuboids for piece in cuboid - new_cuboid]

        # Add the new cuboid
        if cmd == "on":
            cuboids.append(new_cuboid)

    interval = Interval(-50, 51)
    region = Cuboid(interval, interval, interval)
    part1 = sum((c & region).size for c in cuboids)
    part2 = sum(c.size for c in cuboids)

    return part1, part2

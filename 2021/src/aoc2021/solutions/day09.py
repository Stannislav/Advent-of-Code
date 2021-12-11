"""Solutions for day 9."""
import math
from queue import Queue
from typing import Generator

Cave = list[list[int]]
Point = tuple[int, int]


def dim(cave: Cave) -> tuple[int, int]:
    """Get the height and the width of the cave."""
    return len(cave), len(cave[0])


def neighbours(i: int, j: int, cave: Cave) -> Generator[Point, None, None]:
    """Iterate over the neighbour coordinates of a point."""
    ni, nj = dim(cave)
    for x, y in [(i + 1, j), (i, j + 1), (i - 1, j), (i, j - 1)]:
        if 0 <= x < ni and 0 <= y < nj:
            yield x, y


def find_lowest(cave: Cave) -> list[Point]:
    """Find the lowest points in the cave floor."""
    lowest = []
    ni, nj = dim(cave)
    for i in range(ni):
        for j in range(nj):
            if all(cave[x][y] > cave[i][j] for x, y in neighbours(i, j, cave)):
                lowest.append((i, j))

    return lowest


def basin_size(point: Point, cave: Cave) -> int:
    """Find the basin size around a given sink point.

    A point is part of the basin if one can reach the sink point while
    going to a lower point at every step. The highest points, those with
    the value 9, are not part of any basin.

    Find the basin points by starting at the sink point and exploring
    its neighbourhood using a BSF. Only follow points that are higher
    than the previous ones and stop once points with height 9 are reached.
    """
    q: Queue[Point] = Queue()
    seen = {point}
    q.put(point)
    while not q.empty():
        i, j = q.get()
        for x, y in neighbours(i, j, cave):
            if (x, y) not in seen and cave[i][j] < cave[x][y] < 9:
                seen.add((x, y))
                q.put((x, y))

    return len(seen)


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse input
    cave = []
    for line in data_s.splitlines():
        cave.append([int(n) for n in line])

    # Solve
    lowest = find_lowest(cave)
    basin_sizes = sorted(basin_size(point, cave) for point in lowest)

    part1 = sum(cave[i][j] + 1 for i, j in lowest)
    part2 = math.prod(basin_sizes[-3:])

    return part1, part2

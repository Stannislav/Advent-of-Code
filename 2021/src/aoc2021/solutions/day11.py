"""Solutions for day 11."""
from typing import Generator

Grid = list[list[int]]
Point = tuple[int, int]


def neighbours(i: int, j: int) -> Generator[Point, None, None]:
    """Iterate over all neighbour coordinates of a given point."""
    for x, y in [
        (i + 1, j),
        (i + 1, j + 1),
        (i, j + 1),
        (i - 1, j + 1),
        (i - 1, j),
        (i - 1, j - 1),
        (i, j - 1),
        (i + 1, j - 1),
    ]:
        if 0 <= x < 10 and 0 <= y < 10:
            yield x, y


def step(grid: Grid) -> tuple[Grid, int]:
    """Run one evolution step in the grid of flashing octopuses.

    Args:
        grid: The octopus grid.

    Returns:
        The first element is the updated grid, the second the number of
        flashes that happened in this step.

    """
    grid = [[i + 1 for i in row] for row in grid]
    flashed = [[False for _ in row] for row in grid]

    done = False
    while not done:
        done = True
        for i in range(10):
            for j in range(10):
                if not flashed[i][j] and grid[i][j] > 9:
                    flashed[i][j] = True
                    done = False
                    for x, y in neighbours(i, j):
                        grid[x][y] += 1

    for i in range(10):
        for j in range(10):
            if flashed[i][j]:
                grid[i][j] = 0

    return grid, sum(sum(row) for row in flashed)


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse input
    grid = []
    for line in data_s.splitlines():
        grid.append([int(c) for c in line])

    # Solve
    counts = []
    n = 0
    while n != 100:
        grid, n = step(grid)
        counts.append(n)

    return sum(counts[:100]), len(counts)

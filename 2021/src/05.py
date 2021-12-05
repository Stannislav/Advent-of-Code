"""Solutions for day 5."""
from collections import defaultdict
from typing import Generator


VentLine = tuple[int, int, int, int]
Pos = tuple[int, int]


def iter_vents(
    vent_line: VentLine,
    only_horizontal: bool,
) -> Generator[Pos, None, None]:
    """Iterate over all vent positions in a vent line.

    :param vent_line: The start and end points of a vent line.
    :param only_horizontal: If true then non-horizontal lines will be skipped.
    :return: The (x, y) coordinates of a vent.
    """
    x1, y1, x2, y2 = vent_line
    nx = abs(x2 - x1)
    ny = abs(y2 - y1)
    dx = 0 if nx == 0 else int((x2 - x1) / nx)
    dy = 0 if ny == 0 else int((y2 - y1) / ny)

    if only_horizontal and all([nx, ny]):
        return

    for _ in range(max(nx, ny) + 1):
        yield x1, y1
        x1 += dx
        y1 += dy


def solve(data: list[VentLine], only_horizontal: bool) -> int:
    """Solve part 1 or part 2.

    :param data: The input data.
    :param only_horizontal: Should be true for part 1 and false for part 2.
    :return: The solution for either part 1 or part 2.
    """
    vents: dict[Pos, int] = defaultdict(int)
    for vent_line in data:
        for xy in iter_vents(vent_line, only_horizontal=only_horizontal):
            vents[xy] += 1

    return sum(v > 1 for v in vents.values())


def main(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse data
    data = []
    for line in data_s.splitlines():
        start, _, end = line.partition(" -> ")
        x1, y1 = start.split(",")
        x2, y2 = end.split(",")
        data.append((int(x1), int(y1), int(x2), int(y2)))

    # Solve
    part1 = solve(data, only_horizontal=True)
    part2 = solve(data, only_horizontal=False)

    return part1, part2


if __name__ == "__main__":
    with open("input/05.txt") as fh:
        input_data = fh.read()

    part1, part2 = main(input_data)
    print(f"Part 1: {part1}")
    print(f"Part 2: {part2}")

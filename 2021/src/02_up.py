"""Community-inspired solution to day 2."""
from typing import TypeAlias

Command: TypeAlias = tuple[str, int]


def solve(commands: list[Command]) -> tuple[int, int]:
    """Perform piloting according to part 2 instructions.

    Compute the solutions for both parts in one pass by realising
    that `aim` from part 2 is equivalent to `depth` from part 1.

    :param commands: The commands from the problem input.
    :return: Part 1 solution.
    :return: Part 2 solution.
    """
    pos, depth, aim = 0, 0, 0
    for cmd, value in commands:
        match cmd:
            case "forward":
                pos += value
                depth += aim * value
            case "down":
                aim += value
            case "up":
                aim -= value

    return pos * aim, pos * depth


def main() -> None:
    """Compute the solutions."""
    commands = []
    with open("input/02.txt") as fh:
        for line in fh:
            cmd, value = line.split()
            commands.append((cmd, int(value)))

    part1, part2 = solve(commands)
    print("Part 1:", part1)
    print("Part 2:", part2)


if __name__ == "__main__":
    main()

"""Solutions for day 2."""
from typing import TypeAlias

Command: TypeAlias = tuple[str, int]


def part1(commands: list[Command]) -> int:
    """Perform piloting according to part 1 instructions."""
    pos, depth = 0, 0
    for cmd, value in commands:
        match cmd:
            case "forward":
                pos += value
            case "down":
                depth += value
            case "up":
                depth = max(0, depth - value)
    return pos * depth


def part2(commands: list[Command]) -> int:
    """Perform piloting according to part 2 instructions."""
    pos, depth, aim = 0, 0, 0
    for cmd, value in commands:
        match cmd:
            case "forward":
                pos += value
                depth = max(0, depth + aim * value)
            case "down":
                aim += value
            case "up":
                aim -= value

    return pos * depth


def run(data: str) -> tuple[int, int]:
    """Compute the solutions."""
    commands = []
    for line in data.splitlines():
        cmd, value = line.split()
        commands.append((cmd, int(value)))

    return part1(commands), part2(commands)

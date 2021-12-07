"""Solutions for day 7."""
from typing import Callable

CostFn = Callable[[int], int]


def fuel(target: int, positions: list[int], cost: CostFn) -> int:
    """Compute the fuel necessary to align crabs to a target position.

    Args:
        target: The horizontal position to align crabs to.
        positions: The starting positions of crabs.
        cost: The cost function that computes the amount of fuel for a
            given number of steps.

    Returns:
        The total fuel spent to align all crabs to given target position.
    """
    return sum(cost(abs(x - target)) for x in positions)


def cost1(x: int) -> int:
    """Compute the cost of carb movement by x steps for part 1."""
    return x


def cost2(x: int) -> int:
    """Compute the cost of carb movement by x steps for part 2."""
    return x * (x + 1) // 2


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    positions = [int(x) for x in data_s.split(",")]
    targets = range(min(positions), max(positions) + 1)
    part1 = min(fuel(target, positions, cost1) for target in targets)
    part2 = min(fuel(target, positions, cost2) for target in targets)

    return part1, part2

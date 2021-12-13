"""Different definitions and reusable utilities."""
from typing import Protocol


class ModSolution(Protocol):
    """The protocol for a module with an Advent of Code puzzle solution."""

    def run(self, data: str) -> tuple[int | str, int | str]:
        """Solve the puzzle given the input data.

        Args:
            data: The puzzle input data.

        Returns:
            A tuple with two elements - the solutions to part 1 and part2.
        """

"""Different definitions and reusable utilities."""
from typing import Protocol


class ModSolution(Protocol):
    """The protocol for a module with an AOC puzzle solution."""

    def run(self, data: str) -> tuple[int, int]:
        """Solve the puzzle given the input data.

        :param data: The puzzle input data.
        :return: Solution to part 1.
        :return: Solution to part 2.
        """

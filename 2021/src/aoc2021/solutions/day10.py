"""Solutions for day 10."""
from dataclasses import dataclass, field
from statistics import median
from typing import ClassVar, Iterable


@dataclass(frozen=True, kw_only=True)
class Result:
    """Result of the syntax check."""

    error_char: str | None = None
    completion: Iterable[str] = field(default_factory=list)
    error_scores: ClassVar[dict[str | None, int]] = {
        ")": 3,
        "]": 57,
        "}": 1197,
        ">": 25137,
    }
    completion_scores: ClassVar[dict[str, int]] = {
        ")": 1,
        "]": 2,
        "}": 3,
        ">": 4,
    }

    @property
    def ok(self) -> bool:
        """Return true if there is no error."""
        return self.error_char is None

    @property
    def error_score(self) -> int:
        """Compute the error score for part 1."""
        return self.error_scores.get(self.error_char, 0)

    @property
    def completion_score(self) -> int:
        """Compute the completion score for part 2."""
        score = 0
        for c in self.completion:
            score = score * 5 + self.completion_scores[c]

        return score


def check(line: Iterable[str]) -> Result:
    """Run the syntax check on a line of bracket text."""
    # Save the opening brackets on the stack, and pop them as they're closed.
    stack = []
    closing = {"(": ")", "[": "]", "{": "}", "<": ">"}
    for c in line:
        if c in "([{<":
            stack.append(c)
        elif len(stack) == 0:
            # closing bracket can't be the first character
            return Result(error_char=c)
        else:
            if c != closing[stack.pop()]:
                return Result(error_char=c)

    return Result(completion=[closing[c] for c in reversed(stack)])


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    results = [check(line) for line in data_s.splitlines()]
    part1 = sum(result.error_score for result in results)
    part2 = int(median(result.completion_score for result in results if result.ok))

    return part1, part2

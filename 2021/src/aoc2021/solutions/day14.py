"""Solutions for day 14."""
from collections import Counter
from functools import cache


def solve(template: str, rules: dict[tuple[str, str], str], n: int) -> int:
    """Solve the puzzle.

    The key optimization is the use of the cache decorator for memoization.
    This way whole recursive branches that have been counted before
    don't need to be re-counted again.

    Args:
        template: The starting polymer template.
        rules: The insertion rules. The keys are tuples (left, right)
          of two elements and the value is the element to insert between
          left and right.
        n: The number of iterations of the insertion process.

    Returns:
        The count of the most common element minus the count of the least
        common element after all insertion iterations.
    """
    if not len(template):
        return 0

    @cache
    def count_between(left: str, right: str, n: int) -> Counter:
        """Count the elements inserted between left and right.

        The left element is included in the counts, while the right
        one isn't. This avoids overlaps of elements when counting
        in two adjacent intervals.

        Args:
            left: The left element.
            right: The right element.
            n: The number of insertion iterations.

        Returns:
            The counts of all elements after the insertion procedure
            has been repeated n times. The initial right element is
            not included in the counts.
        """
        if n == 0:
            return Counter(left)
        mid = rules[(left, right)]
        return count_between(left, mid, n - 1) + count_between(mid, right, n - 1)

    # For every interval in the template count what's inserted. Since the
    # right-most element is not counted by count_between we need to insert
    # it by hand.
    counts = Counter(template[-1])
    for left, right in zip(template, template[1:]):
        counts += count_between(left, right, n)
    lowest, *_, highest = sorted(counts.values())

    return highest - lowest


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse input
    template, rules_s = data_s.split("\n\n")
    rules = {}
    for rule in rules_s.splitlines():
        left_right, _, mid = rule.partition(" -> ")
        left, right = tuple(left_right)
        rules[(left, right)] = mid

    # Solve
    part1 = solve(template, rules, 10)
    part2 = solve(template, rules, 40)

    return part1, part2

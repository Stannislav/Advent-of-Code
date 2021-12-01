"""Community-inspired solution to day 1."""


def solution(values: list[int], n: int) -> int:
    """Compute the solution to day 1.

    The interesting insight is that for an arbitrary window size only
    the values at the beginning/end of the window that don't overlap
    matter.

    For example, for `n = 3` checking that `a + b + c < b + c + d` is
    equivalent to checking that `a < d`.
    """
    return sum(x < y for x, y in zip(values, values[n:]))


def main() -> None:
    """Compute the solutions."""
    with open("input/01.txt") as fh:
        depths = [int(line) for line in fh]

    print(f"Part 1: {solution(depths, 1)}")
    print(f"Part 2: {solution(depths, 3)}")


if __name__ == "__main__":
    main()

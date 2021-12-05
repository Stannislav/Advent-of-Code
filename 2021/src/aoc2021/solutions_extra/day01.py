"""Community-inspired solution to day 1."""


def solve(values: list[int], n: int) -> int:
    """Compute the solution to day 1.

    The interesting insight is that for an arbitrary window size only
    the values at the beginning/end of the window that don't overlap
    matter.

    For example, for `n = 3` checking that `a + b + c < b + c + d` is
    equivalent to checking that `a < d`.
    """
    return sum(x < y for x, y in zip(values, values[n:]))


def run(raw_data: str) -> tuple[int, int]:
    """Compute the solutions."""
    depths = [int(line) for line in raw_data.splitlines()]

    return solve(depths, 1), solve(depths, 3)


if __name__ == "__main__":
    with open("input/01.txt") as fh:
        raw_data = fh.read()

    p1, p2 = run(raw_data)
    print(f"Part 1: {p1}")
    print(f"Part 2: {p2}")

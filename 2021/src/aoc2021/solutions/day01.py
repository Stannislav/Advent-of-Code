"""Solutions for day 1."""


def grad(values: list[int], /) -> list[int]:
    """Compute the gradient of a sequence of values."""
    return [v2 - v1 for v1, v2 in zip(values, values[1:])]


def sliding_sum(values: list[int], *, window_size: int) -> list[int]:
    """Compute the sliding window sum of a sequence of values."""
    n_windows = len(values) - window_size + 1
    return [sum(values[i : i + window_size]) for i in range(n_windows)]


def count_positive(values: list[int], /) -> int:
    """Count the number of positive values in a sequence."""
    return sum(x > 0 for x in values)


def run(data: str) -> tuple[int, int]:
    """Compute the solutions."""
    depths = [int(line) for line in data.splitlines()]
    part1 = count_positive(grad(depths))
    part2 = count_positive(grad(sliding_sum(depths, window_size=3)))

    return part1, part2

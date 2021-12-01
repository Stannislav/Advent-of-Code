"""Solutions for day 1."""


def grad(values):
    """Compute the gradient of a sequence of values."""
    return [v2 - v1 for v1, v2 in zip(values, values[1:])]


def sliding_sum(values, window_size):
    """Compute the sliding window sum of a sequence of values."""
    n_windows = len(values) - window_size + 1
    return [sum(values[i : i + window_size]) for i in range(n_windows)]


def count_positive(values):
    """Count the number of positive values in a sequence."""
    return sum(x > 0 for x in values)


def main():
    """Compute the solutions."""
    with open("input/01.txt") as fh:
        depths = [int(line) for line in fh]

    print(f"Part 1: {count_positive(grad(depths))}")
    print(f"Part 2: {count_positive(grad(sliding_sum(depths, 3)))}")


if __name__ == "__main__":
    main()

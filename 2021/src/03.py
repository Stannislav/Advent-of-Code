"""Solutions for day 2."""
N_BITS = 12
BIT_MASK = (1 << N_BITS) - 1  # e.g. 0b111 for N_BITS = 3

Data = list[int]


def most_common(numbers: Data, bit_pos: int) -> int:
    """Count the most common bit value at a given bit position."""
    count = sum((n >> bit_pos) & 1 for n in numbers)

    return int(count * 2 >= len(numbers))


def inv(n: int) -> int:
    """Compute the bitwise inverse."""
    return ~n & BIT_MASK


def part1(data: Data) -> int:
    """Compute the part 1 solution.

    The gamma rate is an integer that consists of the most common bits
    in the input data. The epsilon rate consists of the least common bits
    in the input data and is therefore the bitwise inverse of the gamma rate.

    :param data: The puzzle input data.
    :return: The part 1 solution.
    """
    gamma = 0
    for bit_pos in range(N_BITS):
        gamma |= most_common(data, bit_pos) << bit_pos
    epsilon = inv(gamma)

    return gamma * epsilon


def keep_most_common(values: Data, *, least_common: bool = False) -> int:
    """Run the bit criteria loop for part 2.

    :param values: The puzzle input data.
    :param least_common: If true the criteria is the least common bit,
    otherwise it's the most common bit.
    :return: The single remaining value after the criteria loop.
    """
    pos = N_BITS
    while len(values) > 1:
        pos -= 1
        bit_want = most_common(values, pos)
        if least_common:
            bit_want = 1 - bit_want

        values = [n for n in values if (n >> pos) & 1 == bit_want]

    return values.pop()


def part2(data: list[int]) -> int:
    """Compute the part 2 solution."""
    oxygen = keep_most_common(data)
    co2 = keep_most_common(data, least_common=True)

    return oxygen * co2


def main() -> None:
    """Solve the puzzles."""
    with open("input/03.txt") as fh:
        data = [int(line, base=2) for line in fh]

    print(f"Part 1: {part1(data)}")
    print(f"Part 2: {part2(data)}")


if __name__ == "__main__":
    main()

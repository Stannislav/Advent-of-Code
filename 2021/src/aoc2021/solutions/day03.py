"""Solutions for day 3."""
Data = list[int]


def most_common(numbers: Data, bit_pos: int) -> int:
    """Count the most common bit value at a given bit position."""
    count = sum((n >> bit_pos) & 1 for n in numbers)

    return int(count * 2 >= len(numbers))


def inv(n: int, n_bits: int) -> int:
    """Compute the bitwise inverse.

    :param n: An integer.
    :param n_bits: The bit-width of the integers used.
    :returns: The binary inverse of the input.
    """
    # We should only invert the bits that are within the bit-width of the
    # integers we use. We set this mask to set the other bits to zero.
    bit_mask = (1 << n_bits) - 1  # e.g. 0b111 for n_bits = 3

    return ~n & bit_mask


def part1(data: Data, n_bits: int) -> int:
    """Compute the part 1 solution.

    The gamma rate is an integer that consists of the most common bits
    in the input data. The epsilon rate consists of the least common bits
    in the input data and is therefore the bitwise inverse of the gamma rate.

    :param data: The puzzle input data.
    :param n_bits: The bit-width of the integers used.
    :return: The part 1 solution.
    """
    gamma = 0
    for bit_pos in range(n_bits):
        gamma |= most_common(data, bit_pos) << bit_pos

    epsilon = inv(gamma, n_bits)

    return gamma * epsilon


def keep_most_common(values: Data, n_bits: int, *, least_common: bool = False) -> int:
    """Run the bit criteria loop for part 2.

    :param values: The puzzle input data.
    :param n_bits: The bit-width of the integers used.
    :param least_common: If true the criteria is the least common bit,
    otherwise it's the most common bit.
    :return: The single remaining value after the criteria loop.
    """
    pos = n_bits
    while len(values) > 1:
        pos -= 1
        bit_want = most_common(values, pos)
        if least_common:
            bit_want = 1 - bit_want

        values = [n for n in values if (n >> pos) & 1 == bit_want]

    return values.pop()


def part2(data: list[int], n_bits: int) -> int:
    """Compute the part 2 solution."""
    oxygen = keep_most_common(data, n_bits)
    co2 = keep_most_common(data, n_bits, least_common=True)

    return oxygen * co2


def run(raw_data: str) -> tuple[int, int]:
    """Solve the puzzles."""
    lines = raw_data.splitlines()
    n_bits = len(lines[0])
    data = [int(line, base=2) for line in lines]

    return part1(data, n_bits), part2(data, n_bits)

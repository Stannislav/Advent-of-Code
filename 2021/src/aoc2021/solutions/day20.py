"""Solutions for day 20."""

Image = list[list[int]]
Bits = tuple[int, ...]


def kernel(i: int, j: int, img: Image) -> Bits:
    """Get the kernel value in image at given position.

    The kernel is a 3x3 patched centered around (i, j). Its value
    is the sequence of the 9 pixel values covered by the patch.

    Args:
        i: The i-coordinate of the pixel.
        j: The j-coordinate of the pixel.
        img: The image.

    Returns:
        The kernel value computed as the sequence of pixel values
        covered by it.
    """
    s = slice(j - 1, j + 2)
    return *img[i - 1][s], *img[i][s], *img[i + 1][s]


def bits(n: int, length: int = 9) -> Bits:
    """Convert an integer to a sequence of bit values.

    Args:
        n: The integer value to burst into bits.
        length: The number of bits to convert the integer to.

    Returns:
        The sequence of bits in n.
    """
    return tuple((n >> i) & 1 for i in reversed(range(length)))


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles.

    What to do with the pixels at infinity? As long as they always map to
    zero there's no problem, they'll never light up. This happens when the
    algorithm maps 0b000000000 to 0. But this is not the case for the
    puzzle input.

    Since 0b000000000 maps to 1 all the infinite points will light up,
    and we can't keep track of them all. But since 0b111111111 maps back
    to 0 after an even number of maps a finite number of pixels will be on.

    So to deal with this we can keep track of on-pixels after even steps
    and of off-pixels after odd steps. Equivalently we can invert the
    pixels after every map and always keep track of on-pixels. This can be
    done by directly inverting the values in the map. E.g., mapping
    0b000000000 to 1 and then inverting to 0 is the same as mapping
    0b000000000 to 0 directly. Because we're inverting the values, at the
    next step the keys will have to be inverted. Also, the values have to be
    flipped again and go back to the original values. So, in total:

        even_algo: key ->  ~value
        odd_algo : ~key -> value

    In the simpler case where pixels at infinity don't flip we don't need
    any of this switching logic and can simply set:

        even_algo: key -> value
        odd_algo : key -> value
    """
    # Parsing
    code_s, img_s = data_s.split("\n\n")

    # Parse algorithm
    code = {i: 1 if c == "#" else 0 for i, c in enumerate(code_s)}

    # Determine if we have the case with flipping pixels at infinity
    if code[0b000000000] == 0 and code[0b111111111] == 1:  # non-alternating
        algorithms_ = {0: code, 1: code}
    elif code[0b000000000] == 1 and code[0b111111111] == 0:  # alternating
        algorithms_ = {
            0: {k: 1 - v for k, v in code.items()},
            1: {(~k) & 0b111111111: v for k, v in code.items()},
        }
    else:
        raise ValueError("Invalid algorithm: always infinite number of pixels lit up.")

    # Burst keys into tuples of bit values
    algorithms = {i: {bits(k): v for k, v in a.items()} for i, a in algorithms_.items()}

    # Parse image
    lines = img_s.splitlines()
    img = [[1 if c == "#" else 0 for c in line] for line in lines]

    # Pad image to final size
    steps = 50
    pad = steps + 2  # +1 pixel per step, need another +1 for its kernel width
    ni = 2 * pad + len(lines)
    nj = 2 * pad + len(lines[0])
    img = [[0] * pad + row + [0] * pad for row in img]
    img = [[0] * nj for _ in range(pad)] + img + [[0] * nj for _ in range(pad)]

    # Solve
    sums = [sum(sum(row) for row in img)]
    for n in range(steps):
        new_img = [[0] * nj for _ in range(ni)]
        for i in range(1, ni - 1):
            for j in range(1, nj - 1):
                new_img[i][j] = algorithms[n % 2][kernel(i, j, img)]
        img = new_img
        sums.append(sum(sum(row) for row in img))

    return sums[2], sums[50]

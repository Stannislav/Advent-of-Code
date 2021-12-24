"""Solutions for day 24."""
from typing import Iterable

Cmd = list[str]


class ALU:
    """The ALU unit for the puzzle."""

    def __init__(self, cmds: list[Cmd]) -> None:
        """Initialise the ALU unit with the given command set."""
        self.cmds = cmds
        self.reg: dict[str, int] = {"w": 0, "x": 0, "y": 0, "z": 0}

    def resolve(self, val: str) -> int:
        """Resolve either an int literal or a register into an int value."""
        if val in self.reg:
            return self.reg[val]
        else:
            return int(val)

    def __call__(self, inp: Iterable[int] | None = None) -> dict[str, int]:
        """Run the ALU command set on the given input."""
        inputs = iter(inp or [])
        for op, *params in self.cmds:
            match op:
                case "inp":
                    try:
                        val = next(inputs)
                    except StopIteration:
                        raise ValueError("Not enough inputs") from None
                    self.reg[params[0]] = val
                case "add":
                    self.reg[params[0]] += self.resolve(params[1])
                case "mul":
                    self.reg[params[0]] *= self.resolve(params[1])
                case "div":
                    self.reg[params[0]] //= self.resolve(params[1])
                case "mod":
                    self.reg[params[0]] %= self.resolve(params[1])
                case "eql":
                    a = self.resolve(params[0])
                    b = self.resolve(params[1])
                    self.reg[params[0]] = int(a == b)
                case _:
                    raise ValueError(f"Unknown operator: {op}")

        state = self.reg.copy()
        self.reg = {"w": 0, "x": 0, "y": 0, "z": 0}

        return state


def correct_input(inp: list[int], cmds: list[Cmd]) -> list[int]:
    """Correct an input sequence so that it passes the test.

    The input consists of 14 processing blocks - each for one of the inputs.
    Each of them has the same structure and is parametrized by three
    variables, let's call them "div", "chk", and "add". Written in
    pseudocode and using "inp" for the input value each block performs
    the following computation:

        x = (z % 26 + chk) != inp
        z //= div
        z *= 25 * x + 1
        z += (inp + add) * x

    This can be rewritten using an if-block, which eliminates the x-register:

        if z % 26 + chk != inp:
            z //= div
            z *= 26
            z += inp + add
        else:
            z //= div

    We note that "div" can only be one of two values: either 1 or 26. This
    leads us to observe that all computations are manipulations of digits
    of the z-register written in base 26. So it's natural to define
    "div = 26**shf", so that "shf" will be either 0 or 1. We can use
    binary operators to denote operations in base 26 as follows:

        z * 26  = z << 1
        z // 26 = z >> 1
        z % 26  = z & 1

    With this we can write the program as follows:

        if z & 1 + chk != inp:
            z = z >> shf
            z = z << 1
            z = z + (inp + add)
        else:
            z = z >> shf

    We can also write the bitwise operations as follows:

        z & 1 = z.last_bit
        z >> 1 = z.pop()
        (z << 1) & a = z.push(a)
        (z >> 1) << 1) & a = z.pop_push(a)

    where pop/push refer to the bit stack of z in base 26 with the last
    bit on top. Therefore, z.pop() removes the last bit, z.push(a) appends
    the bit "a", and z.pop_push(a) replaces the last bit by "a".

    Given that shf can only be 0 or 1 we get the following two cases:

        if shf == 0:
            if z.last_bit + chk != inp:
                z.push(inp + add)
        elif shf == 1:
            if z.last_bit + chk != inp:
                z.pop_push(inp + add)
            else:
                z.pop()

    According to the puzzle input (our input) in all cases where shf == 0
    it's true that chk > 9. Given that 1 <= inp <= 9 the check
    (if z.last_bit + chk != inp) will therefore always be true. This gives:

        if shf == 0:
            z.push(inp + add)
        elif shf == 1:
            if z.last_bit + chk == inp:
                z.pop()
            else:
                z.pop_push(inp + add)

    We can summarize in words. View z as a stack of bits in base 26. Start
    with an empty stack. Whenever shf == 0 (div == 1) push (inp + add) on
    the stack. If, however, shf == 1, consider the last bit on the stack.
    If it's equal to (inp - chk), then remove it, otherwise replace it by
    (inp + add).

    We also observe from the puzzle input (our input) that among the 14
    instruction blocks for each of the inputs there are exactly 7 cases
    with shf == 0 and 7 with shf == 1. Given that for shf == 0 something
    is always added to the stack, our goal is to arrange the input so
    that for shf == 1 it's always popped from the sack, so that at the end
    of the program we end up with an empty bit stack, which means that
    z == 0, which makes the input pass the test.

    To arrange this start with an arbitrary array of 14 inputs denoted
    by [inp_0, inp_1, ..., inp_13]. If the first two instruction blocks
    have shf_0 == 0 and shf_1 == 0 then after the first two inputs two
    bits will have been pushed to the stack:

        z_stack = [inp_0 + add_0, inp_1 + add_1]

    If then shf_2 == 1 we want to set inp_2 so that the last bit is popped.
    The last bit is popped if

            z.last_bit + chk_2 == inp_2
        =>  inp_1 + add_1 + chk_2 == inp_2

    So we set (inp_2 = inp_1 + add_1 + chk_2). It can now occur that the
    condition 1 <= inp_2 <= 9 is violated. In this case we can add an
    arbitrary value to inp_2 to restore this condition. We will need to
    add the same value to inp_1 too in order to maintain the previous
    equality. We need to be careful that after these adjustments we also
    maintain 1 <= inp_1 <= 9. The least we can do is for cases where
    inp_2 < 1 to choose the value so that inp_2 = 1 and for cases with
    inp_2 > 9 to choose the value so that inp_2 = 9. If this still doesn't
    work for inp_1, then no other value will work for both either.

    This strategy can be used to take any wish input sequence and correct
    it so that it passes the test. So for part 1 we'll want to start with
    the highest possible input, 99999999999999, and for part 2 with the
    lowest, 11111111111111.

    Programmatically, to correct the input we go through code subroutines
    that handle each of the inputs and extract the (shf, chk, add) parameters.
    If shf == 0 we remember the "add" parameter by pushing it on a stack, and
    we also remember which input it corresponds to. If shf == 1 we pop the
    last "add" from the stack and use it to compute the corrected input.


    Args:
        inp: The input array to correct.
        cmds: The input commands

    Returns:
        The corrected input array.
    """
    # copy input array
    inp = list(inp)

    # correct input
    sub_len = 18  # length of the subroutine for each of the inputs.
    line_nos = [4, 5, 15]  # line numbers with the (div, chk, add) parameters.
    stack = []
    for i in range(14):
        div_chk_add = [cmds[i * sub_len + x][-1] for x in line_nos]
        div, chk, add = map(int, div_chk_add)
        if div == 1:
            stack.append((i, add))
        elif div == 26:
            j, add = stack.pop()
            # Set the input so that the test passes
            inp[i] = inp[j] + add + chk
            # Correct the input so that 1 <= inp <= 9
            if inp[i] > 9:
                inp[j] = inp[j] - (inp[i] - 9)
                inp[i] = 9
            if inp[i] < 1:
                inp[j] = inp[j] + (1 - inp[i])
                inp[i] = 1

    return inp


def check(inp: list[int], cmds: list[Cmd]) -> bool:
    """Check if the input sequence passes the test."""
    alu = ALU(cmds)
    reg = alu(inp)
    if reg["z"] == 0:
        return True
    else:
        return False


def solve(wish_inp: list[int], cmds: list[Cmd]) -> str:
    """Solve the puzzle by correcting the given wish input sequence."""
    inp = correct_input(wish_inp, cmds)
    solution = "".join(map(str, inp))
    if not check(inp, cmds):
        raise RuntimeError(f"Part 1 solution doesn't pass the test: {solution}")

    return solution


def run(data_s: str) -> tuple[str, str]:
    """Solve the puzzles."""
    cmds = [line.split() for line in data_s.splitlines()]
    part1 = solve([9] * 14, cmds)
    part2 = solve([1] * 14, cmds)

    return part1, part2

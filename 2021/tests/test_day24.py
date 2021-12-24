import pytest

from aoc2021.solutions import day24

# Negate the input
# Result in reg["x"]
data1 = """\
inp x
mul x -1
"""

# 1 if second input is three times the first input, otherwise 0
# Result in reg["z"]
data2 = """\
inp z
inp x
mul z 3
eql z x
"""
# Extract the lowest 4 bits from input and place them into registers
# "w", "x", "y", and "z".
data3 = """\
inp w
add z w
mod z 2
div w 2
add y w
mod y 2
div w 2
add x w
mod x 2
div w 2
mod w 2
"""


@pytest.mark.parametrize(
    ("data", "inp", "out"),
    (
        (data1, [5], {"x": -5}),
        (data1, [0], {"x": 0}),
        (data1, [-7], {"x": 7}),
        (data2, [2, 3], {"z": 0}),
        (data2, [2, 2 * 3], {"z": 1}),
        (data3, [0b1001], {"w": 1, "x": 0, "y": 0, "z": 1}),
        (data3, [0b0011], {"w": 0, "x": 0, "y": 1, "z": 1}),
        (data3, [0b1010], {"w": 1, "x": 0, "y": 1, "z": 0}),
        (data3, [0b0100], {"w": 0, "x": 1, "y": 0, "z": 0}),
        (data3, [0b1100100100], {"w": 0, "x": 1, "y": 0, "z": 0}),
    ),
)
def test_alu(data, inp, out):
    cmds = [line.split() for line in data.splitlines()]
    alu = day24.ALU(cmds)
    reg = alu(inp)
    for key, value in out.items():
        assert reg[key] == value

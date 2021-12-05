from aoc2021.solutions import day03

data = """\
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
"""


def test_my_solution():
    part1, part2 = day03.run(data)
    assert part1 == 198
    assert part2 == 230

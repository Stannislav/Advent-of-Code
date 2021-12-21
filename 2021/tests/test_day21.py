from aoc2021.solutions import day21

data = """\
Player 1 starting position: 4
Player 2 starting position: 8
"""


def test_my_solution():
    part1, part2 = day21.run(data)
    assert part1 == 739785
    assert part2 == 444356092776315

from aoc2021.solutions import day02
from aoc2021.solutions_extra import day02 as day02extra

data = """\
forward 5
down 5
forward 8
up 3
down 8
forward 2
"""


def test_my_solution():
    part1, part2 = day02.run(data)
    assert part1 == 150
    assert part2 == 900


def test_community_solution():
    part1, part2 = day02extra.run(data)
    assert part1 == 150
    assert part2 == 900

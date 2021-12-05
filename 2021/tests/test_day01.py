from aoc2021.solutions import day01
from aoc2021.solutions_extra import day01 as day01extra

data = """\
199
200
208
210
200
207
240
269
260
263
"""


def test_my_solution():
    part1, part2 = day01.run(data)
    assert part1 == 7
    assert part2 == 5


def test_community_solution():
    part1, part2 = day01extra.run(data)
    assert part1 == 7
    assert part2 == 5

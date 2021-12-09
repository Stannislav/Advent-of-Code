from aoc2021.solutions import day09

data = """\
2199943210
3987894921
9856789892
8767896789
9899965678
"""


def test_my_solution():
    part1, part2 = day09.run(data)
    assert part1 == 15
    assert part2 == 1134

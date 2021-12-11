from aoc2021.solutions import day11

data = """\
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
"""


def test_my_solution():
    part1, part2 = day11.run(data)
    assert part1 == 1656
    assert part2 == 195

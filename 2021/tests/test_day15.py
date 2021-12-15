from aoc2021.solutions import day15

data = """\
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
"""


def test_my_solution():
    part1, part2 = day15.run(data)
    assert part1 == 40
    assert part2 == 315

from aoc2021.solutions import day06

data = "3,4,3,1,2"


def test_my_solution():
    part1, part2 = day06.run(data)
    assert part1 == 5934
    assert part2 == 26984457539

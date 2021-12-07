from aoc2021.solutions import day07

data = "16,1,2,0,4,2,7,1,2,14"


def test_my_solution():
    part1, part2 = day07.run(data)
    assert part1 == 37
    assert part2 == 0

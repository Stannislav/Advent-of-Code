from aoc2021.solutions import day17

data = "target area: x=20..30, y=-10..-5"


def test_my_solution():
    part1, part2 = day17.run(data)
    assert part1 == 45
    assert part2 == 112

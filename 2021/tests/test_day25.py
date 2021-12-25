from aoc2021.solutions import day25

data = """\
v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>
"""


def test_my_solution():
    part1, part2 = day25.run(data)
    assert part1 == 58
    assert part2 == 0

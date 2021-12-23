from aoc2021.solutions import day23

data = """\
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########
"""


def test_my_solution():
    part1, part2 = day23.run(data)
    assert part1 == 12521
    assert part2 == 44169

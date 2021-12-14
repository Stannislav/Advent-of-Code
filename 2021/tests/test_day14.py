from aoc2021.solutions import day14

data = """\
NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
"""


def test_my_solution():
    part1, part2 = day14.run(data)
    assert part1 == 1588
    assert part2 == 2188189693529

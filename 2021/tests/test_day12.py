import pytest

from aoc2021.solutions import day12

data1 = """\
start-A
start-b
A-c
A-b
b-d
A-end
b-end
"""

data2 = """\
dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc
"""

data3 = """\
fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
"""


@pytest.mark.parametrize(
    ("data", "part1_true", "part2_true"),
    (
        (data1, 10, 36),
        (data2, 19, 103),
        (data3, 226, 3509),
    ),
)
def test_my_solution(data, part1_true, part2_true):
    part1, part2 = day12.run(data)
    assert part1 == part1_true
    assert part2 == part2_true

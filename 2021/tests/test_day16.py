import pytest

from aoc2021.solutions import day16

data1 = "8A004A801A8002F478"
data2 = "620080001611562C8802118E34"
data3 = "C0015000016115A2E0802F182340"
data4 = "A0016C880162017C3686B18A3D4780"


@pytest.mark.parametrize(
    ("data", "part1_expected", "part2_expected"),
    (
        (data1, 16, 0),
        (data2, 12, 0),
        (data3, 23, 0),
        (data4, 31, 0),
    )
)
def test_my_solution(data, part1_expected, part2_expected):
    part1, part2 = day16.run(data)
    assert part1 == part1_expected
    assert part2 == part2_expected

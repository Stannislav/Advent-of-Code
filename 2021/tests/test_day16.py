import pytest

from aoc2021.solutions import day16


@pytest.mark.parametrize(
    ("data", "part1_expected", "part2_expected"),
    (
        ("8A004A801A8002F478", 16, 15),
        ("620080001611562C8802118E34", 12, 46),
        ("C0015000016115A2E0802F182340", 23, 46),
        ("A0016C880162017C3686B18A3D4780", 31, 54),
        ("C200B40A82", 14, 3),
        ("04005AC33890", 8, 54),
        ("880086C3E88112", 15, 7),
        ("CE00C43D881120", 11, 9),
        ("D8005AC2A8F0", 13, 1),
        ("F600BC2D8F", 19, 0),
        ("9C005AC2F8F0", 16, 0),
        ("9C0141080250320F1802104A08", 20, 1),
    ),
)
def test_my_solution(data, part1_expected, part2_expected):
    part1, part2 = day16.run(data)
    assert part1 == part1_expected
    assert part2 == part2_expected

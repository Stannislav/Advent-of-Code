"""Solutions for day 8."""
from typing import Iterable

# A signal is a set of active wires. Each wire is denoted by a letter
Signal = set[str]

# Wires are denoted by single letters, segments by a digit 0-7. A wire
# to segment map specifies which wire is connected to which segment.
WireSegmentMap = dict[str, int]

# Digit representations on a seven-segment display. We number the segments
# in the following way:
#  -- 0 --
#  1     2
#  -- 3 --
#  4     5
#  -- 6 --
digits = {
    frozenset({0, 1, 2, 4, 5, 6}): 0,
    frozenset({2, 5}): 1,
    frozenset({0, 2, 3, 4, 6}): 2,
    frozenset({0, 2, 3, 5, 6}): 3,
    frozenset({1, 2, 3, 5}): 4,
    frozenset({0, 1, 3, 5, 6}): 5,
    frozenset({0, 1, 3, 4, 5, 6}): 6,
    frozenset({0, 2, 5}): 7,
    frozenset({0, 1, 2, 3, 4, 5, 6}): 8,
    frozenset({0, 1, 2, 3, 5, 6}): 9,
}


def translate(signals: Iterable[Signal], wire_to_segment: WireSegmentMap) -> int:
    """Translate digits encoded by signals to a number.

    Each signal in the sequence of signals represents one digit. These
    digits put next to each other form the output number. For example,
    if the digit sequence encoded in signals is (5, 3, 8, 4) then the
    resulting number is 5384.

    Args:
        signals: A sequence of signals.
        wire_to_segment: A map that translates wire signals to digit segments.

    Returns:
        The translated number.
    """
    n = 0
    for signal in signals:
        segments = frozenset({wire_to_segment[wire] for wire in signal})
        n = n * 10 + digits[segments]

    return n


def decode(signals: Iterable[Signal]) -> WireSegmentMap:
    """Decode the signal patterns and determine the wire to segment map.

    Approach: we want to know which wire connects to which segment. Start
    with each segment having each wire as a potential candidate. This is
    the initial state of `segment_to_wire`. Iterate through all example
    signals and update `segment_to_wire` until for each segment only one
    candidate wire remains.

    There are three logical steps that are performed during each iteration.

    1. We know that signals with 2, 3, and 4 wires (and 7, but it's useless)
       correspond to unique digits (1, 7, and 4). So we update our knowledge
       with the fact that the candidate wires for the respective digit must
       be one of the active wires in the signal, and also that the other
       segments that are not part of the digit cannot have these wires.
    2. There are 3 digits with 6 segments/wires: 0, 6, 9. So for each of them
       only one single segment is off. Therefore, we know that the one wire that
       is off can only be one of the 3 segments, a different one for each digit.
       So we infer that this wire cannot connect to any of the complimentary
       segments.
    3. After applying the two rules above we check if there are any segments
       with only one wire candidate - these segments are solved. So we know
       that the corresponding wires cannot be candidates for other segments
       and therefore remove them from candidate lists.

    It turns out these three rules are enough to solve both the example and
    the real problem.

    Args:
        signals: A sequence of wire signals.

    Returns:
        A map from wires to segments they connect to.
    """
    segment_to_wire = [set("abcdefg") for _ in range(7)]
    while not all(len(wire) == 1 for wire in segment_to_wire):
        for signal in signals:
            match len(signal):
                # We have the "1" digit. So the active wires must be on
                # segments 2 and 5.
                case 2:
                    segment_to_wire[2] &= signal
                    segment_to_wire[5] &= signal
                    segment_to_wire[0] -= signal
                    segment_to_wire[1] -= signal
                    segment_to_wire[3] -= signal
                    segment_to_wire[4] -= signal
                    segment_to_wire[6] -= signal
                # We have the "7" digit. So the active wires must be on
                # segments 0, 2, and 5.
                case 3:
                    segment_to_wire[0] &= signal
                    segment_to_wire[2] &= signal
                    segment_to_wire[5] &= signal
                    segment_to_wire[1] -= signal
                    segment_to_wire[4] -= signal
                    segment_to_wire[6] -= signal
                # We have the "4" digit. So the active wires must be on
                # segments 1, 2, 3, and 5.
                case 4:
                    segment_to_wire[1] &= signal
                    segment_to_wire[2] &= signal
                    segment_to_wire[3] &= signal
                    segment_to_wire[5] &= signal
                    segment_to_wire[0] -= signal
                    segment_to_wire[4] -= signal
                    segment_to_wire[6] -= signal
                # We have one of the ("0", "6", "9") digits, for each of them
                # only one segment is off: 3, 2, or 4 respectively. So the
                # off-wire must be one of these three and cannot connect to
                # any of the four complimentary segments.
                case 6:
                    off_wire = set("abcdefg") - signal
                    for i in {0, 1, 5, 6}:
                        segment_to_wire[i] -= off_wire

        # Segments with only one candidate wire remaining are solved. Collect
        # all solved wires are remove them from candidate lists of segments
        # that are not yet solved.
        solved_wires = set()
        for wire in segment_to_wire:
            if len(wire) == 1:
                solved_wires |= wire
        for wire in segment_to_wire:
            if len(wire) > 1:
                wire -= solved_wires

    wire_to_segment = {wire.pop(): i for i, wire in enumerate(segment_to_wire)}

    return wire_to_segment


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse input
    lines = []
    for line in data_s.splitlines():
        left, *_, right = line.partition(" | ")
        left_signals = [set(signal) for signal in left.split(" ")]
        right_signals = [set(signal) for signal in right.split(" ")]
        lines.append((left_signals, right_signals))

    # Solve
    part1 = sum(len(signal) in {2, 3, 4, 7} for _, right in lines for signal in right)
    part2 = sum(translate(right, decode(left)) for left, right in lines)

    return part1, part2

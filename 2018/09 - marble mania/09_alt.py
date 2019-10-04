#!/usr/bin/env python3

import re
from collections import deque, defaultdict


with open('09_input.txt', 'r') as f:
    n_players, last_value = [int(n) for n in re.findall('\d+', f.read())]


def solve(n_players, last_value):
    scores = defaultdict(int)
    circle = deque([0])

    for marble in range(1, last_value + 1):
        if marble % 23:
            circle.rotate(-1)
            circle.append(marble)
        else:
            circle.rotate(7)
            scores[marble % n_players] += marble + circle.pop()
            circle.rotate(-1)

    return max(scores.values())


print("Part 1:", solve(n_players, last_value))
print("Part 2:", solve(n_players, last_value * 100))

#!/usr/bin/env python3

from collections import Counter
from itertools import combinations


# Input
with open('02_input.txt', 'r') as f:
    ids = [line.strip() for line in f]


# Part 1
c2, c3 = 0, 0
for id in ids:
    cnt = Counter(id).values()
    c2 += (2 in cnt)
    c3 += (3 in cnt)

print("Part 1:", c2 * c3)


# Part 2
x = len(ids[0]) - 1
for id1, id2 in combinations(ids, 2):
    same = [c1 for c1, c2 in zip(id1, id2) if c1 == c2]

    if len(same) == x:
        print("Part 2:", ''.join(same))
        break

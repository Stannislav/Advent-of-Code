#!/usr/bin/env python3
from functools import reduce

with open("../input/input_02.txt", 'r') as f:
    paper = 0
    ribbon = 0
    for line in f:
        sides = list(map(int, line.rstrip().split('x')))
        surf = [sides[i] * sides[(i + 1) % 3] for i in range(3)]
        paper += 2 * sum(surf) + min(surf)
        ribbon += 2 * (sum(sides) - max(sides)) + reduce(lambda x, y: x * y, sides)
    print("Paper: {}".format(paper))
    print("Ribbon: {}".format(ribbon))

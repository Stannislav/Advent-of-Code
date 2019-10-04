#!/usr/bin/env python3

import numpy as np
import re


# Read input
with open('03_input.txt', 'r') as f:
    lines = [line.strip() for line in f]
    m = len(lines)


# Parse input
patches = np.empty(m, dtype=tuple)
patt = re.compile("#[0-9]+ @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)")
for n, line in enumerate(lines):
    x, y, dx, dy = [int(s) for s in patt.match(line).groups()]
    patches[n] = (slice(x, x + dx), slice(y, y + dy))


# Part 1
fabric = np.zeros((1000, 1000), dtype='int')

for n in range(m):
    fabric[patches[n]] += 1

print("Part 1:", (fabric > 1).sum())


# Part 2
for n in range(m):
    if False not in (fabric[patches[n]] == 1):
        print("Part 2:", n+1)

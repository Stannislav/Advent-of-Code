#!/usr/bin/env python3

import numpy as np
import re


# Read input
with open('03_input.txt', 'r') as f:
    lines = [line.strip() for line in f]
    m = len(lines)


# Parse input
patches = dict()
for n, line in enumerate(lines):
    cid, x, y, dx, dy = [int(s) for s in re.findall(r'\d+', line)]
    patches[cid] = (slice(x, x + dx), slice(y, y + dy))


# Apply patches
fabric = np.zeros((1000, 1000), 'int')
for v in patches.values():
    fabric[v] += 1


# Part 1
print("Part 1:", (fabric > 1).sum())


# Part 2
for k, v in patches.items():
    if False not in (fabric[v] == 1):
        print("Part 2:", k)

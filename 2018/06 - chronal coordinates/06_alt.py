#!/usr/bin/env python3

import re
from collections import defaultdict


# Read input
with open('06_input.txt', 'r') as f:
    points = [tuple(int(c) for c in re.findall(r"\d+", line)) for line in f]


# Define number of points, and coordinates of the bounding box
n = len(points)
N = 10000
w_min = min([p[0] for p in points]) - N // n
h_min = min([p[1] for p in points]) - N // n
w_max = max([p[0] for p in points]) + N // n
h_max = max([p[1] for p in points]) + N // n

in_region = set()
space = {}

for h in range(h_min, h_max + 1):
    for w in range(w_min, w_max + 1):
        cumul_dist = 0
        closest_dist = float('inf')
        closest = points[0]
        for (pw, ph) in points:
            dist = abs(w - pw) + abs(h - ph)
            if dist < closest_dist:
                closest_dist = dist
                closest = (pw, ph)
            elif dist == closest_dist and closest != (pw, ph):
                closest = None
            cumul_dist += dist
        space[(w, h)] = closest
        if cumul_dist < N:
            in_region.add((w, h))

tally = defaultdict(int)
for p in space:
    if space[p] == None:
        continue
    if p[0] in (w_min, w_max) or p[1] in (h_min, h_max):
        tally[space[p]] = float('-inf')
    tally[space[p]] += 1

print("Part 1:", max(tally.values()))
print("Part 2:", len(in_region))

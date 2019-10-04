#!/usr/bin/env python3

import numpy as np


input = 8561


# Calculate partial sums
ps = np.zeros((301, 301))
for x in range(301):
    rackID = x + 10
    for y in range(301):
        power = ((rackID * y + input) * rackID // 100) % 10 - 5
        ps[x, y] = power + ps[x, y-1] + ps[x-1, y] - ps[x-1, y-1]


# Save the best power for each given cell size
best = dict()
# note: here x and y label the bottom right corner, not the top left
for s in range(1, 301):
    best[s] = (-1, -1, float('-inf'))
    for x in range(s, 301):
        for y in range(s, 301):
            power = ps[x, y] - ps[x-s, y] - ps[x, y-s] + ps[x-s, y-s]
            if power > best[s][-1]:
                best[s] = (x-s+1, y-s+1, power)


print("Part 1:", *best[3][:2])

best_s = max(best.keys(), key=lambda s: best[s][-1])
print("Part 2:", *best[best_s][:2], best_s)

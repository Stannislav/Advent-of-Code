#!/usr/bin/env python3


input = 8561


# Calculate the power of all 1x1 cells
power = dict()
for x in range(1, 301):
    rackID = x + 10
    for y in range(1, 301):
        power[(x, y, 1)] = ((rackID * y + input) * rackID // 100) % 10 - 5


# Calculate the powers of all nxn cells
# For even n the power is the sum of four (n/2)x(n/2) cells
# For odd n one needs to sum two (n//2+1)x(n//2+1) and two (n//2)x(n//2) cells
# and subtract the central 1x1 cell, which otherwise would be overcounted
for s in range(2, 301):
    for x in range(1, 302 - s):
        for y in range(1, 302 - s):
            if s % 2 == 0:
                d = s / 2
                power[(x, y, s)] = power[(x, y, d)] + power[(x + d, y, d)] + power[(x, y + d, d)] + power[(x + d, y + d, d)]
            else:
                d1 = s // 2 + 1
                d2 = s // 2
                power[(x, y, s)] = power[(x, y, d1)] + power[(x + d2, y + d2, d1)] + power[(x + d1, y, d2)] + power[(x, y + d1, d2)] - power[(x + d2, y + d2, 1)]


print("Part 1:", max((key for key in power.keys() if key[2] == 3), key=lambda k: power[k]))
print("Part 2:", max(power.keys(), key=lambda k: power[k]))

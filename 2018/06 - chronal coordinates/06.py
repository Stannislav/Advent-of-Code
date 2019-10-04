#!/usr/bin/env python3

import re


# Read input
with open('06_input.txt', 'r') as f:
    points = [tuple(int(c) for c in re.findall(r"\d+", line)) for line in f]


# Define number of points, and coordinates of the bounding box
n = len(points)
w_min = min([p[0] for p in points])
h_min = min([p[1] for p in points])
w_max = max([p[0] for p in points])
h_max = max([p[1] for p in points])


# Helper Functions
def dist(p1, p2):
    ''' Computes the Manhattan distance between points p1 and p2 '''
    return abs(p1[0]-p2[0]) + abs(p1[1]-p2[1])


def closest(w, h):
    ''' Find which landmarks are the closest to the given point '''
    howfar = [dist((w, h), p) for p in points]
    m = min(howfar)
    return [i for i, d in enumerate(howfar) if d == m]


# Part 1

# Find which landmarks will produce infinite areas
# by considering all points on the bounding box
exclude = set()
for w in range(w_max):
    c = closest(w, 0)
    if len(c) == 1:
        exclude.add(c[0])
    c = closest(w, h_max)
    if len(c) == 1:
        exclude.add(c[0])
for h in range(h_max):
    c = closest(0, h)
    if len(c) == 1:
        exclude.add(c[0])
    c = closest(w_max, h)
    if len(c) == 1:
        exclude.add(c[0])


# For every point within the bounding box record the closes landmark
tally = [0] * len(points)
for h in range(h_max):
    for w in range(w_max):
        c = closest(w, h)
        if len(c) == 1:
            tally[c[0]] += 1
tally = [t for i, t in enumerate(tally) if i not in exclude]

print("Part 1:", max(tally))


# Part 2
cnt = 0
N = 10000
# For n landmarks any point further away than (N // n) units will have
# the cumulative distance of at least N.
for w in range(w_min - N // n, w_max + N // n + 1):
    for h in range(h_min - N // n, h_max + N // n + 1):
        if sum([dist((w, h), p) for p in points]) < N:
            cnt += 1

print("Part 2:", cnt)

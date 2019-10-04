import re
from collections import defaultdict
from heapq import heappop, heappush


with open('23_input.txt', 'r') as f:
    lines = [line for line in f]

# lines = [
#     "pos=<10,12,12>, r=2",
#     "pos=<12,14,12>, r=2",
#     "pos=<16,12,12>, r=4",
#     "pos=<14,14,14>, r=6",
#     "pos=<50,50,50>, r=200",
#     "pos=<10,10,10>, r=5",
# ]

def get_data():
    return [tuple(map(int, re.findall('-?\d+', line))) for line in lines]


def dist(bot1, bot2=(0, 0, 0)):
    return abs(bot1[0] - bot2[0]) + abs(bot1[1] - bot2[1]) + abs(bot1[2] - bot2[2])


bots = get_data()

"""
SHOULD WE TILE BY OCTOHEDRONS INSTEAD OF BOXES?
then the counting of intersections will be trivial
but they are not space filling...

=> consider overlapping octahedrons with centres on a cubic grid with grid length d
and octahedron size determined by Manhatten distance d. In a cube with side length d
the point the farthest aways from all 8 vertices is the centre and is
sqrt(3 * (d/2)^2) = d*sqrt(3/8) < d away
"""

"""
start with one massive box that contains all bot centers, which has a side lengh d = 2^n
generate smaller boxes with side length d / 2 and compute the number of bots that intersect it
put all candidate boxes on a heap with the following sorting:
(
    n_intersections (bigger first),
    box_size (smaller first),
    box_coords
)
[if we put box_size first then we'll just consider pixel by pixel and not discard bix boxes]
max_intersections <- inf
candidates <- []

while q not empty:
    pop a box
    if n_intersections < max_intersections:
        continue
    if box_size == 1:
        if n_intersections > max_intersections:
            candidates = [box]
        elif n_intersections == max_intersections:
            candidates += [box]
    else:
        subdivide box d -> d/2
        for each new box compute n_intersections
        put each box into heap

from max_intersections choose point closest to origin
"""

r = 1
while not all(dist(b) <= r for b in bots):
    r *= 2

print(sum((dist(bot) - bot[-1]) / r <= 1 for bot in bots))

max_intersections = 0
candidates = []
q = [(-len(bots), r, (0, 0, 0))]
while q:

    n, r, (x, y, z) = heappop(q)
    print(len(q), n, r, x, y, z, max_intersections)
    if n > max_intersections:
        continue
    if r == 1:
        if n < max_intersections:
            candidates = [(x, y, z)]
            max_intersections = n
        elif n == max_intersections:
            candidates += [(x, y, z)]
            max_intersections = n
    else:
        r //= 2
        for nx in (x - r, x, x + r):
            for ny in (y - r, y, y + r):
                for nz in (z - r, z, z + r):
                    nn = 0
                    for bot in bots:
                        d = dist((nx, ny, nz), bot)
                        if (d - bot[-1]) // r <= 0:
                            nn += 1
                    heappush(q, (-nn, r, (nx, ny, nz)))
    # break

# while q:
#     n, r, (x, y, z) = heappop(q)
#     print(len(q), ":", n, r, x, y, z)
#     for bot in bots:
#         d = dist((x, y, z), bot)
#         if (d - bot[-1]) / r <= 1:
#             print(d / r, bot[-1] / r, (d - bot[-1]) / r)
print(candidates)
# candidates = [(22698921, 59279593, 11772354), (22698922, 59279593, 11772355), (22698921, 59279594, 11772355)]
for c in candidates:
    print(dist(c))
    print(sum(dist(bot, c) <= bot[-1] for bot in bots))

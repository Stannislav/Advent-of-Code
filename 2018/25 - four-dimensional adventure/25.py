#/usr/bin/env python3


import re
from collections import defaultdict
from queue import Queue


# Read data
with open('25_input.txt', 'r') as f:
    data = [tuple(map(int, re.findall('-?\d+', line))) for line in f]


# Find all connections between points
conn = defaultdict(set)
for i in range(len(data)):
    for j in range(i+1, len(data)):
        if sum(abs(c1 - c2) for c1, c2 in zip(data[i], data[j])) <= 3:
            conn[i].add(j)
            conn[j].add(i)

'''
Count constellations:
start with the set of all points and pop one random point from this set.
Recursively find all points that are connected to this point and so form a constellation,
and remove all these points from the set as well. Proceed until the set is empty.
'''
points = set(range(len(data)))
count = 0
while points:
    q = Queue()
    q.put(points.pop())
    while not q.empty():
        p = q.get()
        for np in conn[p]:
            if np in points:
                points.remove(np)
                q.put(np)
    count += 1

print('Part 1:', count)
print('Part 2: get all 50 stars...')

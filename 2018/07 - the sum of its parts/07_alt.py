#!/usr/bin/env python3

import re
from heapq import heappush, heappop, heapify
from collections import defaultdict


# Read input
with open('07_input.txt', 'r') as f:
    input = [re.findall(' ([A-Z]) ', line) for line in f]


# Build graph
nxt = defaultdict(set)
prv = defaultdict(set)
deg = defaultdict(int)  # this is the new idea: count the in-degree

for a, b in input:
    nxt[a].add(b)
    prv[b].add(a)
    deg[b] += 1


# Part 1
todo = [c for c in nxt.keys() if deg[c] == 0]
heapify(todo)
done = []
while todo:
    task = heappop(todo)
    if deg[task] == 0:
        done.append(task)
        for n in nxt[task]:
            deg[n] -= 1
            if deg[n] == 0:
                heappush(todo, n)


print("Part 1:", ''.join(done))


# Part 2
deg = defaultdict(int)
for a, b in input:
    deg[b] += 1

available = [c for c in nxt.keys() if deg[c] == 0]
heapify(available)
workers = []
seconds = -1

n_work = 6
dt = 60

while available or workers:
    seconds += 1

    # do work step
    workers = [(task, t-1) for task, t in workers]
    for task, t in workers:
        if t == 0:
            for n in nxt[task]:
                deg[n] -= 1
                if deg[n] == 0:
                    heappush(available, n)
    workers = [(task, t) for task, t in workers if t > 0]

    # Assign new tasks to workers
    while available and len(workers) < n_work:
        task = heappop(available)
        workers.append((task, ord(task) - ord('A') + 1 + dt))

print("Part 2:", seconds)

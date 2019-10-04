#!/usr/bin/env python3

import re
from heapq import heappush, heappop, heapify
from collections import defaultdict


with open('07_input.txt', 'r') as f:
    input = [re.findall(' ([A-Z]) ', line) for line in f]

nxt = defaultdict(set)
req = defaultdict(set)

for a, b in input:
    nxt[a].add(b)
    req[b].add(a)


# Part 1
todo = [c for c in nxt.keys() if not req[c]]
heapify(todo)
done = []
while todo:
    task = heappop(todo)
    if all([r in done for r in req[task]]):
        done.append(task)
        for n in nxt[task]:
            if n not in todo:
                heappush(todo, n)
    else:
        for r in req[task]:
            if r not in todo and r not in done:
                heappush(todo, r)


print("Part 1:", ''.join(done))


# Part 2
available = [c for c in nxt.keys() if not req[c]]
heapify(todo)
seen = [c for c in available]
waiting = []
workers = []
seconds = -1
done = []

n_work = 6
dt = 60

while available or workers:
    seconds += 1

    # do work step
    workers = [(task, t-1) for task, t in workers]
    for task, t in workers:
        if t == 0:
            done.append(task)
    workers = [(task, t) for task, t in workers if t > 0]

    # Update available
    for task in waiting:
        if all([r in done for r in req[task]]):
            heappush(available, task)
    waiting = [task for task in waiting if task not in available]

    # Assign new tasks to workers
    while available and len(workers) < n_work:
        task = heappop(available)
        workers.append((task, ord(task) - ord('A') + 1 + dt))
        for task in nxt[task]:
            if task not in seen:
                    waiting.append(task)
                    seen.append(task)

print("Part 2:", seconds)

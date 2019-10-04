#!/usr/bin/env python3

import re
import numpy as np


# Read file
with open('04_input.txt', 'r') as f:
    lines = [line.strip() for line in f]

lines = sorted(lines)


# Parse lines
guard_log = dict()
patt_guard = re.compile('Guard #([0-9]+)')
patt_minute = re.compile(':([0-9]+)]')
i = 0
while i < len(lines):
    if 'Guard' in lines[i]:
        idx = int(patt_guard.search(lines[i]).groups()[0])
        if idx not in guard_log:
            guard_log[idx] = []
        i += 1
    else:
        sleep = int(patt_minute.search(lines[i]).groups()[0])
        wake = int(patt_minute.search(lines[i+1]).groups()[0])
        times = np.zeros(60, 'int')
        times[sleep:wake] = 1
        guard_log[idx].append(times)
        i += 2

guard_log = {k: np.asarray(v, 'int') for k, v in guard_log.items()}


# Part 1
max_id = max(guard_log.keys(), key=(lambda idx: guard_log[idx].sum()))
max_minute = guard_log[max_id].sum(axis=0).argmax()
print("Part 1:", max_id * max_minute)

# Part 2
max_id = max(guard_log.keys(), key=(lambda idx: guard_log[idx].sum(axis=0).max()))
max_minute = guard_log[max_id].sum(axis=0).argmax()
print("Part 2:", max_id * max_minute)

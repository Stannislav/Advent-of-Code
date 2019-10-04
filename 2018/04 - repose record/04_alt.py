#!/usr/bin/env python3

from collections import defaultdict, Counter


# Read file
with open('04_input.txt', 'r') as f:
    lines = [line.strip() for line in f]


# Parse lines
guards = defaultdict(Counter)
for line in sorted(lines):
    if 'Guard' in line:
        idx = int(line.split('#')[1].split()[0])
    elif 'asleep' in line:
        sleep = int(line.split(':')[1].split(']')[0])
    elif 'wakes' in line:
        wake = int(line.split(':')[1].split(']')[0])
        guards[idx].update(range(sleep, wake))


# Part 1
max_id = max(guards.keys(), key=lambda idx: sum(guards[idx].values()))
max_minute = guards[max_id].most_common(1)[0][0]
print("Part 1:", max_id * max_minute)


# Part 2
max_id = max(guards.keys(), key=lambda idx: guards[idx].most_common(1)[0][1])
max_minute = guards[max_id].most_common(1)[0][0]
print("Part 2:", max_id * max_minute)

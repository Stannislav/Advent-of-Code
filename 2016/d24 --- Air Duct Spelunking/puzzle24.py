#!/usr/bin/env python3

import re

maze = []
wires = {}

with open("input.txt", 'r') as f:
    for i, line in enumerate(f):
        maze.append([0 if c == '#' else 1 for c in line.strip()])
        for m in re.finditer(r'\d+', line):
            wires[int(m.group())] = (m.start(), i)


def gen_perm(n_list):
    if len(n_list) <= 1:
        yield n_list
    else:
        for i in range(len(n_list)):
            for sub_perm in gen_perm(n_list[:i] + n_list[i + 1:]):
                yield [n_list[i]] + sub_perm


def gen_next(p):
    for x, y in [(p[0] - 1, p[1]), (p[0] + 1, p[1]), (p[0], p[1] + 1), (p[0], p[1] - 1)]:
        if maze[y][x]:
            yield (x, y)


def find_path(start, end):
    queue = [start]
    d_visited = {start: [0, None]}
    while len(queue) > 0:
        curr = queue.pop(0)
        for n in gen_next(curr):
            if n not in d_visited:
                d_visited[n] = [float('inf'), None]
            if d_visited[n][0] > d_visited[curr][0] + 1:
                d_visited[n][0] = d_visited[curr][0] + 1
                d_visited[n][1] = curr
                queue.append(n)
            if n == end:
                return d_visited[n][0]

# Puzzle 1
min_len = float('inf')
min_path = None
for perm in gen_perm(list(wires.keys())):
    curr_len = 0
    for i in range(len(perm) - 1):
        curr_len += find_path(wires[perm[i]], wires[perm[i + 1]])
        if curr_len > min_len:
            break
    else:
        min_len = curr_len
        min_path = perm[:]

print(min_len)
print(min_path)

# Puzzle 2
min_len = float('inf')
min_path = None
for perm in gen_perm(sorted(list(wires.keys()))[1:]):
    curr_len = find_path(wires[0], wires[perm[0]]) + \
        find_path(wires[perm[-1]], wires[0])
    for i in range(len(perm) - 1):
        curr_len += find_path(wires[perm[i]], wires[perm[i + 1]])
        if curr_len > min_len:
            break
    else:
        min_len = curr_len
        min_path = perm[:]

print(min_len)
print([0] + min_path + [0])

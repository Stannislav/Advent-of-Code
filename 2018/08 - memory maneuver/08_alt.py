#!/usr/bin/env python3

from collections import defaultdict


with open('08_input.txt', 'r') as f:
    data = iter(int(c) for c in f.read().split())


def solve(n_children, n_meta):
    if n_children == 0:
        return [sum(next(data) for _ in range(n_meta))] * 2

    meta, value = 0, 0
    value_children = defaultdict(int)

    for n in range(n_children):
        m, value_children[n] = solve(next(data), next(data))
        meta += m
    for _ in range(n_meta):
        m = next(data)
        meta += m
        value += value_children[m - 1]

    return meta, value


meta, value = solve(next(data), next(data))
print("Part 1:", meta)
print("Part 2:", value)

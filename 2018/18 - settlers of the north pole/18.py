#!/usr/bin/env python3


import numpy as np
from collections import Counter


with open('18_input.txt', 'r') as f:
    lines = [line.strip() for line in f]


def get_map(lines):
    map = np.array([[c for c in line] for line in lines])
    return np.pad(map, 1, mode='constant')


def evolution_step(map):
    newmap = np.array(map)
    mi, mj = map.shape

    for i in range(1, mi - 1):
        for j in range(1, mj - 1):
            c = map[i, j]
            counts = Counter(map[i - 1: i + 2, j - 1 : j + 2].reshape(-1))

            if c == '.' and counts['|'] > 2:
                newmap[i, j] = '|'
            elif c == '|' and counts['#'] > 2:
                newmap[i, j] = '#'
            elif c == '#' and  not (counts['#'] - 1 and counts['|']):
                newmap[i, j] = '.'

    return newmap


def evolve(map, n):
    hist = dict()
    while n:
        key = tuple(map.reshape(-1))
        if key in hist:
            n = n % (hist[key] - n)
            hist = dict()
        hist[key] = n
        map = evolution_step(map)
        n -= 1

    return map


def score(map):
    return len(map[map == '#']) * len(map[map == '|'])


# Part 1
map = get_map(lines)
map = evolve(map, 10)
print('Part 1:', score(map))


# Part 2
map = get_map(lines)
map = evolve(map, 1000000000)
print('Part 2:', score(map))

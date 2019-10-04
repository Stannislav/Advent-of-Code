#!/usr/bin/env python3

from collections import deque

# Read input
with open('14_input.txt', 'r') as f:
    input = f.read().strip()


def gen_recipes():
    p1, p2 = 0, 1
    m = [3, 7]
    yield from m
    while True:
        tot = [int(c) for c in str(m[p1] + m[p2])]
        yield from tot
        m.extend(tot)
        p1 = (p1 + m[p1] + 1) % len(m)
        p2 = (p2 + m[p2] + 1) % len(m)


# Part 1
in1 = int(input)
recipe = gen_recipes()
for n in range(in1):
    next(recipe)
print("Part1: ", *[next(recipe) for n in range(10)], sep='')


# Part 2
in2 = list(map(int, input))
n = len(in2)
m = []
for r in gen_recipes():
    m.append(r)
    if m[-n:] == in2:
        break

print("Part2:", len(m) - n)

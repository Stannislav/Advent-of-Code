#!/usr/bin/env python3


# Read input
with open('05_input.txt', 'r') as f:
    input = f.read().strip()
input = [ord(c) for c in input]



# Define reaction of polymer
def react(poly):
    trig = (ord('a') - ord('A'), ord('A') - ord('a'))
    len_prev = -1

    while len(poly) != len_prev:
        for i, (p1, p2) in enumerate(zip(poly, poly[1:])):
            if p1 - p2 in trig:
                poly[i], poly[i+1] = 0, 0
        len_prev = len(poly)
        poly = [c for c in poly if c]

    return poly


# Part 1
print("Part 1:", len(react(input)))


# Part 2
min_len = float('inf')
for p in zip(range(ord('A'), ord('Z')+1), range(ord('a'), ord('z')+1)):
    res = react([c for c in input if c not in p])
    min_len = min(len(res), min_len)

print("Part 2:", min_len)

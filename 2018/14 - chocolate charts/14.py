#!/usr/bin/env python3


# Read input
with open('14_input.txt', 'r') as f:
    input = [c for c in f.read().strip()]


# Part 1
in1 = int(''.join(input))
p1, p2 = 0, 1
m = [3, 7]
while len(m) < in1 + 10:
    s1, s2 = m[p1], m[p2]
    tot = s1 + s2
    if tot >= 10:
        m.append(tot // 10)
    m.append(tot % 10)
    p1 = (p1 + s1 + 1) % len(m)
    p2 = (p2 + s2 + 1) % len(m)

part1 = ''.join(str(n) for n in m[in1:in1+10])
print("Part1:", part1)


# Part 2
in2 = [int(c) for c in input]
p1, p2 = 0, 1
m = [3, 7]
n = len(in2)
while True:
    s1, s2 = m[p1], m[p2]
    tot = s1 + s2
    if tot >= 10:
        m.append(tot // 10)
        if m[-n:] == in2:
            break
    m.append(tot % 10)
    if m[-n:] == in2:
        break
    p1 = (p1 + s1 + 1) % len(m)
    p2 = (p2 + s2 + 1) % len(m)

print("Part2:", len(m) - n)

#!/usr/bin/env python3


# Read input
with open('05_input.txt', 'r') as f:
    input = [c for c in f.read().strip()]


# Define reaction of polymer
alpha = 'abcdefghijklmnopqrstuvwxyz'
M = {}
for c in alpha:
    M[c] = c.upper()
    M[c.upper()] = c

def react(poly):
    stack = []
    for c in poly:
        if stack and c == M[stack[-1]]:
            stack.pop()
        else:
            stack.append(c)

    return stack


# Part 1
print("Part 1:", len(react(input)))


# Part 2
min_len = len(input)
for p in zip(alpha, alpha.upper()):
    res = react([c for c in input if c not in p])
    min_len = min(len(res), min_len)

print("Part 2:", min_len)

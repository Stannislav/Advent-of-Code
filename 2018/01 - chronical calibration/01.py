#!/usr/bin/env python3


# Read input
dfs = [int(line) for line in open('01_input.txt', 'r')]


# Part 1
print(f"Part 1: {sum(dfs)}")


# Part 2
f = 0
seen = {f}
found = False

while not found:
    for df in dfs:
        f += df
        if f in seen:
            print(f"Part 2: {f}")
            found = True
            break
        seen.add(f)

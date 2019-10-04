#!/usr/bin/env python3


# Read input
dfs = [int(line) for line in open('01_input.txt', 'r')]


# Part 1
print(f"Part 1: {sum(dfs)}")


# Part 2
# Credit:
# www.reddit.com/r/adventofcode/comments/a20646/2018_day_1_solutions/eauapmb/
from itertools import accumulate, cycle

seen = {0}

# Create a generator for all repeated frequencies
gen = (f for f in accumulate(cycle(dfs)) if f in seen or seen.add(f))

# Print first repeated frequency
print(f"Part 2: {next(gen)}")

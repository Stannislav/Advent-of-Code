#!/usr/bin/env python3


with open('08_input.txt', 'r') as f:
    input = [int(c) for c in f.read().strip().split()]

# input = [2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2]


def process(inp, pos = 0):
    n_children = inp[pos]
    n_meta = inp[pos + 1]
    pos += 2

    child_meta = 0
    child_vals = dict()
    for n in range(n_children):
        new_pos, meta, val = process(inp, pos)
        child_meta += meta
        pos = new_pos
        child_vals[n] = val

    meta = inp[pos:pos + n_meta]
    value = 0
    if n_children == 0:
        value = sum(meta)
    else:
        for ref in meta:
            if 0 < ref <= n_children:
                value += child_vals[ref - 1]

    return pos + n_meta, sum(meta) + child_meta, value


_, tot_meta, val = process(input)
print("Part 1:", tot_meta)
print("Part 2:", val)

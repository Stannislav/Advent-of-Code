#!/usr/bin/env python3

import numpy as np

dirs = {'^': [0, -1], 'v': [0, 1], '<': [-1, 0], '>': [1, 0]}


def solve(agents_cnt):
    field = np.zeros((500, 500), dtype=np.int)
    pos = [np.array((250, 250)) for i in range(agents_cnt)]
    field[tuple(pos[0])] = agents_cnt

    with open("../input/input_03.txt", 'r') as f:
        instr = f.read()
        i = 0
        for c in instr:
            pos[i] += dirs[c]
            field[tuple(pos[i])] += 1
            i = (i + 1) % agents_cnt
    print(np.count_nonzero(field))


solve(1)
solve(2)

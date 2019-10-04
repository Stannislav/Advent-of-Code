#!/usr/bin/env python3

disc_size = [17, 19, 7, 13, 5, 3]
disc_pos = [5, 8, 1, 7, 1, 0]

def solve():
    shift_pos = [(disc_pos[i]+i+1)%disc_size[i] for i in range(len(disc_pos))]
    t = 0

    while max(shift_pos) != 0:
        shift_pos = [(shift_pos[i]+1)%disc_size[i] for i in range(len(shift_pos))]
        t += 1
    print(t)

solve()
disc_size += [11]
disc_pos += [0]
solve()
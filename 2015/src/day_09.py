#!/usr/bin/env python3

d_places = {}
d_dist = {}
cntr = 0
with open("../input/input_09.txt", 'r') as f:
    for line in f:
        p1, x, p2, x, dist = line.split()
        if p1 not in d_places:
            d_places[p1] = cntr
            cntr += 1
        if p2 not in d_places:
            d_places[p2] = cntr
            cntr += 1
        d_dist[(d_places[p1], d_places[p2])] = d_dist[(d_places[p2], d_places[p1])] = int(dist)


def travel(start, been, comp):
    if len(been) == cntr:
        return 0
    return comp([d_dist[(start, to)] + travel(to, been + [to], comp) for to in range(cntr) if to not in been])


print(min([travel(i, [i], min) for i in range(cntr)]))
print(max([travel(i, [i], max) for i in range(cntr)]))

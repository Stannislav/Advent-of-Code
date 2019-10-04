#!/usr/bin/env python3

import itertools

with open("../input/input_13.txt", 'r') as f:
    d_data = {}
    for line in f:
        name, _, what, x, _, _, _, _, _, _, who = line.rstrip()[:-1].split()
        if name not in d_data:
            d_data[name] = {}
        d_data[name][who] = int(x) if what == "gain" else -int(x)


    def max_happy():
        max_happiness = float("-inf")
        n = len(d_data)
        for p in itertools.permutations(d_data.keys()):
            happiness = 0
            for i in range(n):
                happiness += d_data[p[i]][p[(i + 1) % n]] + d_data[p[(i + 1) % n]][p[i]]
            if happiness > max_happiness:
                max_happiness = happiness
        return max_happiness


    m1 = max_happy()
    print(m1)
    for guest in d_data.keys():
        d_data[guest]["me"] = 0
    d_data["me"] = dict([(guest, 0) for guest in d_data.keys()])
    m2 = max_happy()
    print(m2)

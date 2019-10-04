#!/usr/bin/env python3

inp = 36000000

n = 1
d_elves = {}
sol1 = sol2 = 0
while True:
    factors = [[i, n // i] for i in range(1, int(n ** 0.5) + 1) if n % i == 0]
    flattened = set([val for pair in factors for val in pair])
    for f in flattened:
        if f not in d_elves:
            d_elves[f] = 0
        d_elves[f] += 1
    cnt1 = sum(flattened)
    cnt2 = sum(filter(lambda x: d_elves[x] <= 50, flattened))
    if cnt1 * 10 >= inp and sol1 == 0:
        sol1 = n
        if sol2 != 0:
            break
    if cnt2 * 11 >= inp and sol2 == 0:
        sol2 = n
        if sol1 != 0:
            break
    n += 1

print(sol1)
print(sol2)

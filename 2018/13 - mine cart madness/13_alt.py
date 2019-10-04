#!/usr/bin/env python3


from itertools import cycle


# Read input
with open('13_input.txt', 'r') as f:
    lines = [[c for c in line] for line in f]

'''
Complex coordinates: x = Im, y = Re
+-----> Im
|
|
v Re
'''

# Parse the map, and the cart locations
m, carts = dict(), dict()
dir_d = {'<': -1j, '>': 1j, '^': -1, 'v': 1}
for re, row in enumerate(lines):
    for im, c in enumerate(row):
        pos = re + 1j * im
        if c in '<>^v':
            carts[pos] = dir_d[c], cycle([1j, 1, -1j])
        elif c in '\\/+':
            m[pos] = c


# Run simulation
part1 = None
while len(carts) > 1:
    for pos in sorted(carts.keys(), key=lambda p: (p.real, p.imag)):
        if pos not in carts:  # cart has already crashed
            continue

        dir, turn = carts.pop(pos)
        c = m.get(pos)

        if c == '+':
            dir *= next(turn)
        elif c:  # \ or /
            dir = (-1 if c == '/' else 1) * (dir.imag + 1j * dir.real)

        pos += dir
        
        if pos in carts:  # crash
            carts.pop(pos)
            if not part1:
                part1 = f"{int(pos.imag)},{int(pos.real)}"
        else:
            carts[pos] = dir, turn


print("Part 1:", part1)
pos = list(carts)[0]
print(f"Part 2: {int(pos.imag)},{int(pos.real)}")

#!/usr/bin/env python3


# Read input
with open('13_input.txt', 'r') as f:
    track = [[c for c in line] for line in f]


# Parse cart positions
carts = dict()
for i, row in enumerate(track):
    for j, c in enumerate(row):
        if c == '<':
            carts[(i, j)] = (0, -1, -1)
            track[i][j] = '-'
        elif c == '>':
            carts[(i, j)] = (0, 1, -1)
            track[i][j] = '-'
        elif c == '^':
            carts[(i, j)] = (-1, 0, -1)
            track[i][j] = '|'
        elif c == 'v':
            carts[(i, j)] = (1, 0, -1)
            track[i][j] = '|'


# Run simulation
part1 = None
while len(carts) > 1:
    for i, j in sorted(carts.keys()):
        if (i, j) not in carts:  # so it has already crashed
            continue

        dir_i, dir_j, turn = carts.pop((i, j))
        c = track[i][j]

        if c == '+':
            if turn > 0:
                c = '/' if dir_i else '\\'
            if turn < 0:
                c = '/' if dir_j else '\\'
            turn = (turn + 2) % 3 - 1

        if c == '/':
            dir_i, dir_j = -dir_j, -dir_i
        elif c == '\\':
            dir_i, dir_j = dir_j, dir_i

        i += dir_i
        j += dir_j
        if (i, j) in carts:  # crash
            carts.pop((i, j))
            if not part1:
                part1 = f"{j},{i}"
        else:
            carts[(i, j)] = (dir_i, dir_j, turn)


print("Part 1:", part1)
i, j = list(carts)[0]
print(f"Part 2: {j},{i}")

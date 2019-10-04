#!/usr/bin/env python3

config = []

with open("../input/input_18.txt", 'r') as f:
    for line in f:
        config.append([0] + [0 if c == '.' else 1 for c in line.rstrip()] + [0])

ly = len(config) + 2
lx = len(config[0])
config.append([0] * lx)
config = [config[-1]] + config


def evolve(conf, extra=False):
    c = [list(row) for row in conf]

    for y in range(1, ly - 1):
        for x in range(1, lx - 1):
            cnt = sum(conf[y - 1][x - 1:x + 2]) + sum(conf[y + 1][x - 1:x + 2]) + conf[y][x - 1] + conf[y][x + 1]
            if conf[y][x] == 1 and cnt not in (2, 3):
                c[y][x] = 0
            elif conf[y][x] == 0 and cnt == 3:
                c[y][x] = 1
    if extra:
        c[1][1] = c[1][-2] = c[-2][1] = c[-2][-2] = 1

    return c


c_copy = [list(row) for row in config]
c_copy[1][1] = c_copy[1][-2] = c_copy[-2][1] = c_copy[-2][-2] = 1

for i in range(100):
    config = evolve(config)
print(sum([sum(row) for row in config]))

for i in range(100):
    c_copy = evolve(c_copy, True)
print(sum([sum(row) for row in c_copy]))

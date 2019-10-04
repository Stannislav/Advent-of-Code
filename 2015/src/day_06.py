#!/usr/bin/env python3

field = [[0 for i in range(1000)] for j in range(1000)]

with open("../input/input_06.txt") as f:
    for line in f:
        l = line.rstrip().split()
        if l[0] == "toggle":
            sx, sy = map(int, l[1].split(','))
            ex, ey = map(int, l[3].split(','))
            for y in range(sy, ey + 1):
                for x in range(sx, ex + 1):
                    field[y][x] = 1 - field[y][x]
        else:
            sx, sy = map(int, l[2].split(','))
            ex, ey = map(int, l[4].split(','))
            for y in range(sy, ey + 1):
                for x in range(sx, ex + 1):
                    field[y][x] = 1 if l[1] == "on" else 0

cnt = 0
for i in range(1000):
    cnt += field[i].count(1)
print(cnt)

field = [[0 for i in range(1000)] for j in range(1000)]
with open("../input/input_06.txt") as f:
    for line in f:
        l = line.rstrip().split()
        if l[0] == "toggle":
            sx, sy = map(int, l[1].split(','))
            ex, ey = map(int, l[3].split(','))
            for y in range(sy, ey + 1):
                for x in range(sx, ex + 1):
                    field[y][x] += 2
        else:
            sx, sy = map(int, l[2].split(','))
            ex, ey = map(int, l[4].split(','))
            for y in range(sy, ey + 1):
                for x in range(sx, ex + 1):
                    if l[1] == "on":
                        field[y][x] += 1
                    else:
                        field[y][x] -= 1
                        if field[y][x] < 0:
                            field[y][x] = 0

cnt = 0
for i in range(1000):
    cnt += sum(field[i])
print(cnt)

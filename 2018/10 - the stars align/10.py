#/usr/bin/env python3

import re


with open('10_input.txt', 'r') as f:
    input = [[int(n) for n in re.findall('-?\d+', line)] for line in f]

dy_min = float('inf')
n = 0
while True:
    x_all, y_all, _, _ = zip(*input)
    x_min, x_max, y_min, y_max = min(x_all), max(x_all), min(y_all), max(y_all)
    dy = y_max - y_min
    if dy < dy_min:
        n += 1
        dy_min = dy
        input = [(x + vx, y + vy, vx, vy) for x, y, vx, vy in input]
    else:
        n -= 1
        break


print("Part 1:")
pos = [(px - vx, py - vy) for px, py, vx, vy in input]
x_all, y_all = zip(*pos)
x_min, x_max, y_min, y_max = min(x_all), max(x_all), min(y_all), max(y_all)
for y in range(y_min, y_max + 1):
    line = ['#'  if (x, y) in pos else '.' for x in range(x_min, x_max + 1)]
    print(''.join(line))

print("Part 2:", n)

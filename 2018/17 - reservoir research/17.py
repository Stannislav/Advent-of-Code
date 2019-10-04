#!/usr/bin/env python3


import re
import numpy as np


# Read input
with open('17_input.txt', 'r') as f:
    lines = [line for line in f]


# Transform lines from input into data
def setup(lines):
    v = []  # vertical segments: (x, y1, y2)
    h = []  # horizontal segments: (y, x1, x2)

    for line in lines:
        numbers = list(map(int, re.findall('\d+', line)))
        if line[0] == 'x':
            v.append(numbers)
        else:
            h.append(numbers)

    h = np.array(h)
    v = np.array(v)

    min_x = min(h[:, 1].min(), v[:, 0].min()) - 1  # leave space left and right for the water
    max_x = max(h[:, 2].max(), v[:, 0].max()) + 1
    min_y = min(v[:, 1].min(), h[:, 0].min()) - 1  # leave space for the spring
    max_y = max(v[:, 2].max(), h[:, 0].max())

    # normalize the coordinates
    v -= (min_x, min_y, min_y)
    h -= (min_y, min_x, min_x)

    spring = (max(0, 0 - min_y), 500 - min_x)  #if spring out of bounds, then place it at the top

    ground = np.array([['.' for x in range(min_x, max_x + 1)] for y in range(min_y, max_y + 1)])

    for x, y1, y2 in v:
        ground[y1:y2+1, x] = '#'

    for y, x1, x2 in h:
        ground[y, x1:x2+1] = '#'

    ground[spring] = '+'

    return ground, spring


# Visualise the current configuration
colours = {
    '~': '\x1b[0;30;46m', '|': '\x1b[0;30;46m', '+': '\x1b[0;30;41m', '#': '\x1b[0;37;47m'
}

def viz(ground, min_y=None, max_y=None):
    for n, row in enumerate(ground[min_y:max_y]):
        print(f"{n:4d} ", end='')  # row number
        for c in row:
            if c in colours:
                print(colours[c] + c + '\x1b[0m', end='')
            else:
                print(c, end='')
        print()
    print()


# The actual algorithm
def fill(ground, spring):
    '''
    Each recursive call of fill does two things:
    1. Flow down until hit something or out of bounds
    2. If hit clay then fill horizontally.
       If at some point an overflow is detected then recurse from that location

    If after filling horizontally we end up at the same y coordinate where we started,
    then the water had no free fall. This means that the previous recursion can fill
    water on top of it.

    Return: rest = did we fill up completely and avoided free fall?
    '''
    my, mx = ground.shape  # max y, max x
    sy, sx = spring  # start y, start x
    rest = True  # whether the layer below is at rest so that we can fill up on top of it

    # First flow down while there is space unter current position
    y = sy + 1
    while y < my and ground[y, sx] == '.':
        y += 1
    if y == my or ground[y, sx] == '|':
        rest = False  # out of bounds or found an already filled area - so nothing to fill here
    y -= 1  # back to an empty square


    # Fill horizontally starting from the bottom and going up
    while y + 1 > sy and rest:
        # Explore to the left
        lx = sx
        while lx > 0 and ground[y, lx - 1] == '.':
            lx -= 1
            if y < my and ground[y + 1, lx] == '.':  # can overflow
                rest = fill(ground, (y, lx))
                break  # overflown - stop exploring to the left

        # Explore to the right
        rx = sx
        while rx < mx - 1 and ground[y, rx + 1] == '.':
            rx += 1
            if y < my and ground [y + 1, rx] == '.':  # can overflow
                rest = fill(ground, (y, rx))
                break  # overflown - stop exploring to the right

        ground[y, lx : rx + 1] = '~' if rest else '|'
        y -= 1

    if sy - y != 1:
        rest = False
        ground[sy + 1 : y + 1, sx] = '|'

    return rest


# Test case
# lines = [
# "x=495, y=2..7",
# "y=7, x=495..501",
# "x=501, y=3..7",
# "x=498, y=2..4",
# "x=506, y=1..2",
# "x=498, y=10..13",
# "x=504, y=10..13",
# "y=13, x=498..504",
# ]


# Main
ground, spring = setup(lines)
fill(ground, spring)
viz(ground, 0, 50)
print('Part 1:', len(ground[np.isin(ground, ['~', '|'])]))
print('Part 2:', len(ground[ground == '~']))

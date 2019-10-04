#!/usr/bin/env python3


from heapq import heappush, heappop


with open('22_input.txt', 'r') as f:
    depth = int(next(f).split()[-1])
    target = complex(*map(int, next(f).split()[-1].split(',')))


ROCKY, WET, NARROW = 0, 1, 2
NEITHER, TORCH, CLIMB = 0, 1, 2
m_erosion = dict()


def erosion(pos):
    if pos in m_erosion:
        return m_erosion[pos]

    if pos == 0 or pos == target:
        geo = 0
    elif pos.imag == 0:
        geo = pos.real * 16807
    elif pos.real == 0:
        geo = pos.imag * 48271
    else:
        geo = erosion(pos - 1) * erosion(pos - 1j)

    ret = (geo + depth) % 20183
    m_erosion[pos] = ret

    return ret


# Part 1
risk = 0
for y in range(int(target.imag) + 1):
    for x in range(int(target.real) + 1):
        risk += erosion(x + 1j * y) % 3
print('Part 1:', int(risk))


# Part 2
# Do a BFS search with a priority queue.
heap = [(0, 0, 0, TORCH)]  # (time, x, y, tool), heap sorted by time, so time has to be first
visited = dict()  # (pos, tool): time

while heap:
    # It's annoying we cannot use the heap with complex numbers because they cannot be ordered...
    time, x, y, tool = heappop(heap)
    pos = x + 1j * y

    if (pos, tool) == (target, TORCH):
        break

    if (pos, tool) in visited and visited[(pos, tool)] <= time:  # there is a faster way
        continue
    visited[(pos, tool)] = time

    # Change tool
    heappush(heap, (time + 7, x, y, 3 - tool - erosion(pos) % 3))

    # Go to the next square
    time += 1
    for newpos in [pos - 1, pos + 1, pos - 1j, pos + 1j]:
        # Check out of bounds and if tool is valid
        if newpos.real < 0 or newpos.imag < 0 or erosion(newpos) % 3 == tool:
            continue
        heappush(heap, (time, newpos.real, newpos.imag, tool))


print('Part 2:', time)

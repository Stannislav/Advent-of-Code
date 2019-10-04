#!/usr/bin/env python3

inp = 3005290

# Strategy: start with first elf and go only so far
# that you don't overshoot. Then remove elves without
# a present and shift the list such that the next elf
# is the first in the queue

# Puzzle 1
conf = [i+1 for i in range(inp)]

while len(conf) > 1:
    l = len(conf)
    for i in range(l//2):
        conf[2*i+1] = 0
    if l%2: # overshot by one
        conf[0] = 0
    conf = [c for c in conf if c != 0]

print(conf[0])

# NB: see this Numberphile video for details:
# https://youtu.be/uCsD3ZGzMgE
# according to it the solutions is
# print(2*(inp - (1<<(inp.bit_length()-1))) + 1)

# Puzzle 2
conf = [i+1 for i in range(inp)]

# i = position of elf under consideration, l = length of queue
# if i = 0, steal from elf l/2. at any i we would have to
# steal from i + l/2. However, because now i elves are without
# a present, we have to replace l/2 by (l+i)/2 and we have to go from
# i to i + (i+l)/2 = (3*i+l)/2
# how far can we go to not overshoot?
# (3*i_max+l)/2 = l --> i_max = l/3
while len(conf) > 2:
    l = len(conf)
    for i in range(l//3):
        conf[(3*i+l)//2] = 0
    z = conf.count(0)
    conf = [c for c in conf if c != 0]
    conf = conf[z:] + conf[:z]

print(conf[0])

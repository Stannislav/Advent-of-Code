#!/usr/bin/env python3

find = [17, 61]

# each bot gets one single instruction
class Bot():
    def __init__(self, name):
        self.name = name
        self.values = []
        self.towho = []

    def put_value(self, val):
        self.values.append(val)
        # Am I ready?
        if len(self.values) == 2:
            self.values.sort()
            queue.append(self.name)
        # Puzzle 1
        if set(self.values) == set(find):
            print("Bot {} compares {} and {}.".format(self.name[1], find[0], find[1]))

    def execute(self):
        bl,bh = self.towho
        vl,vh = self.values
        if bl[0] == 'output':
            d_output[int(bl[1])] = vl
        else:
            d_bots[bl].put_value(vl)
        if bh[0] == 'output':
            d_output[int(bh[1])] = vh
        else:
            d_bots[bh].put_value(vh)

# Once a bot has both values it is put in the queue
queue = []
d_bots = {}
d_output = {}

with open("input.txt", 'r') as f:
    for line in f:
        if line[0] == 'b':
# bot n gives low to bot m and high to bot k
            bt, bn, _, _, _, blt, bln, _, _, _, bht, bhn = line.split()
            if (bt,bn) not in d_bots:
                d_bots[(bt,bn)] = Bot((bt,bn))
            d_bots[(bt,bn)].towho = ((blt,bln),(bht,bhn))
        else:
# value x goes to bot n
            _, val, _, _, bt, bn = line.split()
            if (bt,bn) not in d_bots:
                d_bots[(bt,bn)] = Bot((bt,bn))
            d_bots[(bt,bn)].put_value(int(val))

while len(queue) > 0:
    d_bots[queue.pop(0)].execute()
# Puzzle 2
print(d_output[0]*d_output[1]*d_output[2])

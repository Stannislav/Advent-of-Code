#!/usr/bin/env python3

inst = [line.rstrip().split() for line in open("input.txt", 'r') if len(line) > 0]

def run(abcd):
    reg = dict(zip('abcd',abcd))
    i = 0
    while i < len(inst):
        cmd, val = inst[i][0], reg[inst[i][1]] if inst[i][1] in reg else int(inst[i][1])
        if cmd == "cpy":
            reg[inst[i][2]] = val
        elif cmd == "inc":
            reg[inst[i][1]] += 1
        elif cmd == "dec":
            reg[inst[i][1]] -= 1
        elif cmd == "jnz" and val != 0:
            i += int(inst[i][2])
            continue
        i += 1
    return reg['a']

print(run([0,0,0,0]))
print(run([0,0,1,0]))
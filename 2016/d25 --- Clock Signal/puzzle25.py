#!/usr/bin/env python3

inst = [line.rstrip().split() for line in open("input.txt", 'r') if len(line) > 0]

def run(abcd):
    reg = dict(zip('abcd',abcd))
    i = 0
    last_out = None
    while i < len(inst):
        cmd, val = inst[i][0], reg[inst[i][1]] if inst[i][1] in reg else int(inst[i][1])
        if cmd == "cpy":
            reg[inst[i][2]] = val
        elif cmd == "inc":
            reg[inst[i][1]] += 1
        elif cmd == "dec":
            reg[inst[i][1]] -= 1
        elif cmd == "out":
            if last_out != None:
                if last_out not in [0,1] or last_out == val:
                    return False
                else:
                    last_out = val
            else:
                last_out = val
        elif cmd == "jnz" and val != 0:
            i += int(inst[i][2])
            continue
        i += 1
    return True

i = 0
while not run([i,0,0,0]):
    print("Not", i)
    i += 1
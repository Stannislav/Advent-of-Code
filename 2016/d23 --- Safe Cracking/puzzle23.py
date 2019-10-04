#!/usr/bin/env python3

d_tgl = dict(zip(
    ['inc', 'dec', 'tgl', 'jnz', 'cpy'],
    ['dec', 'inc', 'inc', 'cpy', 'jnz']))


def run(abcd):
    reg = dict(zip('abcd', abcd))
    i = 0
    while i < len(inst):
        cmd, val = inst[i][0], reg[inst[i][1]] if inst[
            i][1] in reg else int(inst[i][1])
        if cmd == "cpy" and inst[i][2] in reg:
            reg[inst[i][2]] = val
        elif cmd == "inc" and inst[i][1] in reg:
            reg[inst[i][1]] += 1
        elif cmd == "dec" and inst[i][1] in reg:
            reg[inst[i][1]] -= 1
        elif cmd == "tgl":
            j = i + val
            if 0 <= j < len(inst):
                inst[j][0] = d_tgl[inst[j][0]]
        elif cmd == "jnz" and val != 0:
            val2 = reg[inst[i][2]] if inst[i][2] in reg else int(inst[i][2])
            i += val2
            continue
        i += 1
    return reg['a']


inst = [line.strip().split()
        for line in open("input.txt", 'r') if len(line) > 0]
print(run([7, 0, 0, 0]))
inst = [line.strip().split()
        for line in open("input.txt", 'r') if len(line) > 0]
print(run([12, 0, 0, 0]))

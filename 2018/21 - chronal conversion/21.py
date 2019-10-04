#!/usr/bin/env python3


# Read input
with open('21_input.txt', 'r') as f:
    lines = [line.strip() for line in f]


# Parse input
def get_prog(lines):
    ipreg = int(lines[0][-1])
    lines = [line.split() for line in lines[1:]]
    prog = [(i, int(a), int(b), int(c)) for i, a, b, c in lines]

    return ipreg, prog


# Run the input code directly
def run(prog, ipreg, r0=0):
    reg = [0] * 6
    reg[0] = r0
    ip = 0

    commands = {
        'addr': lambda a, b: reg[a] + reg[b],
        'addi': lambda a, b: reg[a] + b,
        'mulr': lambda a, b: reg[a] * reg[b],
        'muli': lambda a, b: reg[a] * b,
        'setr': lambda a, b: reg[a],
        'seti': lambda a, b: a,
        'gtrr': lambda a, b: reg[a] > reg[b],
        'gtir': lambda a, b: a > reg[b],
        'gtri': lambda a, b: reg[a] > b,
        'eqrr': lambda a, b: reg[a] == reg[b],
        'eqir': lambda a, b: a == reg[b],
        'eqri': lambda a, b: reg[a] == b,
        'banr': lambda a, b: reg[a] & b,
        'bani': lambda a, b: reg[a] & b,
        'borr': lambda a, b: reg[a] | reg[b],
        'bori': lambda a, b: reg[a] | b,
    }

    r5hist = []
    while ip < len(prog):
        if ip == 28:
            if reg[5] in r5hist:
                break
            r5hist.append(reg[5])

        cmd, A, B, C = prog[ip]
        reg[ipreg] = ip
        reg[C] = commands[cmd](A, B)

        ip = reg[ipreg] + 1

    return r5hist


# Implementation of the disassembled code
def run_fast(seed1, seed2, r0=0):
    r5 = 0
    r5hist = []

    while True:
        r3 = r5 | (1<<16)
        r5 = seed1
        while True:
            r5 += r3 & 0xFF
            r5 &= 0xFFFFFF
            r5 *= seed2
            r5 &= 0xFFFFFF
            if r3 < (1<<8):
                break
            r3 >>= 8

        if r5 in r5hist:  # we're back to the first r5, so from here we'd be looping
            break
        r5hist.append(r5)

        if r5 == r0:
            break

    return r5hist


# Solution
ipreg, prog = get_prog(lines)

hist = run_fast(seed1=prog[7][1], seed2=prog[11][2])
print('Part 1:', hist[0])
print('Part 2:', hist[-1])


# Check that our disassembled code produces the same as running the code directly
# This should be true for any 0 <= n < len(hist)
n = 3
assert len(run(prog, ipreg, r0=hist[n])) == n + 1

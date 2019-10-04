#!/usr/bin/env python3


# Read input
with open('19_input.txt', 'r') as f:
    lines = [line.strip() for line in f]


# Parse input
def get_prog(lines):
    ipreg = int(lines[0][-1])
    lines = [line.split() for line in lines[1:]]
    prog = [(i, int(a), int(b), int(c)) for i, a, b, c in lines]

    return ipreg, prog


# The the input code directly
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
        'eqrr': lambda a, b: reg[a] == reg[b],
    }

    while ip < len(prog):
        cmd, A, B, C = prog[ip]
        reg[ipreg] = ip
        reg[C] = commands[cmd](A, B)
        ip = reg[ipreg] + 1

    return reg[0]


#  Run the algorithm extracted from code
def run_fast(r0=0):
    ''' Computes the sum of all divisors of r5 '''
    r5 = 981 + r0 * 10550400

    sqrt = int(r5 ** 0.5)
    result = sqrt if r5 % sqrt == 0 else 0
    for n in range(1, sqrt):
        if r5 % n == 0:
            result += n + r5 // n

    return result


# Check if our decoding is correct
print('Performing consistency check... ', end='', flush=True)
ipreg, prog = get_prog(lines)
if run(prog, ipreg) == run_fast():
    print('Passed')
else:
    print('Failed!')
    quit()


# Solution
print('Part 1:', run_fast(r0=0))
print('Part 2:', run_fast(r0=1))

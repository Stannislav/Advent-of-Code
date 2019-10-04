#!/usr/bin/env python3

import re


# Read input
with open('16_input.txt', 'r') as f:
    examples = []
    while f:
        before = next(f).strip()
        x = next(f).strip()
        if not x:
            break
        after = next(f).strip()

        examples.append([
            [int(c) for c in re.findall('\d+', before)],
            [int(c) for c in re.findall('\d+', x)],
            [int(c) for c in re.findall('\d+', after)],
        ])
        next(f)

    prog = [[int(c) for c in line.strip().split()] for line in f]


# Define commands
def addr(A, B, reg):
    return reg[A] + reg[B]

def addi(A, B, reg):
    return reg[A] + B

def mulr(A, B, reg):
    return reg[A] * reg[B]

def muli(A, B, reg):
    return reg[A] * B

def banr(A, B, reg):
    return reg[A] & reg[B]

def bani(A, B, reg):
    return reg[A] & B

def borr(A, B, reg):
    return reg[A] | reg[B]

def bori(A, B, reg):
    return reg[A] | B

def setr(A, B, reg):
    return reg[A]

def seti(A, B, reg):
    return A

def gtir(A, B, reg):
    return int(A > reg[B])

def gtri(A, B, reg):
    return int(reg[A] > B)

def gtrr(A, B, reg):
    return int(reg[A] > reg[B])

def eqir(A, B, reg):
    return int(A == reg[B])

def eqri(A, B, reg):
    return int(reg[A] == B)

def eqrr(A, B, reg):
    return int(reg[A] == reg[B])

commands = [addr, addi, mulr, muli, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir, eqri, eqrr]


# Find all commands that fit a given example. Exclude commands with index in exclude (for part 2)
def find_fit(before, instruction, after, exclude=[]):
    cmd, A, B, C = instruction
    reg_orig = tuple(before)
    fit = []
    for n, c in enumerate(commands):
        if n in exclude:
            continue
        reg = [i for i in reg_orig]
        reg[C] = c(A, B, reg)
        if reg == after:
            fit.append(n)

    return fit


# Derive the map for the commands from the examples
def find_commands(examples):
    m = dict()  # map from cmd in example to index in 'commands'
    while len(m) < len(commands):
        for before, instruction, after in examples:
            cmd, A, B, C = instruction
            if instruction[0] in m:  # cmd has already been matched
                continue
            fit = find_fit(before, instruction, after, exclude=m.values())
            if len(fit) == 1:
                m[cmd] = fit[0]

    return {k: commands[v] for k, v in m.items()}


# Execute the programme given the command map
def exec(prog, cmd_map):
    reg = [0] * 4
    for cmd, A, B, C in prog:
        reg[C] = cmd_map[cmd](A, B, reg)

    return reg


# Results
print('Part 1:', sum(1 for ex in examples if len(find_fit(*ex)) >= 3))
cmd_map = find_commands(examples)
print('Part 2:', exec(prog, cmd_map)[0])

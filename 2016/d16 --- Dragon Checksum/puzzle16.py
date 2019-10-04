#!/usr/bin/env python3

inp = "01111010110010011"

def solve(target_len):
    # translate input into numbers
    state = [int(c) for c in inp]

    # generate data
    while len(state) < target_len:
        state += [0] + [1-i for i in state[::-1]]

    # compute checksujm
    state = state[:target_len]
    while len(state)%2 == 0:
        for i in range(int(len(state)/2)):
            state[i] = 1 if state[2*i] == state[2*i+1] else 0
        state = state[:int(len(state)/2)]

    print(''.join(map(str, state)))

solve(272)
solve(35651584)
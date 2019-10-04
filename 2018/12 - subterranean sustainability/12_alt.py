#!/usr/bin/env python3


with open('12_input.txt', 'r') as f:
    init = set(i for i, c in enumerate(next(f).split()[-1]) if c == '#')
    next(f)
    patt = set()  # save only the indices of '#' rather than the whole pattern
    for p in f:
        x, _, y = p.strip().split()
        if y == '#':
            patt.add(tuple(i - 2 for i, c in enumerate(x) if c == '#'))


def evolve(state):
    '''
    For each index in state check if any pattern can be matched
    in the window +-2 indices away from it, and if yes then add
    the corresponding entry in the evolved state
    '''
    newstate = set()
    for idx in state:
        for shift in [-2, -1, 0, 1, 2]:
            if tuple(i for i in [-2, -1, 0, 1, 2] if idx + i + shift in state) in patt:
                newstate.add(idx + shift)
    return newstate


def solve(gen):
    '''
    Evelve until a repitition in the pattern is seen. From then one
    can calculate how many repititons fit into the remaining generations
    and skip them. One needs to keep track of the shift of the pattern
    to correctly calculate the final score.
    '''
    seen = dict()
    state = init
    while gen:
        gen -= 1
        state = evolve(state)
        state_pattern = tuple(i - min(state) for i in state)
        if state_pattern in seen:
            prev_min, prev_gen = seen[state_pattern]
            dg = prev_gen - gen
            idx_shift =  gen // dg * (min(state) - prev_min)
            gen = gen % dg
            hist = dict()
        else:
            seen[state_pattern] = min(state), gen

    return sum(i + idx_shift for i in state)


print("Part 1:", solve(200))
print("Part 2:", solve(50000000000))

#!/usr/bin/env python3

containers = []
with open("../input/input_17.txt", 'r') as f:
    for line in f:
        containers.append(int((line.rstrip())))


def gen_combo(cont, liters, seq=[]):
    if len(cont) == 0:
        yield from []
    else:
        if cont[0] == liters:
            yield seq + [cont[0]]
        elif cont[0] < liters:
            yield from gen_combo(cont[1:], liters - cont[0], seq + [cont[0]])
        yield from gen_combo(cont[1:], liters, seq)


total = min_cnt = 0
len_min = float('inf')
for combo in gen_combo(containers, 150):
    total += 1
    if len(combo) < len_min:
        len_min = len(combo)
        min_cnt = 1
    elif len(combo) == len_min:
        min_cnt += 1

print(total)
print(min_cnt)

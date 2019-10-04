#!/usr/bin/env python3

gift = {
    "children": 3,
    "cats": 7,
    "samoyeds": 2,
    "pomeranians": 3,
    "akitas": 0,
    "vizslas": 0,
    "goldfish": 5,
    "trees": 3,
    "cars": 2,
    "perfumes": 1,
}

aunts = []
with open("../input/input_16.txt", 'r') as f:
    for line in f:
        items = ''.join([c for c in line if c not in ":,\n"]).split()[2:]
        d = {}
        for i in range(int(len(items) / 2)):
            d[items[2 * i]] = int(items[2 * i + 1])
        aunts.append(d)

sol1 = sol2 = 0
for i, aunt in enumerate(aunts):
    for key in aunt.keys():
        if aunt[key] != gift[key]:
            break
    else:
        sol1 = i + 1

    for key in aunt.keys():
        if key in ["cats", "trees"]:
            if aunt[key] <= gift[key]:
                break
        elif key in ["pomeranians", "goldfish"]:
            if aunt[key] >= gift[key]:
                break
        elif aunt[key] != gift[key]:
            break
    else:
        sol2 = i + 1

print(sol1)
print(sol2)

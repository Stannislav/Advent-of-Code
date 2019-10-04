#!/usr/bin/env python3

with open("../input/input_08.txt") as f:
    # lines = f.read()
    total = 0
    for line in f:
        i = 1
        total += 2
        while i < len(line) - 2:
            if line[i] == "\\":
                if line[i + 1] == "\"" or line[i + 1] == "\\":
                    total += 1
                    i += 1
                elif line[i + 1] == "x":  # line[i+1] = x
                    total += 3
                    i += 3
            i += 1
    print(total)

with open("../input/input_08.txt") as f:
    total = 0
    for line in f:
        total += 2  # from surrounding quotes
        for c in line.rstrip():
            if c in "\\\"":
                total += 1
    print(total)

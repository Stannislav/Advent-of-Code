#!/usr/bin/env python3


with open("../input/input_01.txt", 'r') as f:
    text = f.read()
    print(text.count('(') - text.count(')'))

    pos = 0
    floor = 0
    for c in text.replace('\n', ''):
        pos += 1
        if c == '(':
            floor += 1
        else:
            floor -= 1
        if floor < 0:
            print(pos)
            break

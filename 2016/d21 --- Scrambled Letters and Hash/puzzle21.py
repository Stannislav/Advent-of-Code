#!/usr/bin/env python3

scrumble_instructions = [line.strip().split() for line in open("input.txt", 'r')]

def scramble(inp):
    inp = list(inp)
    for words in scrumble_instructions:
        if words[0][0] == 'm': # move position
            inp.insert(int(words[5]), inp.pop(int(words[2])))
        elif words[0][0] == 's':
            if words[1][0] == 'p': # swap position
                p1, p2 = int(words[2]), int(words[5])
            elif words[1][0] == 'l': # swapp letter
                p1, p2 = inp.index(words[2]), inp.index(words[5])
            inp[p1], inp[p2] = inp[p2], inp[p1]
        elif words[0][0] == 'r':
            if words[1][0] == 'p': # reverse position
                p1, p2 = int(words[2]), int(words[4])
                inp[p1:p2+1] = inp[p1:p2+1][::-1]
            elif words[1][0] == 'b': # rotate based on position
                x = inp.index(words[6])
                x = (x + (x >= 4) + 1) % len(inp)
                inp = inp[-x:] + inp[:-x]
            elif words[1][0] == 'r': # rotate right
                x = int(words[2])
                inp = inp[-x:] + inp[:-x]
            elif words[1][0] == 'l': # rotate left
                x = int(words[2])
                inp = inp[x:] + inp[:x]
    return ''.join(inp)

def gen_perm(s):
    if len(s) == 0:
        yield ""
    for i in range(len(s)):
        for subperm in gen_perm(s[:i] + s[i+1:]):
            yield s[i] + subperm

inp1 = "abcdefgh"
inp2 = "fbgdceah"

print(scramble(inp1))
for perm in gen_perm(inp2):
    if scramble(perm) == inp2:
        print(perm)
        break
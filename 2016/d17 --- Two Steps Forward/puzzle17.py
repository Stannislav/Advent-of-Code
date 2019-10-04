#!/usr/bin/env python3

import hashlib

passcode = "awrkjxxr"

def gen_open(el):
    path, posx, posy = el
    key = hashlib.md5(str(passcode+path).encode()).hexdigest()
    if posy > 0 and key[0] in 'bcdef': yield (path + 'U', posx, posy-1)
    if posy < 3 and key[1] in 'bcdef': yield (path + 'D', posx, posy+1)
    if posx > 0 and key[2] in 'bcdef': yield (path + 'L', posx-1, posy)
    if posx < 3 and key[3] in 'bcdef': yield (path + 'R', posx+1, posy)

start = ("", 0, 0) # (path, posx, posy)
queue = [start]
end = (3, 3)
sol = None

# do BFS
while len(queue) > 0:
    for new_el in gen_open(queue.pop(0)):
        if new_el[1:] == end:
            if sol == None:
                print(new_el[0])
            sol = new_el[0]
            continue
        queue.append(new_el)

print(len(sol))
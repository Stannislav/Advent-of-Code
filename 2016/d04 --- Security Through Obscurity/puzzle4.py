#!/usr/bin/env python3

def chksm(s):
    l = []
    for c in [chr(i) for i in range(ord('a'),ord('z')+1)]:
        l.append([-s.count(c),c])
    l.sort()
    return ''.join([l[i][1] for i in range(5)])

def decrypt(s, n):
    return ''.join(map(lambda c:chr((ord(c)-ord('a')+n)%26+ord('a')),s))

with open("input.txt",'r') as f:
    total = 0
    for line in f:
        chk = line[-7:-2]
        name = line[:-8].split('-')
        sectorid = int(name.pop())
        if(chk == chksm(''.join(name))):
            total += sectorid
        decoded = ' '.join(map(lambda s: decrypt(s,sectorid), name))
        if "north" in decoded:
            print("{}, id={}".format(decoded, sectorid))
    print(total)
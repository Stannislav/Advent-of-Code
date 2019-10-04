#!/usr/bin/env python3

inp = "vzbxkghb"
pwd = [ord(c) - ord('a') for c in inp]


def increment(p, n=7):
    p[n] = (p[n] + 1) % 26
    return increment(p, n - 1) if p[n] == 0 else p


def isgood(p):
    # check straight
    for i in range(len(p) - 2):
        if p[i] == p[i + 1] - 1 and p[i] == p[i + 2] - 2:
            break
    else:
        return False
    # check forbidden letters
    if (ord('i') - ord('a') in p) or (ord('i') - ord('a') in p) or (ord('i') - ord('a') in p):
        return False
    # check pairs
    for i in range(len(p) - 1):
        if p[i] == p[i + 1]:
            for j in range(i + 2, len(p) - 1):
                if p[j] == p[j + 1]:
                    return True
            else:
                return False
    else:
        return False


cnt = 2
while True:
    pwd = increment(pwd)
    if isgood(pwd):
        cnt -= 1
        print(''.join([chr(ord('a') + i) for i in pwd]))
        if not cnt:
            break

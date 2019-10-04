#!/usr/bin/env python3

from hashlib import md5

inp = "qzyelonm"

chars = "0123456789abcdef"

def find_64(cycles):
    triples = [[] for i in range(16)]
    keys_at = []
    i = 0
    while True:
        h = md5((inp+str(i)).encode("utf-8")).hexdigest()
        for k in range(cycles):
            h = md5(h.encode("utf-8")).hexdigest()
        for j in range(16): # look for "xxxxx"
            if chars[j]*5 in h: 
                while len(triples[j]) > 0: # if found, the triples[x] will be processed and then emptied!
                    num = triples[j].pop()
                    if i-num <= 1000:
                        keys_at.append(num)
        for j in range(len(h)-2): # look for "xxx"
            if h[j] == h[j+1] == h[j+2]:
                triples[chars.index(h[j])].append(i)
                break
        i += 1
        if(len(keys_at)) >= 64:
            keys_at.sort()
            if i-keys_at[63] > 1000:
                print(keys_at[63])
                break

find_64(0)
find_64(2016)
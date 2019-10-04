#!/usr/bin/env python3

import hashlib

key = "ckczppom"
i = 0
n = 5
while n < 7:
    i += 1
    h = hashlib.md5((key + str(i)).encode()).hexdigest()
    if h[:n] == "0" * n:
        print(i)
        n += 1

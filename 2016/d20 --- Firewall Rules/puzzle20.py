#!/usr/bin/env python3

ubnd = allowed_cnt = 0

for lo, hi in sorted([list(map(int, line.split('-'))) for line in open("input.txt", 'r')]):
    if lo > ubnd:
        if allowed_cnt == 0:
            print(ubnd)
        allowed_cnt += lo-ubnd
    ubnd = max(ubnd, hi+1) # max(): "hi" could be lower than a previous bound

print(allowed_cnt)

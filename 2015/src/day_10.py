#!/usr/bin/env python3

inp = "1321131112"


def transform(s):
    ret = ""
    pos = 0
    while pos < len(s):
        cnt = 0
        for search in range(pos, len(s)):
            if s[search] == s[pos]:
                cnt += 1
            else:
                break
        ret += str(cnt) + s[pos]
        pos += cnt
    return ret


for i in range(50):
    inp = transform(inp)
    if i == 40 - 1:
        print(len(inp))
print(len(inp))

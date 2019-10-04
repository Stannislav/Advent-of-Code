#!/usr/bin/env python3

def decompress(s, puzzle=1):
    start = s.find('(')
    end = s.find(')', start)
    if start == -1:
        return len(s)
    else:
        nchars, rep = map(int,s[start+1:end].split('x'))
        if puzzle == 1:
            l_sub = rep * (end+1+nchars - (end+1))
        else:
            l_sub = rep * decompress(s[end+1:end+1+nchars], puzzle=2)
        return start + l_sub + decompress(s[end+nchars+1:], puzzle)

with open("input.txt") as f:
    text = f.read().replace('\n','')
    print(decompress(text, puzzle=1))
    print(decompress(text, puzzle=2))
#!/usr/bin/env python3

cnt1 = 0
cnt2 = 0
vowels = "aeiou"
bad_str = ["ab", "cd", "pq", "xy"]


def nice1(s):
    if sum([s.count(c) for c in vowels]) < 3:
        return 0
    if True in [bs in s for bs in bad_str]:
        return 0
    for i in range(0, len(s) - 1):
        if s[i] == s[i + 1]:
            return 1
    return 0


def nice2(s):
    r = 0
    for i in range(len(s) - 1):
        if s[i:i + 2] in s[0:i] or s[i:i + 2] in s[i + 2:]:
            break
    else:  # if for didn't break
        return 0
    for i in range(len(s) - 2):
        if s[i] == s[i + 2] and s[i] != s[i + 1]:
            break
    else:  # if for didn't break
        return 0
    return 1


with open("../input/input_05.txt", 'r') as f:
    for line in f:
        cnt1 += nice1(line)
        cnt2 += nice2(line)
print(cnt1)
print(cnt2)

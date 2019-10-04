#!/usr/bin/env python3


# Input
with open('02_input.txt', 'r') as f:
    lines = [line.strip() for line in f]
nl = len(lines)
xl = len(lines[0])


# Part 1
# Save letter counts in a dictionary, check if values '2' and '3' among them
s2, s3 = 0, 0
keys = [chr(i + ord('a')) for i in range(26)]
for line in lines:
    cnt = dict(zip(keys, [0] * len(keys)))

    for c in line.strip():
        cnt[c] += 1
    s2 += int(2 in cnt.values())
    s3 += int(3 in cnt.values())

print(f"Part 1: {s2 * s3}")


# Part 2
# Generate word pairs and compare them letter by letter
def gen_pairs():
    for i1 in range(nl):
        for i2 in range(i1+1, nl):
            yield lines[i1], lines[i2]


for w1, w2 in gen_pairs():
    diff = [w1[j] != w2[j] for j in range(xl)]

    if(sum(diff) == 1):
        print("Part 2:", ''.join([c for i, c in enumerate(w1) if not diff[i]]))
        break

#!/usr/bin/env python3

freqs = [[0 for x in range(26)] for i in range(8)]

with open("input.txt",'r') as f:
	for line in f:
		for i, c in enumerate(line.rstrip()):
			freqs[i][ord(c)-ord('a')] += 1

print(''.join([chr(l.index(max(l))+ord('a')) for l in freqs]))
print(''.join([chr(l.index(min(l))+ord('a')) for l in freqs]))
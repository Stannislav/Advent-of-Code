#!/usr/bin/env python3

wx, wy = (50, 6)
display = [[0 for i in range(wx)] for j in range(wy)]

with open("input.txt", 'r') as f:
	for line in f:
		l = line.split()
		if l[0] == 'rect':
			dx, dy = map(int,l[1].split('x'))
			for j in range(dy):
				display[j][:dx] = [1 for i in range(dx)]
		elif l[0] == 'rotate':
			d = int(l[-1])
			pos = int(l[-3].split('=')[-1])
			if l[1] == 'row':
				display[pos] = display[pos][wx-d:] + display[pos][:-d]
			if l[1] == 'column':
				col = [display[i][pos] for i in range(wy)]
				col = col[wy-d:] + col[:-d]
				for i in range(wy):
					display[i][pos] = col[i]

print(sum([row.count(1) for row in display]))
for i in range(wy):
	print(''.join(map(lambda c: u"\u2593" if c else ' ', display[i])))
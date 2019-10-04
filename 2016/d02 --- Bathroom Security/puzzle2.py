#!/usr/bin/env python3

moves = {'U': [-1,0], 'D': [1,0], 'L': [0,-1], 'R': [0,1]}
def solve(pos, padlock, p_test):
	with open("input.txt",'r') as f:
		for line in f:
			for c in line.rstrip():
				new_pos=[pos[i]+moves[c][i] for i in [0,1]]
				if p_test(new_pos): pos = new_pos
			print("{}".format(padlock[pos[0]][pos[1]]),end='')
		print("")

def test1(p): return False if p[0] < 0 or p[0] > 2 or p[1] < 0 or p[1] > 2 else True
def test2(p): return False if abs(p[0]-2)+abs(p[1]-2)>2 else True

padlock1=[
[1,2,3],
[4,5,6],
[7,8,9]
]
start1 = [1,1]

padlock2=[
[0,0,1,0,0],
[0,2,3,4,0],
[5,6,7,8,9],
[0,'A','B','C',0],
[0,0,'D',0,0]
]
start2 = [2,0]

solve(start1, padlock1, test1)
solve(start2, padlock2, test2)

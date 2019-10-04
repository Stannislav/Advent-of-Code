#!/usr/bin/env python
import numpy

# Read path from files
path=[]
with open("input.txt",'r') as f:
	path = f.read().split(', ')
# Dictionary for directions
dirs=map(numpy.array,[[1,0],[0,1],[-1,0],[0,-1]])
t={'R':1, 'L':-1}

# Puzzle 1
print "Puzzle 1"
# path=['R2','L3']
# path=['R2','R2','R2']
# path=['R5','L5','R5','R3']
pos=numpy.array([0,0])
orientation = 0
for s in path:
	orientation = (orientation + t[s[0]])%4
	pos += dirs[orientation]*int(s[1:])
print "position:", pos
print "length:", abs(pos[0])+abs(pos[1])
print ""

# Puzzle 2
print "Puzzle 2"
# path=['R8','R4','R4','R8']
pos=numpy.array([0,0])
orientation = 0
# want to remember all point along the path now
nodes=[list(pos)]
# initialize an empty 400x400 map
mymap=[[' ' for i in range(400)] for j in range(400)]
# mark the center by an 'O'
mymap[200][200]='O'
arrw=['^','>','v','<']
for s in path:
	orientation = (orientation + t[s[0]])%4
	for i in range(int(s[1:])):
		pos += dirs[orientation]
		mymap[200-pos[0]][pos[1]-200] = arrw[orientation]
		if list(pos) in nodes:
			break
		else:
			nodes.append(list(pos))
	else: # called if the inner loop ended normally
		continue # then just continue
	break # called if the inner loop ended by break, then we break out of outer loop
print "position:",pos
print "length:",abs(pos[0])+abs(pos[1])

# export the map into a file
with open("map.txt",'w') as map_out:
	for row in mymap:
		map_out.write(''.join(row) + '\n')
#!/usr/bin/env python3

first_floor = []
dim = 0

with open("input.txt", 'r') as f:
    line = f.read().rstrip()
    dim = len(line)
    first_floor = [0] + [0 if c=='.' else 1 for c in line] + [0]

def solve(n):
    floor = first_floor
    total = sum(floor)
    for i in range(n-1):
        new = [0]*(dim+2)
        for j in range(1, dim+1):
            new[j] = 0 if floor[j-1] == floor[j+1] else 1
        floor=new[:]
        total += sum(floor)
        
    print(n*dim-total)

solve(40)
solve(400000)

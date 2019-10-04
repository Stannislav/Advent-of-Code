#!/usr/bin/env python3

def get_triangle1():
    with open("input.txt",'r') as f:
        for line in f:
            yield map(int,line.split())

def get_triangle2():
    with open("input.txt",'r') as f:
        for line1 in f:
            tri = [
                map(int,line1.split()),
                map(int,next(f).split()),
                map(int,next(f).split())
            ]
            for i in range(3):
                yield [tri[0][i],tri[1][i],tri[2][i]]

def solve(triangle_gen):
    cnt_all = cnt_good = 0
    for sides in triangle_gen():
        cnt_all += 1
        if sum(sides)-2*max(sides) > 0:
            cnt_good += 1    
    print("{} of {} triangles are good".format(cnt_good,cnt_all))

solve(get_triangle1)
solve(get_triangle2)

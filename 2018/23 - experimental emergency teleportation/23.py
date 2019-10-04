#!/usr/bin/env python3


import re
from collections import defaultdict


with open('23_input.txt', 'r') as f:
    lines = [line for line in f]


# lines = [
#     "pos=<0,0,0>, r=4",
#     "pos=<1,0,0>, r=1",
#     "pos=<4,0,0>, r=3",
#     "pos=<0,2,0>, r=1",
#     "pos=<0,5,0>, r=3",
#     "pos=<0,0,3>, r=1",
#     "pos=<1,1,1>, r=1",
#     "pos=<1,1,2>, r=1",
#     "pos=<1,3,1>, r=1",
# ]
#
# lines = [
#     "pos=<10,12,12>, r=2",
#     "pos=<12,14,12>, r=2",
#     "pos=<16,12,12>, r=4",
#     "pos=<14,14,14>, r=6",
#     "pos=<50,50,50>, r=200",
#     "pos=<10,10,10>, r=5",
# ]

def get_data():
    return [tuple(map(int, re.findall('-?\d+', line))) for line in lines]


def dist(bot1, bot2):
    return abs(bot1[0] - bot2[0]) + abs(bot1[1] - bot2[1]) + abs(bot1[2] - bot2[2])


bots = get_data()

# Part 1
strongest = max(bots, key=lambda x: x[-1])
print('Part 1:', sum(1 for bot in bots if dist(bot, strongest) <= strongest[-1]))


# Part 2
# 1. for all bot pairs determine if their ranges intersect
# 2. find sets of bots where all ranges mutually intersect
# 3. in all these sets find the point closest to (0, 0, 0)

# print('1. Finding pairwise overlaps... ', end='', flush=True)
# pairwise = set()
# graph = defaultdict(set)
# for n1 in range(len(bots)):
#     for n2 in range(n1+1, len(bots)):
#         if dist(bots[n1], bots[n2]) <= bots[n1][-1] + bots[n2][-1]:
#             graph[n1].add(n2)
# print('Done.')
#
#
# # graph = {
# #     0: {1, 2, 3},
# #     1: {0, 2, 3, 4, 5},
# #     2: {0, 1, 3, 4, 5},
# #     3: {0, 1, 2},
# #     4: {1, 2, 5},
# #     5: {1, 2, 4}
# # }
#
#
#
# # do DFS on complete graphs
# *** Use Bron–Kerbosch algorithm to find maximal clique! ***
# max_len = 0
# max_conn = []
# def grow_graph(g=set(), candidates=set(graph.keys())):
#     # print('g', g)
#     global max_len, max_conn
#
#     if not candidates:  # can't grow any more
#         if len(g) > max_len:
#             max_conn = [g]
#             max_len = len(g)
#         elif len(g) == max_len:
#             max_conn.append(g)
#     else:  # can grow
#         for c in candidates:
#             newcand = set(nc for nc in candidates if nc in graph[c] and nc > c)
#             if len(g) + 1 + len(newcand) < max_len:  # won't be able to improve max_len
#                 continue
#             grow_graph(g | {c}, newcand)
#
# print('2. Find the maximal overlap... ', end='', flush=True)
# grow_graph()
# print('Done.')
# conn = max_conn.pop()
# with open('tmp.txt', 'w') as f:
#     for x in conn:
#         f.write(str(x) + '\n')
#
with open('tmp.txt', 'r') as f:
    conn = [int(x) for x in f]

print(len(conn))
lo = 0
hi = float('inf')
r = 0
for bot in [bots[i] for i in conn]:
    x = bot[-1] + dist(bot, (0, 0, 0, 0))
    hi = min(x, hi)

# r = 0
# tried = set()
# for _ in range(20):  # while true
#     tried.add(r)
#     print(f'Testing r = {r}', lo, hi)
#     probe = all(dist(bots[i], [0, 0, 0, 0]) <= bots[i][-1] + r for i in conn)
#     if probe:  # intersecting all, reduce r
#         if r - 1 in tried:
#             break
#         hi = r
#         r -= max((r - lo) // 2, 1)
#     else:  # increase
#         lo = r
#         r += max((hi - r) // 2, 1)
#
# print(r)
# for x in [r - 1, r, r + 1]:
#     print(all(dist(bots[i], [0, 0, 0, 0]) <= bots[i][-1] + x for i in conn))

print(max(dist((0, 0, 0, 0), bots[i]) - bots[i][-1] for i in conn))
#
# r = 0
# while not all(dist(bots[i], [0, 0, 0, 0]) <= bots[i][-1] + r for i in conn):
#     if r % 1000000 == 0:
#         print(r)
#     r += 1
# print(r)
# #
# print('3. Calculate the intersecting area... ')
# dist = 0
# for n in max_conn[0]:
#     b = bots[n]
#     d = abs(b[0]) + abs(b[1]) + abs(b[2]) - b[3]
#     dist = max(dist, d)
# dist = 0
# for g in max_conn:
#     dist = max(dist, max(abs(bots[n][0]) + abs(bots[n][1]) + abs(bots[n][2]) - bots[n][3] for n in g))
# print('Part 2:', dist)

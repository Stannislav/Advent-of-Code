#!/usr/bin/env python3

t = 2503

with open("../input/input_14.txt", 'r') as f:
    deer = []
    for line in f:
        name, _, _, v, _, _, tv, _, _, _, _, _, _, tr, _ = line.rstrip().split()
        deer.append([int(v), int(tv), int(tr), name])
    n = len(deer)


    # deer = [[14, 10, 127, "Comet"], [16, 11, 162, "Dancer"]]
    # n = 2
    # t = 1000

    def get_dist_at(time):
        dist = [0] * n
        for i in range(n):
            v, tv, tr = deer[i][0:3]
            runs = time // (tv + tr)
            d = runs * v * tv + v * min(time - runs * (tv + tr), tv)
            dist[i] = d
        return dist


    # Puzzle 1
    distances = get_dist_at(t)
    who_won = distances.index(max(distances))
    print(distances[who_won], "({})".format(deer[who_won][3]))

    # Puzzle 2
    score = [0] * n
    for time in range(1, t + 1):
        dist = get_dist_at(time)
        m = max(dist)
        score = [score[i] + 1 if dist[i] == m else score[i] for i in range(n)]
    who_won = score.index(max(score))
    print(score[who_won], "({})".format(deer[who_won][3]))

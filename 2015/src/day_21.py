#!/usr/bin/env python

# [name, cost, damage, armor]
weapons = [
    ["Dagger", 8, 4, 0],
    ["Shortsword", 10, 5, 0],
    ["Warhammer", 25, 6, 0],
    ["Longsword", 40, 7, 0],
    ["Greataxe", 74, 8, 0]
]

armor = [
    ["Nothing", 0, 0, 0],
    ["Leather", 13, 0, 1],
    ["Chainmail", 31, 0, 2],
    ["Splintmail", 53, 0, 3],
    ["Bandedmail", 75, 0, 4],
    ["Platemail", 102, 0, 5]
]

rings = [
    ["Nothing", 0, 0, 0],
    ["Nothing", 0, 0, 0],
    ["Damage +1", 25, 1, 0],
    ["Damage +2", 50, 2, 0],
    ["Damage +3", 100, 3, 0],
    ["Defense +1", 20, 0, 1],
    ["Defense +2", 40, 0, 2],
    ["Defense +3", 80, 0, 3]
]


def gen_equipment():
    for w in weapons:
        for a in armor:
            for r1 in range(len(rings)):
                for r2 in range(r1 + 1, len(rings)):
                    cost = w[1] + a[1] + rings[r1][1] + rings[r2][1]
                    dam = w[2] + a[2] + rings[r1][2] + rings[r2][2]
                    arm = w[3] + a[3] + rings[r1][3] + rings[r2][3]

                    yield cost, dam, arm


player, boss = range(2)


class State:
    def __init__(self):
        self.stats = {
            player: dict(zip(["hp", "damage", "armor"], [100, 0, 0])),
            boss: dict(zip(["hp", "damage", "armor"],
                           [int(line.strip().split()[-1]) for line in open("../input/input_21.txt", 'r').readlines()]))
        }

        self.finished = False
        self.winner = None
        self.at = player
        self.df = boss

    def step(self):
        dam = self.stats[self.at]["damage"] - self.stats[self.df]["armor"]

        if dam <= 0:
            dam = 1
        self.stats[self.df]["hp"] -= dam

        if self.stats[self.df]["hp"] <= 0:
            self.finished = True
            self.winner = self.at

        self.at, self.df = self.df, self.at

    def run(self):
        while not self.finished:
            self.step()


def solve(max_func, winner_target):
    min_cost = None
    for cost, dam, arm in gen_equipment():
        if min_cost is not None and max_func(cost, min_cost):
            continue
        game = State()
        game.stats[player]["damage"] += dam
        game.stats[player]["armor"] += arm
        game.run()
        if game.winner == winner_target:
            min_cost = cost

    return min_cost


print("part 1: {}".format(solve(lambda x, y: x > y, player)))
print("part 2: {}".format(solve(lambda x, y: x < y, boss)))

#!/usr/bin/env python3


from queue import Queue
import numpy as np
from collections import namedtuple


with open('15_input.txt', 'r') as f:
    lines = [line.strip() for line in f]


# lines = [
# "#######",
# "#.G...#",
# "#...EG#",
# "#.#.#G#",
# "#..G#E#",
# "#.....#",
# "#######",
# ]

# lines = [
# "#######",
# "#G..#E#",
# "#E#E.E#",
# "#G.##.#",
# "#...#E#",
# "#...E.#",
# "#######",
# ]

# lines = [
# "#######",
# "#E..EG#",
# "#.#G.E#",
# "#E.##E#",
# "#G..#.#",
# "#..E#.#",
# "#######",
# ]

lines = [
"#########",
"#G......#",
"#.E.#...#",
"#..##..G#",
"#...##..#",
"#...#...#",
"#.G...G.#",
"#.....G.#",
"#########",
]

Creature = namedtuple("Creature", ['tag', 'health'])
map = np.array([list(line) for line in lines])
players_init = dict()
for i, row in enumerate(map):
    for j, c in enumerate(row):
        if c in 'EG':
            players_init [i, j] = Creature(c, 200)
            map[i, j] = '.'


players = dict()

def reset_game():
    global players
    players = {k: v for k, v in players_init.items()}


def viz():
    print('   ', end='')
    for i in range(map.shape[1]):
        print(' ' if i // 10 == 0 else i // 10, end='')
    print()
    print('   ', end='')
    for i in range(map.shape[1]):
        print(i % 10, end='')
    print()

    for i, row in enumerate(map):
        print(f"{i:2d} ", end='')
        scores = []
        for j, c in enumerate(row):
            if (i, j) in players:
                player, health = players[i, j]
                print(player, end='')

                scores.append(f"{player}({health})")
            else:
                print(c, end='')
        print(f" {i:2d} ", end='')
        print('    ', ', '.join(scores))


    print('   ', end='')
    for i in range(map.shape[1]):
        print(' ' if i // 10 == 0 else i // 10, end='')
    print()
    print('   ', end='')
    for i in range(map.shape[1]):
        print(i % 10, end='')
    print()



def adjacent(i, j):
    ''' return adjacent squares in reading order '''
    yield from [(i-1, j), (i, j-1), (i, j+1), (i+1, j)]


def get_dist(i, j, it, jt):
    # First compute all distances to all empty cells
    dist = np.zeros(map.shape) + float('inf')
    dist[i, j] = 0
    q = Queue()
    q.put((i, j))
    while not q.empty():
        i, j = q.get()
        d = dist[i, j] + 1
        for k, l in adjacent(i, j):
            if map[k, l] != '#' and (k, l) not in players and d < dist[k, l]:
                dist[k, l] = d
                q.put((k, l))

    # Find from which square one reaches the target
    dir = sorted([(dist[k, l], k, l) for k, l in adjacent(it, jt)])
    return dist[it, jt], dir[0][1], dir[0][2]

# NEW!!!
def get_dist_new(i, j, it, jt):
    if (i, j) == (it, jt):
        return 0, -1, -1
    # First compute all distances to all empty cells
    # dist = np.zeros(map.shape) + float('inf')
    dist = {(i, j): 0}

    q = Queue()
    q.put((i, j))
    while not q.empty():
        i, j = q.get()
        d = dist[i, j] + 1
        for k, l in adjacent(i, j):
            if map[k, l] != '#' and (k, l) not in players:
                if (k, l) in dist and d >= dist[k, l]:
                    continue
                dist[k, l] = d
                q.put((k, l))

    if (it, jt) not in dist:
        return float('inf'), -1, -1

    # Find from which square one reaches the target
    dir = sorted([(dist[k, l], k, l) for k, l in adjacent(it, jt) if (k, l) in dist])
    return dist[it, jt], dir[0][1], dir[0][2]



def play_game(damage=3):
    viz()
    round = 0
    while True:
        print(f"--- round {round} ----------------------")
        print('players', sorted(players))
        for i, j in sorted(players):
            if (i, j) not in players:  # already dead
                continue
            player, health = players.pop((i, j))
            # print('playing:', player, i, j)
            enemy = 'G' if player == 'E' else 'E'

            # Find squares which are in range of each target
            enemies_pos = []
            for (k, l) in players:
                if players[k, l].tag == enemy:
                    enemies_pos.append((k, l))
            if not enemies_pos:  # no enemies left - battle finished
                players[i, j] = Creature(player, health)
                break

            targets = []
            for (k, l) in enemies_pos:
                for (m, n) in adjacent(k, l):
                    if (map[m, n] == '.' and (m, n) not in players) or (m, n) == (i, j):
                        targets.append((m, n))
            # print('  targets', targets)


            # Move if necessary and if can
            if targets:
                distances = []
                for (k, l) in targets:
                    d, ni, nj = get_dist(k, l, i, j)
                    distances.append((d, k, l, ni, nj))
                distances.sort()
                # print('  distances', distances)
                if distances[0][0] > 0 and distances[0][0] != float('inf'):
                    # print('  moving')
                    i, j = distances[0][3], distances[0][4]
            players[i, j] = Creature(player, health)

            # Attack if somebody next
            targets = []
            for (k, l) in adjacent(i, j):
                if (k, l) in players and players[k, l].tag == enemy:
                    targets.append((players[k, l].health, k, l))
            if targets:
                targets.sort()
                hp, k, l = targets[0]
                hp -= 3 if player == 'G' else damage
                if hp > 0:
                    players[k, l] = Creature(enemy, hp)
                else:
                    players.pop((k, l))

        else:
            round += 1
            print(f"\nAfter {round+1} round(s):")
            viz()
            continue
        viz()
        return round * sum(players[i, j].health for (i, j) in players)


def count_elves():
    return len([0 for (i, j) in players if players[i, j].tag == 'E'])


# Part 1
reset_game()
print('Part 1:', play_game())


# # Part 2
# # solution: 15
# damage = 15
# while True:
#     reset_game()
#     before = count_elves()
#     print('Elves before:', before)
#
#     print('Part 1:', play_game(damage))
#     after = count_elves()
#     print('Elves after:', after)
#
#     if before == after:
#         print('damage:', damage)
#         break
#     else:
#         damage += 1
# viz()

# print(len(players), 'players')
# for i in range(200):
#     get_dist_new(9, 1, 30, 4)
# print(get_dist_new(9, 1, 30, 4))

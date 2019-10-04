#!/usr/bin/env python3

import re


with open('09_input.txt', 'r') as f:
    n_players, last_value = [int(n) for n in re.findall('\d+', f.read())]


class Node:
    __slots__ = ['prev', 'next']
    def __init__(self, prev = -1, next = -1):
        self.prev = prev
        self.next = next


def solve(n_players, last_value):
    scores = [0] * n_players
    marbles = {0: Node(1, 1), 1: Node(0, 0)}
    cm = 1  # current marble
    cp = 1  # current player
    for n in range(2, last_value + 1):
        if n % 23:
            marbles[n] = Node(
                prev = marbles[cm].next,
                next = marbles[marbles[cm].next].next
            )
            marbles[marbles[n].prev].next = n
            marbles[marbles[n].next].prev = n
            cm = n
        else:
            for _ in range(6):
                cm = marbles[cm].prev
            scores[cp] += n + marbles[cm].prev
            marbles[marbles[marbles[cm].prev].prev].next = cm
            marbles[cm].prev = marbles[marbles[cm].prev].prev
        cp = (cp + 1) % n_players

    return max(scores)


print("Part 1:", solve(n_players, last_value))
print("Part 2:", solve(n_players, last_value * 100))

#!/usr/bin/env python3


with open('20_input.txt', 'r') as f:
    data = f.read()


def get_example(n):
    examples = {
        0: "^WNE$",
        1: "^ENWWW(NEEE|SSE(EE|N))$",
        2: "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$",
        3: "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$",
        4: "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$",
    }
    return examples[n]


dir = {'N': 1j, 'S': -1j, 'E': 1, 'W': -1}

def traverse(it, map={0: 0}, pos=0, depth=0):
    initial_pos = pos
    initial_depth = depth
    for c in it:
        if c in dir:
            pos += dir[c]
            if pos in map:  # been here before, so we are backtracking
                depth = map[pos]
            else:
                depth += 1
                map[pos] = depth
        elif c == '|':
            pos = initial_pos
            depth = initial_depth
        elif c == '(':
            traverse(it, map, pos, depth)
        elif c == ')':
            return
        elif c == '$':
            return map
        else:
            print(f'Unknown character: {c}')


# data = get_example(4)
map = traverse(iter(data[1:]))
print('Part 1:', max(map.values()))
print('Part 2:', len([n for n in map.values() if n >= 1000]))

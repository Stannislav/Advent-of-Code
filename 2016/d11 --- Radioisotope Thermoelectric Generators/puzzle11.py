#!/usr/bin/env python3

class State():
    def __init__(self, e, pos, dist=float('inf'), prev=None):
        self.e = e 
        self.pos = pos
        self.dist = dist
        self.prev = prev

    # we need a good hash that is the same for all (g,m)-pair permutations
    # this hash will be used to index the dictionary of states
    def __hash__(self):
        l = []
        for i in range(int(len(self.pos)/2)):
            l += [(self.pos[2*i], self.pos[2*i+1])]
        l.sort() # (g,m) pairs are indistinguishable!
        return hash(tuple([self.e] + l))

def possible_moves(state):
    n = int(len(state.pos)/2)

    def is_good(pos):
        for m in range(n):
            if pos[2*m+1] == pos[2*m]:
                continue # safe
            for g in range(n):
                if pos[2*g] == pos[2*m+1]:
                    return False
        return True

    for i in range(2*n):
        for j in range(i, 2*n):
            if state.pos[j] == state.pos[i] == state.e:
                if state.e < 3:
                    new_pos = list(state.pos)
                    new_pos[i] += 1
                    if i != j:
                        new_pos[j] +=1
                    if is_good(new_pos):
                        yield State(state.e+1, new_pos)
                if state.e > 0:
                    new_pos = list(state.pos)
                    new_pos[i] -= 1
                    if i != j:
                        new_pos[j] -= 1
                    if is_good(new_pos):
                        yield State(state.e-1, new_pos)

def solve(pos): # Do a BFS search
    start = State(0, pos, 0)
    endhash = hash(State(3, [3]*len(pos)))
    queue = [start]
    d_states = {start: start}

    while len(queue) > 0:
        state = queue.pop(0)
        for s_next in possible_moves(state):
            h = hash(s_next)
            if h not in d_states:
                d_states[h] = s_next
            if d_states[h].dist > state.dist+1:
                d_states[h].dist = state.dist+1
                d_states[h].prev = state
                queue.append(d_states[h])
            if h == endhash:
                return state.dist+1

print(solve([0, 0, 0, 0, 1, 2, 1, 1, 1, 1]))
print(solve([0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 0, 0, 0, 0]))

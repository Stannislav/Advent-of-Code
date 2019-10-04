from functools import reduce
from operator import mul


def solve(weights, target):
    """
    Solve by recursively forming all possible combinations for group 1.
    Because we pre-sort the weight list in descending order, the shortest
    solutions will be found first, and longer solutions are pruned away.
    """

    weights = list(reversed(sorted(weights)))
    entanglements = []
    group = []

    def traverse(pos=0, best_len=float('inf')):
        # Prune: if shorter length already found, then don't bother looking
        if len(group) > best_len:
            return best_len

        if sum(group) < target:  # Group sum too small, recurse
            for i, w in enumerate(weights[pos:]):
                group.append(w)
                best_len = traverse(pos + i + 1, best_len)
                group.pop()
        elif sum(group) == target:
            entanglements.append(reduce(mul, group))
            best_len = len(group)
        return best_len

    traverse()

    return min(entanglements)


weights = []
with open("../input/input_24.txt", 'r') as f:
    for line in f:
        weights.append(int(line))

print("Part 1:", solve(weights, sum(weights) // 3))
print("Part 2:", solve(weights, sum(weights) // 4))

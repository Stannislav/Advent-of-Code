from itertools import islice
from collections import defaultdict
import random


def read_input():
    """Read the contents of the puzzle input file

    :return relations: replacement rules
    :return target: target molecule
    """
    relations = []

    # Read file contents
    with open("../input/input_19.txt", 'r') as f:
        for line in f:
            if line == '\n':
                break
            a, _, b = line.strip().partition(" => ")
            relations.append((a, b))
        target = f.readline().strip()

    return relations, target


def iterate_atoms(molecule):
    """Iterate all atoms in a linear molecule

    For example `molecule="CRnSiRnCa"` would yield
    `('C', 'Rn', 'Si', 'Rn', 'Ca')`

    :param molecule: a linear molecule of atoms
    :yield: single atoms in given molecule
    """
    idx = 0
    for i, c in islice(enumerate(molecule), 1, None, None):
        if c.isupper():
            yield molecule[idx:i]
            idx = i
    yield molecule[idx:]


def to_cnf(relations):
    """Transform relations to Chomsky-Normal-Form (CNF)

    The only transformation we need to do for this puzzle
    is to break up one-to-many relations into one-to-two
    relations. This is done by mapping relations
    of the form "a -> bcde" to
        a -> (bcd, e)
        bcd -> (bc, d)
        bc -> (b, c)

    :param relations: old relations, potentially not in CNF
    :return new_relations: transformed relations in CNF
    """

    # Has to be a set otherwise duplicate entries possible
    new_relations = set()

    for left, right in relations:
        right = list(iterate_atoms(right))
        while len(right) > 1:
            long, short = right[:-1], right[-1]
            new_relations.add((left, (''.join(long), short)))
            left, right = ''.join(long), long

    return new_relations


def do_cyk(relations, target, verbose=False):
    """Perform the CYK parsing of the molecule

    In case of a successful parse the CYK table containing
    all possible paths for the molecule generation is returned.

    In order to reconstruct the paths we also record the branching
    path at each found node.

    The returned table is recorded in a dictionary where the keys
    are all non-trivial nodes of the form (row, column, molecule)
    and the values are the branching targets in the form
    (target_1, target_2) = ((row_1, column_1, molecule_1),
                            (row_2, column_2, molecule_2))

    :param relations: relations of the grammar
    :param target: the target molecule
    :param verbose: verbose output
    :return: the CYK table
    """
    cyk = defaultdict(set)

    # Initialise the first row of the CYK table
    for i, molecule in enumerate(iterate_atoms(target)):
        cyk[(0, i, molecule)] = set()
    n = len(cyk)

    # The actual CYK algorithm
    for l in range(1, n):
        if verbose:
            print("l = {} of {}".format(l, n - 1))
        for s in range(n - l):
            for p in range(l):
                for left, (r1, r2) in relations:
                    if (p, s, r1) in cyk and (l - p - 1, s + p + 1, r2) in cyk:
                        cyk[(l, s, left)].add(((p, s, r1), (l - p - 1, s + p + 1, r2)))

    if (n - 1, 0, 'e') in cyk:
        if verbose:
            print("Solution exists!")
        return cyk
    else:
        if verbose:
            print("No solutions exists")
        return None


def get_random_target(relations, steps=5, verbose=False):
    """Generate random target molecules

    We have to work with original relations obtained from
    the input file, as the CNF-converted relations contain
    incomplete targets.

    :param relations: the replacement relations
    :param steps: the number of generation steps to carry out
    :param verbose: print verbose output
    :return:
    """

    cnt = 0
    molecule = ['e']
    while cnt < steps:
        idx = random.randint(0, len(molecule) - 1)
        candidates = [right for left, right in relations if left == molecule[idx]]
        if len(candidates) == 0:
            continue
        right = random.choice(candidates)
        new_molecule = molecule[:idx] + \
            list(iterate_atoms(right)) + \
            molecule[idx + 1:]
        if verbose:
            print("{} ({} => {}) {}".format(''.join(molecule),
                                            molecule[idx],
                                            right,
                                            ''.join(new_molecule)))
        molecule = new_molecule

        cnt += 1

    return ''.join(molecule)


def iter_cyk_paths(cyk):
    """Given a CYK table iterate over all possible paths.

    Each branching is represented by a tuple of the form
    (molecule, (target_1, target_2), so a possible path
    could be of the form
        (a, (b, (c, (d, e))))
    corresponding to the path
        a (a -> bc) bc
        bc (c -> de) bde

    :param cyk: the CYK table
    :yield path: all possible paths contained in the CYK table
    """

    def gen_path(node):
        if len(cyk[node]) == 0:
            # This is a leaf node, so just yield the molecule name
            yield node[2]

        # A node could correspond to multiple possible branchings
        # leading to a number of possible paths through the CYK table
        # Here we iterate over all such possible paths
        for child_1, child_2 in cyk[node]:
            for next_1 in gen_path(child_1):
                for next_2 in gen_path(child_2):
                    yield (node[2], (next_1, next_2))

    # Find the starting point
    start = None
    for (x, y, molecule) in cyk:
        if molecule == 'e':
            start = (x, y, 'e')
            break

    # Do the iteration
    if start is not None:
        yield from gen_path(start)
    else:
        return


def count_steps(path):
    """Count the number of replacement steps in a given path

    :param path: the path through the CYK table
    :return steps: the number of replacement steps
    """
    # path is always a branching of the form
    # (a, (b, c))
    # standing for a => (b, c)
    name, result = path
    steps = 1  # count the branching that `path` stands for
    # add all sub-branchings
    for x in result:
        if not isinstance(x, str):
            steps += count_steps(x)

    return steps


def reduce_path(path):
    """Undo the Chomsky-Normal-Form (CNF)

    In this particular case we re-introduce one-to-many
    relations where by many we mean more than two.

    :param path: a path generated from the CYK table
    :return path: a path with one-to-many relations
    """
    ret = []
    for x in path:
        if isinstance(x, str):
            ret.append(x)
        else:  # x is tuple
            if isinstance(x[0], str) and len(list(iterate_atoms(x[0]))) > 1:
                for y in x[1:]:
                    for reduced in reduce_path(y):
                        ret.append(reduced)
            else:
                ret.append(reduce_path(x))
    return ret


def main():
    print(">>> Reading input")
    relations, target = read_input()

    print(">>> Transforming relations to CNF")
    cnf_relations = to_cnf(relations)

    # There are too many possible paths to iterate over
    # in the original target. Generate a random small
    # molecule instead
    steps = 5
    print(">>> Generating a random target (steps={})".format(steps))
    target = get_random_target(relations, steps=steps, verbose=True)
    print("Target:", target)

    print(">>> Doing the CYK")
    cyk = do_cyk(cnf_relations, target, verbose=True)

    all_paths = [reduce_path(path) for path in iter_cyk_paths(cyk)]
    print(">>> {} path(s) found".format(len(all_paths)))

    print("Normally all distances should be the same")
    min_dist = min(map(count_steps, all_paths))
    max_dist = max(map(count_steps, all_paths))
    print("Minimal distance:", min_dist)
    print("Maximal distance:", max_dist)
    assert min_dist == max_dist, "Path distances are not all equal"


if __name__ == "__main__":
    main()

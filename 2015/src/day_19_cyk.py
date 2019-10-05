from itertools import islice


def read_input():
    relations = []

    # Read file contents
    with open("../input/input_19.txt", 'r') as f:
        for line in f:
            if line == '\n':
                break
            a, _, b = line.strip().partition(" => ")
            relations.append((a, b))
        target = f.readline().strip()

    return relations, target,


def iterate_molecules(compound):
    """Iterate all molecules in a linear compound

    For example `compound="CRnSiRnCa"` would yield
    `("C", "Rn", "Si", "Rn", "Ca")`

    :param: compound: a linear compound of molecules
    :yield: single molecule in compound
    """
    idx = 0
    for i, c in islice(enumerate(compound), 1, None, None):
        if c.isupper():
            yield compound[idx:i]
            idx = i
    yield compound[idx:]


def get_labels(relations):
    """Create numeric labels for all molecules

    :param relations: relations of the context-free grammar
    :return labels: numeric labels
    """
    labels = dict()
    idx = 0
    for a, b in relations:
        for molecule in iterate_molecules(a + b):
            if molecule not in labels:
                labels[molecule] = idx
                idx += 1

    return labels


def to_cnf(relations):
    """Transform relations to Chomsky-Normal-Form (CNF)

    In particular, reduce all one-to-many relations to
    one-to-two relations. This is done by mapping relations
    of the form "a -> bcde" to
        a -> (bcd, e)
        bcd -> (bc, d)
        bc -> (b, c)

    :param relations: old relations, potentially not in (CNF)
    :return new_relations: transformed relations in CNF
    """

    new_relations = []

    for left, right in relations:
        right = list(iterate_molecules(right))
        while len(right) > 1:
            long, short = right[:-1], right[-1]
            new_relations.append((left, (''.join(long), short)))
            left, right = ''.join(long), long

    return new_relations


def do_cyk(relations, target):
    cyk = dict()
    for i, molecule in enumerate(iterate_molecules(target)):
        cyk[(0, i, molecule)] = True

    print(cyk)


def main():
    print("Reading input")
    relations, target = read_input()

    print("Generating labels")
    labels = get_labels(relations)
    print(labels)
    print(relations)

    print("Transforming relations to CNF")
    new_relations = to_cnf(relations)
    print(new_relations)

    print("Doing the CYK")
    do_cyk(relations, target)


if __name__ == "__main__":
    main()




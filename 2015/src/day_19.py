def read_input():
    rules = []

    # Read file contents
    with open("../input/input_19.txt", 'r') as f:
        for line in f:
            if line == '\n':
                break
            a, _, b = line.strip().partition(" => ")
            rules.append((a, b))
        target = f.readline().strip()

    return rules, target,


def part1(target, rules):
    results = set()
    for a, b in rules:
        for i in range(len(target)):
            if target.startswith(a, i):
                results.add(target[:i] + b + target[i + len(a):])

    return len(results)


def part2(target, rules):
    def backtrack(mol, steps=0):
        """
        We'll assume (without formally proving) that there is a
        unique solution. So if we've found one then we can stop
        """

        if mol == "e":
            return steps

        for b, a in rules:
            for i in range(len(mol)):
                if mol.startswith(a, i):
                    result = backtrack(mol[:i] + b + mol[i + len(a):], steps + 1)
                    if result != -1:
                        return result
        return -1

    return backtrack(target)


def main():
    rules, target = read_input()
    print("Part 1:", part1(target, rules))
    print("Part 2:", part2(target, rules))


if __name__ == "__main__":
    main()




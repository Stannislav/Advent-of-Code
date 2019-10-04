instructions = []

with open("../input/input_23.txt", 'r') as f:
    for line in f:
        cmd, _, rest = line.strip().partition(' ')
        instructions.append((cmd, rest))


def execute(instructions, a, b):
    reg = {'a': a, 'b': b}
    pos = 0
    while pos < len(instructions):
        cmd, rest = instructions[pos]
        if cmd == 'hlf':
            reg[rest] //= 2
        elif cmd == 'tpl':
            reg[rest] *= 3
        elif cmd == 'inc':
            reg[rest] += 1
        elif cmd == 'jmp':
            pos += int(rest)
            continue
        elif cmd == 'jie':
            r, offset = rest.split(', ')
            if reg[r] % 2 == 0:
                pos += int(offset)
                continue
        elif cmd == 'jio':
            r, offset = rest.split(', ')
            if reg[r] == 1:
                pos += int(offset)
                continue
        else:
            print("Unknown instruction!")

        pos += 1

    return reg['a'], reg['b']


_, b = execute(instructions, 0, 0)
print("Part 1:", b)
_, b = execute(instructions, 1, 0)
print("Part 2:", b)

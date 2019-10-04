import re

with open("../input/input_25.txt", 'r') as f:
    text = f.read()


def solve(target_row, target_col, code=20151125, row=1, col=1):
    while row != target_row or col != target_col:
        code = (code * 252533) % 33554393
        row, col = row - 1, col + 1
        if row == 0:
            row = col
            col = 1
    return code


row_col = map(int, re.findall("[0-9]+", text))
print("Part 1:", solve(*row_col))
print("Part 2: not needed")

#!/usr/bin/env python3

data = []
# read the input file
with open("../input/input_15.txt", 'r') as f:
    for line in f:
        s = ''.join([c for c in line if c not in ",\n:"])
        name, _, cap, _, dur, _, fla, _, tex, _, cal = s.split()
        data.append(list(map(int, [cap, dur, fla, tex, cal])))

# n_ingredients, m_properties
n, m = [len(data), len(data[0])]


# generate all n-arrays that sum to total
def gen_configs(n, total, arr=[]):
    if (len(arr) == n - 1):
        yield arr + [total - sum(arr)]
    else:
        for i in range(total - sum(arr) + 1):
            for ret in gen_configs(n, total, arr + [i]):
                yield ret


# find the best cookie
max_score = cal_score = 0
max_config = cal_config = None
for config in gen_configs(n, 100):
    # compute scores for each ingredient
    scores = [sum([config[i] * data[i][j] for i in range(n)]) for j in range(m)]
    if min(scores) < 0:
        continue

    # multiply all scores, except for the last one (= calories)
    total_score = 1
    for i in range(len(scores) - 1):
        total_score *= scores[i]

    # top overall score?
    if total_score > max_score:
        max_score = total_score
        max_config = config

    # top score under condition score(cal) == 500?
    if scores[-1] == 500 and total_score > cal_score:
        cal_score = total_score
        cal_config = config

print(max_score, max_config)
print(cal_score, cal_config)

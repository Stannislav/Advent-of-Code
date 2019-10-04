#!/usr/bin/env python3

tls_cnt = 0
ssl_cnt = 0
with open("input.txt",'r') as f:
	for line in f:
		bracket = 0
		tls_flag = 0
		ABAs = [[],[]]
		for i in range(len(line)-2):
			if line[i] == '[':
				bracket = 1
			elif line[i] == ']':
				bracket = 0
			else:
				if line[i] == line[i+2] and line[i+1] not in "]":
					ABAs[bracket].append(line[i:i+3])
				if i < len(line)-3 and line[i:i+2] == line[i+3:i+1:-1] and line[i] != line[i+1]:
					if not bracket and tls_flag != 2:
						tls_flag = 1
					else:
						tls_flag = 2
		else: # for exited normally
			if tls_flag == 1:
				tls_cnt += 1
			ABAs[0] = map(lambda s: s[1:]+s[1], ABAs[0])
			if len(set(ABAs[0]).intersection(ABAs[1])) > 0:
				ssl_cnt += 1
print("TLS: {}".format(tls_cnt))
print("SSL: {}".format(ssl_cnt))
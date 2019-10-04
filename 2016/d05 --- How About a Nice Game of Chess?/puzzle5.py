#!/usr/bin/env python3

import hashlib

doorID="ojvtpuvg"

# Puzzle 1
print("Puzzle 1")
i = 0
pwd1 = ""
while True:
	md5hash = hashlib.md5((doorID+str(i)).encode('utf-8')).hexdigest()
	if md5hash[:5] == "00000":
		print("Found: {} ({})".format(md5hash, doorID+str(i)))
		pwd1 += md5hash[5]
	if len(pwd1) == 8:
		break
	i += 1
print("Password 1: {}".format(pwd1))
print("")

# Puzzle 2
print("Puzzle 2")
i = 0
pwd2 = [None]*8
while True:
	md5hash = hashlib.md5((doorID+str(i)).encode('utf-8')).hexdigest()
	if md5hash[:5] == "00000":
		print("Found {} ({})".format(md5hash, doorID+str(i)))
		if md5hash[5] in "01234567" and pwd2[int(md5hash[5])] == None:
			pwd2[int(md5hash[5])] = md5hash[6]
			print("> Current guess: {}".format(''.join(map(str,['_' if x==None else x for x in pwd2]))))
		else:
			print("> Ignored")
	if None not in pwd2:
		break
	i += 1
print("Password 2: {}".format(''.join(map(str,pwd2))))
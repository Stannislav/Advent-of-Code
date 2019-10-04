#!/usr/bin/env python3

import json


def jsum(jdata, ignore_red=False):
    if type(jdata) == str:
        return 0
    elif type(jdata) == dict:
        if ignore_red and "red" in jdata.values():
            return 0
        ret = 0
        for entry in jdata.values():
            ret += jsum(entry, ignore_red)
        return ret
    elif type(jdata) == int:
        return jdata
    elif type(jdata) == list:
        ret = 0
        for entry in jdata:
            ret += jsum(entry, ignore_red)
        return ret


with open("../input/input_12.txt") as f:
    jdata = json.load(f)
    print(jsum(jdata))
    print(jsum(jdata, ignore_red=True))

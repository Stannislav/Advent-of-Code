#!/usr/bin/env python


class wire():
    def __init__(self, s):
        l = s.split()
        self.name = l[-1]
        self.known = False
        self.value = 0
        if any([op in l for op in ["AND", "OR", "LSHIFT", "RSHIFT"]]):
            self.op = l[1]
            self.source = [l[0], l[2]]
        elif "NOT" in l:
            self.op = "NOT"
            self.source = l[1]
        else:
            self.op = "ID"
            self.source = l[0]

    def __str__(self):
        return "Name: {}, op: {}, src: {}".format(self.name, self.op, self.source)

    def sval(self, src):
        if src.isdigit():
            return int(src)
        else:
            return wiredict[src].getvalue()

    def getname(self):
        return self.name

    def getvalue(self):
        if not self.known:
            self.known = True
            if self.op == "ID":
                self.value = self.sval(self.source)
            elif self.op == "RSHIFT":
                self.value = self.sval(self.source[0]) >> self.sval(self.source[1])
            elif self.op == "LSHIFT":
                self.value = self.sval(self.source[0]) << self.sval(self.source[1])
            elif self.op == "NOT":
                self.value = ~self.sval(self.source)
            elif self.op == "OR":
                self.value = self.sval(self.source[0]) | self.sval(self.source[1])
            elif self.op == "AND":
                self.value = self.sval(self.source[0]) & self.sval(self.source[1])
        return self.value


wiredict = {}
with open("../input/input_07.txt", 'r') as f:
    for line in f:
        w = wire(line)
        wiredict[w.getname()] = w

vala = wiredict['a'].getvalue()
print(vala)
for wire in wiredict.values():
    wire.known = False
wiredict['b'].source = str(vala)
print(wiredict['a'].getvalue())

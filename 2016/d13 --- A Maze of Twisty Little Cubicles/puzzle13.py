#!/usr/bin/env python3

class Node():
    def __init__(self, x, y, dist=0, prev=None):
        self.x = x
        self.y = y
        self.dist = dist
        self.prev = prev

def is_wall(x, y):
    if x < 0 or y < 0:
        return True
    n = x*x + 3*x + 2*x*y + y + y*y + inp
    cnt = 0
    while n != 0:
        cnt += n&1
        n >>= 1
    return True if cnt%2 else False

def print_maze_path(path):
    bdry_x = bdry_y = 0
    for point in path:
        if point[0] > bdry_x:
            bdry_x = point[0]
        if point[1] > bdry_y:
            bdry_y = point[1]
    print("__"*(bdry_x+2))
    for y in range(bdry_y+2):
        for x in range(bdry_x+2):
            if is_wall(x, y):
                print("##", end="")
            else:
                print("()" if (x,y) in path else "  ", end="")
        print("|")
    print("^^"*(bdry_x+2))

def BFS(start, end):
    path = [start]
    queue = [Node(start[0], start[1])]
    d_nodes = {start: queue[0]}
    bdry_x = max(start[0], end[0])
    bdry_y = max(start[1], end[1])

    # Do BFS to find the shortest path
    while len(queue) > 0:
        node = queue.pop(0)
        for x, y in [(node.x, node.y+1), (node.x, node.y-1), (node.x+1, node.y), (node.x-1, node.y)]:
            if not is_wall(x, y):
                if (x, y) not in d_nodes:
                    queue.append(Node(x, y, node.dist+1, node))
                    d_nodes[(x, y)] = queue[-1]
                    if (x, y) == end:
                        # Reconstruct the path by going backwards
                        n = d_nodes[(end[0], end[1])]
                        while n.prev != None:
                            path.append((n.x, n.y))
                            n = n.prev
                        # Print maze
                        print_maze_path(path)
                        print(node.dist+1)
                        return
                elif d_nodes[(x, y)].dist > node.dist+1:
                    d_nodes[(x, y)].prev = node
                    d_nodes[(x, y)].dist = node.dist+1

def DFS(max_dist = 50):
    d_nodes = {start: Node(start[0], start[1])}

    # The local recursion function
    def rec(node):
        if node.dist == max_dist:
            return
        for x, y in [(node.x, node.y+1), (node.x, node.y-1), (node.x+1, node.y), (node.x-1, node.y)]:
            if not is_wall(x, y):
                if (x, y) not in d_nodes:
                    d_nodes[(x, y)] = Node(x, y, node.dist+1, node)
                    rec(d_nodes[(x, y)])
                elif d_nodes[(x, y)].dist > node.dist+1:
                    d_nodes[(x, y)].prev = node
                    d_nodes[(x, y)].dist = node.dist+1
                    rec(d_nodes[(x, y)])

    # Do the actual search
    rec(d_nodes[(start[0], start[1])])
    # Put the found path co-ordinates in a list
    path = []
    for node in d_nodes.values():
        path.append((node.x, node.y))
    # Print results
    print_maze_path(path)
    print(len(d_nodes))

# Config
inp=1362
start = (1, 1)
end = (31, 39)
max_dist = 50
# Puzzles
BFS(start, end)
DFS(max_dist)
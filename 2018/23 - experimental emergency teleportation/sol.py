#!/usr/bin/env python2

import os.path
import re
import itertools

class Octahedron(object):
  def __init__(self, center, radius):
    self.center = center
    self.radius = radius

  def in_range(self, point):
    distance = sum(abs(c - p) for c, p in zip(self.center,point))
    return distance <= self.radius

  @staticmethod
  def convert(point):
    axes = [[1, 1, 1],
            [-1, 1, 1],
            [1, -1, 1],
            [-1, -1, 1],
            ]
    return [sum(p * a for p, a in zip(point, axis)) for axis in axes]

  @staticmethod
  def distance(box):
    dist = 0
    for n, x in zip(box.min, box.max):
      if (n < 0) != (x < 0):
        continue
      d = min(abs(n), abs(x))
      if d > dist:
        dist = d
    return dist

  @property
  def box(self):
    return Box(self.convert(self.center[:-1] + [self.center[-1] - self.radius]),
               self.convert(self.center[:-1] + [self.center[-1] + self.radius]))

  def __repr__(self):
    return '%s(%r, %r)' % (self.__class__.__name__, self.center, self.radius)

  def __str__(self):
    return 'pos=<%s>, r=%d' % (','.join(str(c) for c in self.center), self.radius)

class Box(object):
  def __init__(self, min, max):
    self.min = min
    self.max = max

  def __repr__(self):
    return '%s(%r, %r)' % (self.__class__.__name__, self.min, self.max)

  def __repr__(self):
    return '%r - %r' % (self.min, self.max)

  def __nonzero__(self):
    return all(x >= n for n, x in zip(self.min, self.max))

  def __and__(self, other):
    new_min = [max(n1, n2) for n1, n2 in zip(self.min, other.min)]
    new_max = [min(x1, x2) for x1, x2 in zip(self.max, other.max)]
    return self.__class__(new_min, new_max)


def get_input(filename=None):
  if not filename:
    filename = os.path.splitext(os.path.basename(__file__))[0]+'.txt'
  with open(filename) as fp:
    input = fp.read().rstrip()

  return [Octahedron([x, y, z], r) for x, y, z, r in (map(int, re.search(r'^pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(-?\d+)$', line).groups()) for line in input.split('\n'))]


def part1(bots):
  strongest = max(bots, key=lambda bot: bot.radius)
  count = 0
  for bot in bots:
    count += strongest.in_range(bot.center)
  return count

def part2(bots):
  bots = [bot.box for bot in bots]

  intersecting = []
  for box in bots:
    count = 0
    for box2 in bots:
      if box & box2:
        count += 1
    intersecting.append(count)

  for n, count in enumerate(sorted(intersecting, reverse=True)):
    if n + 1 >= count:
      break

  distance = None
  for n in xrange(count, 0, -1):
    print 'n=%d' % n
    possible_indexes = [i for i, count in enumerate(intersecting) if count >= n]
    for indexes in itertools.combinations(possible_indexes, n):
      box = bots[indexes[0]]
      for index in indexes[1:]:
        box &= bots[index]
        if not box:
          break
      else:
        dist = Octahedron.distance(box)
        ## print 'n=%d, boxes=%r, distance=%d' % (n, indexes, dist)
        if distance is None or dist < distance:
          distance = dist
    if distance is not None:
      return distance


if __name__ == '__main__':
  from optparse import OptionParser
  parser = OptionParser(usage='%prog [options] [<input.txt>]')
  options, args = parser.parse_args()
  input = get_input('23_input.txt')
  print part1(input)
  print part2(input)

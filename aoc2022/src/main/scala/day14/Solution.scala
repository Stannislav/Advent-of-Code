package day14

import Math.{min, max}
import io.Source

object Solution {
  def main(args: Array[String]): Unit = {
    val rocks = parseInput(Source.fromResource("input/14.txt"))

    println(s"Part 1: ${part1(rocks)}")
    println(s"Part 2: ${part2(rocks)}")
  }

  def parseInput(stream: Source): Set[Point] = {

    def fillLine(p1: Point, p2: Point): Seq[Point] = {
      if (p1.x == p2.x) {
        for (y <- min(p1.y, p2.y) to max(p1.y, p2.y)) yield Point(p1.x, y)
      } else if (p2.y == p2.y) {
        for (x <- min(p1.x, p2.x) to max(p1.x, p2.x)) yield Point(x, p1.y)
      } else {
        throw Exception(s"Invalid line corners: $p1, $p2")
      }
    }

    stream
      .getLines()
      .filter(_.nonEmpty)
      .map(_.split(" -> ").map(Point.fromString))
      .flatMap(corners => (corners zip corners.tail).map(fillLine))
      .map(_.toSet)
      .reduce(_ | _)
  }

  def part1(rocks: Set[Point]): Int = evolve(rocks, withFloor = false).size - rocks.size

  def part2(rocks: Set[Point]): Int = evolve(rocks, withFloor = true).size - rocks.size

  def evolve(rocks: Set[Point], withFloor: Boolean): Set[Point] = {
    val maxY = rocks.map(_.y).max + 1

    def addRock(state: Set[Point]): Set[Point] = {
      var pos = Point(500, 0)
      var done = state.contains(pos)

      while (!done) {
        if (!state.contains(pos.down())) {
          pos = pos.down()
        } else if (!state.contains(pos.downLeft())) {
          pos = pos.downLeft()
        } else if (!state.contains(pos.downRight())) {
          pos = pos.downRight()
        } else {
          done = true
        }
        if pos.y == maxY then done = true
      }

      if (!withFloor && pos.y == maxY) state else state + pos
    }

    var state = Set.from(rocks)
    var prevSize = state.size
    while ( {
      prevSize = state.size
      state = addRock(state)
      state.size > prevSize
    }) {}

    state
  }
}

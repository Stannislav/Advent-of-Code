package day14

import Math.{min, max}

object Solution {
  def main(args: Array[String]): Unit = {
    val rocks = parseInput(io.Source.fromResource("input/14.txt"))

    println(s"Part 1: ${part1(rocks)}")
    println(s"Part 2: ${part2(rocks)}")
  }

  def parseInput(stream: io.Source): Set[Point]= {

    def fillLine(p1: Point, p2: Point): Seq[Point] = {
      if (p1.x == p2.x) {
        for(y <- min(p1.y, p2.y) to max(p1.y, p2.y)) yield Point(p1.x, y)
      } else if(p2.y == p2.y) {
        for(x <- min(p1.x, p2.x) to max(p1.x, p2.x)) yield Point(x, p1.y)
      } else {
        throw Exception(s"Invalid line corners: $p1, $p2")
      }
    }

    stream
      .getLines()
      .filter(_.nonEmpty)
      .map(_.split(" -> ").map(_.split(",").map(_.toInt)).map {
        case Array(x: Int, y: Int) => Point(x, y)
        case invalid@_ => throw Exception(s"Invalid point: ${invalid.mkString(",")}")
      })
      .flatMap(corners => (corners zip corners.tail).map(fillLine))
      .map(_.toSet)
      .reduce(_ | _)
  }

  def part1(rocks: Set[Point]): Int = {
    val maxY = rocks.map(_.y).max
    var state = Set.from(rocks)
    var prevSize = state.size

    def addRock(state: Set[Point]): Set[Point] = {
      var pos = Point(500, 0)
      var stopped = false
      var leaked = false

      while (!stopped && !leaked) {
        if (!state.contains(pos.down())) {
          pos = pos.down()
        } else if(!state.contains(pos.downLeft())) {
          pos = pos.downLeft()
        } else if(!state.contains(pos.downRight())) {
          pos = pos.downRight()
        } else {
          stopped = true
        }
        if pos.y > maxY then leaked = true
      }

      if (stopped) state + pos else state
    }

    while ({
      prevSize = state.size
      state = addRock(state)
      state.size > prevSize
    }) {}

    state.size - rocks.size
  }

  def part2(rocks: Set[Point]): Int = {
    val maxY = rocks.map(_.y).max + 1
    var state = Set.from(rocks)
    var prevSize = state.size

    def addRock(state: Set[Point]): Set[Point] = {
      var pos = Point(500, 0)
      var stopped = state.contains(pos)

      while (!stopped) {
        if (!state.contains(pos.down())) {
          pos = pos.down()
        } else if(!state.contains(pos.downLeft())) {
          pos = pos.downLeft()
        } else if(!state.contains(pos.downRight())) {
          pos = pos.downRight()
        } else {
          stopped = true
        }
        if pos.y == maxY then stopped = true
      }

      if (stopped) state + pos else state
    }

    while ({
      prevSize = state.size
      state = addRock(state)
      state.size > prevSize
    }) {}

    state.size - rocks.size
  }
}


case class Point(x: Int, y: Int) {
  def down(): Point = this.copy(y = y + 1)
  def downLeft(): Point = this.copy(x = x - 1, y = y + 1)
  def downRight(): Point = this.copy(x = x + 1, y = y + 1)
}

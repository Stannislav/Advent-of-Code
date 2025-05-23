package day14

import Math.{min, max}

object Solution {
  def main(args: Array[String]): Unit = {
    val rocks = parseInput(io.Source.fromResource("input/14.txt"))
    println(rocks)
    println(rocks.size)
    val maxY = rocks.map(_.y).max
    println(s"Max y: $maxY")
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
}


case class Point(x: Int, y: Int)

package day14

object Solution {
  def main(args: Array[String]): Unit = {
    val input = parseInput(io.Source.fromResource("input/14.txt"))
  }

  def parseInput(stream: io.Source): Set[Point]= {

    def fillLines(corners: Array[Point]): Set[Point] = {
      Set(Point(0, 0))
    }

    stream
      .getLines()
      .filter(_.nonEmpty)
      .map(_.split(" -> ").map(_.split(",").map(_.toInt)).map {
        case Array(x: Int, y: Int) => Point(x, y)
        case invalid@_ => throw Exception(s"Invalid point: ${invalid.mkString(",")}")
      })
      .map(fillLines)
      .reduce(_ | _)
  }
}


case class Point(x: Int, y: Int)

object Day04 {
  def main(args: Array[String]): Unit = {
    val lines = util.Using.resource(io.Source.fromFile("2022/input/04.txt")) { _
      .getLines
      .map(_.split("[-,]").map(_.toInt).toList)
      .toList
    }

    val part1 = lines.count {
      case List(a, b, x, y) => (a <= x && b >= y) || (x <= a && y >= b)
      case _ => throw new Exception("Unexpected line")
    }
    println(s"Part 1: $part1")

    val part2 = lines.count {
      case List(a, b, x, y) => (a <= x && x <= b) || (x <= a && a <= y)
      case _ => throw new Exception("Unexpected line")
    }
    println(s"Part 2: $part2")
  }
}

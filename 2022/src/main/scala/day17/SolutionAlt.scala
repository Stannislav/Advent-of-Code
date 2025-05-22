// TODO: Clean up, merge into Solution.
package day17

type Rock = List[Int]


object SolutionAlt {
  def main(args: Array[String]): Unit = {
    def ops = util.Using.resource(io.Source.fromResource("input/17.txt")) {
      _.mkString.strip.toList.map {
        case '<' => ((x: Int) => x << 1)
        case '>' => ((x: Int) => x >> 1)
        case c@_ => throw Exception(s"Unknown command: $c")
      }
    }

    parseRock(".#.\n###\n.#.") foreach { num => println(num.toBinaryString)}
    """#
      |#
      |#
      |#
      |""".stripMargin
  }

  def parseRock(drawing: String): List[Int] = {
//    def step(x: Int, c: Char): Int = x << 1 | (if (c == '#') 1 else 0)
//
////    drawing.strip().lines.map(_.foldLeft(0)(step))

    def makeBinary(s: String): String =
      s.replace('#', '1').replace('.', '0')

    drawing
      .strip
      .linesIterator
      .map(line => Integer.parseInt(makeBinary(line), 2))
      .toList
  }
}

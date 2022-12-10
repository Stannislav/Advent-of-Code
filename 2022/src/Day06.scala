import util.control.Breaks.*


object Day06 {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromFile("2022/input/06.txt")) {_.mkString}

    println(s"Part 1: ${solve(input, 4)}")
    println(s"Part 2: ${solve(input, 14)}")
  }

  def solve(input: String, len: Int): Int = {
    for (received <- Range(len, input.length)) {
      if (input.slice(received - len, received).toSet.size == len)
        return received
    }

    -1
  }
}

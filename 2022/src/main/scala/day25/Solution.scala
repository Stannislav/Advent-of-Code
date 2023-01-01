package day25

object Solution {
  def main(args: Array[String]): Unit = {
    val snafus = util.Using.resource(io.Source.fromResource("input/25.txt")) {
      _.getLines.map(SNAFU.parse).toList
    }

    println(s"Part 1: ${snafus.reduce(_ + _)}")
  }
}

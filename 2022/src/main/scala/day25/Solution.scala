package day25

object Solution {
  def main(args: Array[String]): Unit = {
    val source = io.Source.fromResource("input/25.txt")
    val snafus = try source.getLines.map(SNAFU.parse).toList finally source.close
    println(s"Part 1: ${snafus.reduce(_ + _)}")
  }
}

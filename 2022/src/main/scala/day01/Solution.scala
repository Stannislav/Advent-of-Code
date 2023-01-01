package day01

object Solution {
  def main(args: Array[String]): Unit = {
    val calories = util.Using.resource(io.Source.fromResource("input/01.txt")) { _
        .mkString
        .split("\n\n")
        .map(block => block.split('\n').map(_.toInt).sum)
    }

    println(f"Part 1: ${calories.max}")
    println(f"Part 2: ${calories.sorted.reverse.slice(0, 3).sum}")
  }
}

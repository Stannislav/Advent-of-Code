import scala.io.Source

object Day01 {
  def main(args: Array[String]): Unit = {
    val calories = Source
      .fromFile("2022/input/01.txt")
      .mkString
      .split("\n\n")
      .map(block => block.split('\n').map(_.toInt).sum)

    println(f"Part 1: ${calories.max}")
    println(f"Part 2: ${calories.sorted.reverse.slice(0, 3).sum}")
  }
}

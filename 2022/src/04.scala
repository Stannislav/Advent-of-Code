import scala.io.Source


@main
def main(): Unit = {
  val lines = Source
    .fromFile("input/04.txt")
    .getLines
    .map(_.split("[-,]").map(_.toInt).toList)
    .toList

  val part1 = lines.filter {
    case List(a, b, x, y) => (a <= x && b >= y) || (x <= a && y >= b)
    case _ => throw new Exception("Unexpected line")
  }.length
  println(s"Part 1: $part1")

  val part2 = lines.filter {
    case List(a, b, x, y) => (a <= x && x <= b) || (x <= a && a <= y)
    case _ => throw new Exception("Unexpected line")
  }.length
  println(s"Part 2: $part2")
}
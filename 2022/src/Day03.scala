object Day03 {
  def main(args: Array[String]): Unit = {
    val lines = util.Using.resource(io.Source.fromFile("2022/input/03.txt")) { _
      .getLines
      .map(line => line.map(priority).toList)
      .toList
    }

    val part1 = lines
      .map(line => {
        val first = line.slice(0, line.length / 2).toSet
        val second = line.slice(line.length / 2, line.length).toSet
        first.intersect(second).last
      })
      .sum
    println(s"Part 1: $part1")

    val part2 = Range(0, lines.length, 3)
      .map(idx => {
        lines(idx)
          .toSet
          .intersect(lines(idx + 1).toSet)
          .intersect(lines(idx + 2).toSet)
          .last
      })
      .sum
    println(s"Part 2: $part2")
  }

  private def priority(c: Char): Int = {
    if ('a' <= c && c <= 'z')
      c - 'a' + 1
    else
      c - 'A' + 27
  }
}

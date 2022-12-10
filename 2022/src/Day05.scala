import scala.collection.mutable

object Day05 {
  def main(args: Array[String]): Unit = {
    // Read input
    val (stacks, instructions) = util.Using.resource(io.Source.fromFile("2022/input/05.txt")) { _
      .mkString
      .split("\n\n")
      match {
        case Array(a: String, b: String) => (parseStacks(a), parseInstructions(b))
        case _ => throw Exception("Badly formatted input")
      }
    }

    // Part 1
    val part1 = solve(cloneStacks(stacks), instructions, one_by_one = true)
    println(s"Part 1: $part1")

    // Part 2
    val part2 = solve(cloneStacks(stacks), instructions, one_by_one = false)
    println(s"Part 2: $part2")
  }


  private def parseStacks(input: String): List[mutable.Stack[Char]] = {
    val lines = input.linesIterator.toList
    val height = lines.length - 1 // last row = labels
    val width = lines.map(_.strip.length).max

    Range(1, width, 4).map(col => {
      val stack: mutable.Stack[Char] = mutable.Stack()
      Range(0, height).reverse.foreach(row => {
        val c = lines(row)(col)
        if (c != ' ')
          stack.push(c)
      })
      stack
    }).toList
  }

  private def parseInstructions(input: String): List[(Int, Int, Int)] = {
    input
      .linesIterator
      .map {
        case s"move $n from $from to $to" => (n.toInt, from.toInt - 1, to.toInt - 1)
        case line: String => throw Exception(s"Malformed instruction: $line")
      }
      .toList
  }

  private def solve(
    stacks: List[mutable.Stack[Char]],
    instructions: List[(Int, Int, Int)],
    one_by_one: Boolean,
  ): String = {
    for ((n, from, to) <- instructions)
      val took = (1 to n).map(_ => stacks(from).pop)
      if (one_by_one)
        stacks(to).pushAll(took)
      else
        stacks(to).pushAll(took.reverse)

    stacks.map(stack => stack.top).mkString
  }

  private def cloneStacks(stacks: List[mutable.Stack[Char]]) = {
    stacks.map(s => mutable.Stack().pushAll(s.reverse))
  }
}

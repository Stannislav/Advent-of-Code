import scala.util.Using

object Day10 {
  def main(args: Array[String]): Unit = {
    // Read and transform the input: list of (dx, nCycle)
    val cycles = Using.resource(io.Source.fromFile("2022/input/10.txt")) {
      _
        .getLines
        .flatMap(_ match {
          case "noop" => List(0)
          case s"addx $n" => List(0, n.toInt)
          case _ => throw Exception("Invalid line")
        })
        .zipWithIndex
        .toList
    }

    // Part 1
    var x = 1
    var strength = 0
    cycles
      .map((dx, cycle) => (dx, cycle + 1))
      .foreach((dx, cycle) => {
        if ((cycle - 20) % 40 == 0)
          strength += x * cycle
        x += dx
      })
    println(s"Part 1: $strength")

    // Part 2
    x = 1
    val crt = cycles
      .map((dx, cycle) => {
        val lit = (x - 1 to x + 1).contains(cycle % 40)
        x += dx
        if (lit) "##" else "  "
      })
      .mkString
    println("Part 2:")
    Range(0, crt.length, 80).foreach(pos => println(crt.slice(pos, pos + 80)))
  }
}

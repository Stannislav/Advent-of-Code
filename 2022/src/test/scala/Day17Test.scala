import org.scalatest.funsuite.AnyFunSuite

import day17.Solution

class Day17Test extends AnyFunSuite {
  private val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>\n"

  test("Solution.parseInput") {
    val expected = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    assert(Solution.parseInput(io.Source.fromString(input)) === expected)
  }
}

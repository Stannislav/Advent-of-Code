import org.scalatest.funsuite.AnyFunSuite

import day17.SolutionNew

class Day17Test extends AnyFunSuite {
  private val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

  test("Solution.parseInput") {
    val expected = Array(
      '>', '>', '>', '<', '<', '>', '<', '>', '>', '<', '<', '<', '>', '>', '<', '>', '>', '>', '<', '<',
      '<', '>', '>', '>', '<', '<', '<', '>', '<', '<', '<', '>', '>', '<', '>', '>', '<', '<', '>', '>',
    )
    assert(SolutionNew.parseInput(io.Source.fromString(input)) === expected)
  }
}

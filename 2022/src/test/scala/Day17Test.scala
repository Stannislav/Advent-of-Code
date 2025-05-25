import org.scalatest.funsuite.AnyFunSuite
import io.Source
import day17.{ChamberSimulation, Solution}

class Day17Test extends AnyFunSuite {
  private val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>\n"

  test("Solution.parseInput") {
    val expected = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    assert(Solution.parseInput(Source.fromString(input)) === expected)
  }

  test("Solution.part1") {
    val jets = Solution.parseInput(Source.fromString(input))
    val (heights, loopStart, loopHeightGain) = ChamberSimulation.run(jets)
    val solution = Solution(heights, loopStart, loopHeightGain)
    assert(solution.part1 === 3068)
  }

  test("Solution.part2") {
    val jets = Solution.parseInput(Source.fromString(input))
    val (heights, loopStart, loopHeightGain) = ChamberSimulation.run(jets)
    val solution = Solution(heights, loopStart, loopHeightGain)
    assert(solution.part2 === 1514285714288L)
  }
}

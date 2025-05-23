import org.scalatest.funsuite.AnyFunSuite
import day14.{Point, Solution}

class Day14Test extends AnyFunSuite {
  private val input =
    """
      |498,4 -> 498,6 -> 496,6
      |503,4 -> 502,4 -> 502,9 -> 494,9
      |""".stripMargin

  test("Solution.parseInput") {
    val expected = Set(
      Point(498, 4), Point(498, 5),Point(498, 6), Point(497, 6), Point(496, 6),
      Point(503, 4), Point(502, 4), Point(502, 5), Point(502, 6), Point(502, 7),
      Point(502, 8), Point(502, 9), Point(501, 9), Point(500, 9), Point(499, 9),
      Point(498, 9), Point(497, 9), Point(496, 9), Point(495, 9), Point(494, 9),
    )
    assert(Solution.parseInput(io.Source.fromString(input)) === expected)
  }
}

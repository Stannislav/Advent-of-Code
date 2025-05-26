import org.scalatest.funsuite.AnyFunSuite
import io.Source
import day19.Solution

class Day19Test extends AnyFunSuite {
  //Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
  //Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
  test("Solution.parseInput") {
    val expected = Seq(
      Array(
        Array(4, 0, 0, 0),
        Array(2, 0, 0, 0),
        Array(3, 14, 0, 0),
        Array(2, 0, 7, 0),
      ),
      Array(
        Array(2, 0, 0, 0),
        Array(3, 0, 0, 0),
        Array(3, 8, 0, 0),
        Array(3, 0, 12, 0),
      )
    )
    val parsed = Solution.parseInput(Source.fromResource("19.txt"))
    assert(parsed.length === expected.length)
    (parsed zip expected).foreach { (a, b) => assert(a.length === b.length)}
    (parsed zip expected).foreach { (a, b) =>
      (a zip b).foreach { (x, y) => assert(x.toSeq === y.toSeq)}
    }
  }

  test("Solution.runOptimally") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.runOptimally(blueprints(0)) === 9)
    assert(Solution.runOptimally(blueprints(1)) === 12)
  }

  test("Solution.part1") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.part1(blueprints) === 33)
  }
}

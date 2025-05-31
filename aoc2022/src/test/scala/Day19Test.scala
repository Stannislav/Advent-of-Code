import org.scalatest.funsuite.AnyFunSuite
import io.Source
import day19.Solution

class Day19Test extends AnyFunSuite {
  //Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
  //Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
  test("Solution.parseInput") {
    val expected = Seq(
      Vector(
        Vector(4, 0, 0, 0),
        Vector(2, 0, 0, 0),
        Vector(3, 14, 0, 0),
        Vector(2, 0, 7, 0),
      ),
      Vector(
        Vector(2, 0, 0, 0),
        Vector(3, 0, 0, 0),
        Vector(3, 8, 0, 0),
        Vector(3, 0, 12, 0),
      )
    )
    assert(Solution.parseInput(Source.fromResource("19.txt")) === expected)
  }

  test("Solution.run(blueprint, 24)") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.run(blueprints(0), 24) === 9)
    assert(Solution.run(blueprints(1), 24) === 12)
  }

  test("Solution.run(blueprint, 32)") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.run(blueprints(0), 32) === 56)
    assert(Solution.run(blueprints(1), 32) === 62)
  }

  test("Solution.part1") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.part1(blueprints) === 33)
  }

  test("Solution.part2") {
    val blueprints = Solution.parseInput(Source.fromResource("19.txt"))
    assert(Solution.part2(blueprints) === 56 * 62)
  }
}

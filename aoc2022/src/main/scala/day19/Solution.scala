package day19

import io.Source

object Solution {
  def main(args: Array[String]): Unit = {
    val blueprints = Solution.parseInput(Source.fromResource("input/19.txt"))
    println(s"Part 1: ${part1(blueprints)}")
    println(s"Part 2: ${part2(blueprints)}")
  }

  def parseInput(stream: Source): Seq[Vector[Vector[Int]]] = {
    stream
      .getLines()
      .map(parseBlueprint)
      .toVector
  }

  private def parseBlueprint(line: String): Vector[Vector[Int]] = {
    line.match {
      case s"Blueprint $i: Each ore robot costs $oreOre ore. Each clay robot costs $clayOre ore. Each obsidian robot costs $obsidianOre ore and $obsidianClay clay. Each geode robot costs $geodeOre ore and $geodeObsidian obsidian." =>
        Vector(
          Vector(oreOre.toInt, 0, 0, 0),
          Vector(clayOre.toInt, 0, 0, 0),
          Vector(obsidianOre.toInt, obsidianClay.toInt, 0, 0),
          Vector(geodeOre.toInt, 0, geodeObsidian.toInt, 0),
        )
      case _ => throw Exception(s"Can't parse line: $line")
    }
  }

  def part1(blueprints: Seq[Vector[Vector[Int]]]): Int = {
    blueprints
      .zipWithIndex
      .map((blueprint, idx) => {(idx + 1) * run(blueprint, 24)})
      .sum
  }

  def part2(blueprints: Seq[Vector[Vector[Int]]]): Int = {
    blueprints
      .slice(0, 3)
      .map(run(_, 32))
      .product
  }

  def run(blueprint: Vector[Vector[Int]], nMinutes: Int): Int = {
    var states = Set(State(Vector(1, 0, 0, 0), Vector(0, 0, 0, 0)))
    for(minute <- 1 to nMinutes) {
      val nextStates = states.flatMap { _.step(blueprint)}
      // The heuristic optimisation below makes the program finish in reasonable time:
      // At any given minute only keep the states with the maximal number of
      // geode-cracking robots.
      // This heuristic seems to be wrong: the examples used in unit tests don't pass.
      // TODO: Find a better optimisation which also works for test examples.
      val maxGeodeRobots = nextStates.map(_.robots(3)).max
      states = nextStates.filter(_.robots(3) == maxGeodeRobots)
    }
    states.map(_.resources(3)).max
  }
}

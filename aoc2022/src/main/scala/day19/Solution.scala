// TODO: Finish solving.
package day19

import io.Source

object Solution {
  def main(args: Array[String]): Unit = {
//    val input = util.Using.resource(io.Source.fromResource("input/19-debug.txt")) {
//      _.getLines.map(Blueprint.fromString).toList
//    }
//    println(input.map(_.run()))
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
      .map((blueprint, idx) => (idx + 1) * runOptimally(blueprint))
      .sum
  }

  def runOptimally(blueprint: Vector[Vector[Int]]): Int = {
    0
  }
}

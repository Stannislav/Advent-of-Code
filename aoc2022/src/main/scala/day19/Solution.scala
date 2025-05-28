// TODO: Finish solving.
package day19

import io.Source

object Solution {
  def main(args: Array[String]): Unit = {
//    val input = util.Using.resource(io.Source.fromResource("input/19-debug.txt")) {
//      _.getLines.map(Blueprint.fromString).toList
//    }
//    println(input.map(_.run()))
    val blueprints = parseInput(Source.fromResource("input/19.txt"))
    println(s"Part 1: ${part1(blueprints)}")
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
      .map((blueprint, idx) => {
        println(s"Processing blueprint ${idx + 1}/${blueprints.length}.")
        (idx + 1) * runOptimally(blueprint)
      })
      .sum
  }

  def runOptimally(blueprint: Vector[Vector[Int]]): Int = {
    var states = Set(State(Vector(1, 0, 0, 0), Vector(0, 0, 0, 0)))
    val seen = collection.mutable.Set[State]()

    for(i <- 1 to 24) {
//      println(s"Current length: ${state.size}")
//      println(s"  $state")
      val nextStates = states.flatMap { _.step(blueprint).filter(!seen.contains(_))}
      nextStates.foreach(seen.add)
      // for the same resources pick the state with the best robots state
      states = nextStates
        .groupBy(_.resources)
        .values
        .map { items =>
          if (items.exists(_.robots(3) != 0)) {
            val max = items.map(_._1(3)).max
            items.filter(_.robots(3) == max)
          } else if (items.exists(_._1(2) != 0)) {
            val max = items.map(_.robots(2)).max
            items.filter(_.robots(2) == max)
          } else if (items.exists(_._1(1) != 0)) {
            val max = items.map(_._1(1)).max
            items.filter(_.robots(1) == max)
          } else {
            val max = items.map(_._1(0)).max
            items.filter(_.robots(0) == max)
          }
        }
        .reduce(_ | _)
    }
    println(s"Total states at the end: ${states.size}")
    states.map(_._2(3)).max
  }
}

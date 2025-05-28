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
    var state = Set((Vector(1, 0, 0, 0), Vector(0, 0, 0, 0)))
    val seen = collection.mutable.Set[(Vector[Int], Vector[Int])]()

    def step(nRobots: Vector[Int], nResources: Vector[Int]): Seq[(Vector[Int], Vector[Int])] = {
      Seq(
        (Vector(0, 0, 0, 0), nResources),
        (Vector(1, 0, 0, 0), (nResources zip blueprint(0)).map(_ - _)),
        (Vector(0, 1, 0, 0), (nResources zip blueprint(1)).map(_ - _)),
        (Vector(0, 0, 1, 0), (nResources zip blueprint(2)).map(_ - _)),
        (Vector(0, 0, 0, 1), (nResources zip blueprint(3)).map(_ - _)),
      )
        .filter(_._2.forall {_ >= 0})
        .map { (robotBuilds, resources) =>
          ((nRobots zip robotBuilds).map(_ + _), (resources zip nRobots).map(_ + _))
        }
        .filter(!seen.contains(_))
    }

    for(i <- 1 to 24) {
//      println(s"Current length: ${state.size}")
//      println(s"  $state")
      val nextState = state.flatMap { (nRobots, nResources) => step(nRobots, nResources) }
      nextState.foreach(seen.add)
      // for the same resources pick the state with the best robots state
      state = nextState
        .groupBy(_._2)
        .values
        .map { items =>
          if (items.exists(_._1(3) != 0)) {
            val max = items.map(_._1(3)).max
            items.filter(_._1(3) == max)
          } else if (items.exists(_._1(2) != 0)) {
            val max = items.map(_._1(2)).max
            items.filter(_._1(2) == max)
          } else if (items.exists(_._1(1) != 0)) {
            val max = items.map(_._1(1)).max
            items.filter(_._1(1) == max)
          } else {
            val max = items.map(_._1(0)).max
            items.filter(_._1(0) == max)
          }
        }
        .reduce(_ | _)
    }
    println(s"Total states at the end: ${state.size}")
    state.map(_._2(3)).max
  }
}

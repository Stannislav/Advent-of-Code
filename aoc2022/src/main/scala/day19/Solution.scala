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

  def run(blueprint: Vector[Vector[Int]]): Int = {

    def dfs(state: State, stepsDone: Int): Int = if(stepsDone == 24) state.resources(3) else {
      val nextStates = state
        .nextStates(blueprint)
        .sortWith { (s1, s2) =>
          (s1.robots zip s2.robots).map(_ - _).filter(_ != 0).reverse.head > 0
        }

      0
    }

    dfs(State(Vector(1, 0, 0, 0), Vector(0, 0, 0, 0)), 0)
  }

  def runOptimally(blueprint: Vector[Vector[Int]]): Int = {
    val states = collection.mutable.Map[Int, collection.mutable.Map[State, Int]]()
    val seenAt = collection.mutable.Map[State, Int]().withDefaultValue(25)

    def getGeodesFromState(state: State, stepsDone: Int): Int = states
      .getOrElseUpdate(stepsDone, { collection.mutable.Map[State, Int]() })
      .getOrElseUpdate(state, {
        seenAt(state) = stepsDone
        if (stepsDone == 24) {
//          println(s"Current states (${states.size})")
//          for ((k, v) <- states) {
//            println(s"states at step $k: ${v.size}")
//          }
//          println(s"Seeing ${state.resources(3)}")
//          println("All states at i=24:")
//          for ((k, v) <- states(24)) {
//            println(k)
//          }
          for (i <- 1 to 24) {
            print(s"${states(i).size} | ")
          }
          println()
          state.resources(3)
        } else {
          state
            .nextStates(blueprint)
            .filter { seenAt(_) > stepsDone + 1 }
            .map { getGeodesFromState(_, stepsDone + 1)}
//            .max
            .maxOption
            .getOrElse { 0 }
        }
    })

    getGeodesFromState(State(Vector(1, 0, 0, 0), Vector(0, 0, 0, 0)), 0)
  }
}

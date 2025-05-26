package day19

import scala.collection.mutable

val ORE = 0
val CLAY = 1
val OBSIDIAN = 2
val GEODE = 3
val NAMES = List("ore", "clay", "obsidian", "geode")

object Blueprint {
  def fromString(line: String): Blueprint = {
    line.match {
      case s"Blueprint $i: Each ore robot costs $oreOre ore. Each clay robot costs $clayOre ore. Each obsidian robot costs $obsidianOre ore and $obsidianClay clay. Each geode robot costs $geodeOre ore and $geodeObsidian obsidian." =>
        new Blueprint(
          i.toInt,
          List(
            List(oreOre.toInt, 0, 0, 0),
            List(clayOre.toInt, 0, 0, 0),
            List(obsidianOre.toInt, obsidianClay.toInt, 0, 0),
            List(geodeOre.toInt, 0, geodeObsidian.toInt, 0),
          )
        )
    }
  }
}
class Blueprint private(i: Int, robotCosts: List[List[Int]]) {

  private def update(resources: List[Int], robots: List[Int], useResources: List[Int], buildRobots: List[Int]): (List[Int], List[Int]) = {
    val newResources = Range(0, 4).map(kind => resources(kind) - useResources(kind) + robots(kind)).toList
    val newRobots = Range(0, 4).map(kind => robots(kind) + buildRobots(kind)).toList

    (newResources, newRobots)
  }

  def run(): Int = {
    val states = Range(0, 24).map(* => mutable.Set[(List[Int], List[Int])]()).toList

    states.head.addOne((List(0, 0, 0, 0), List(1, 0, 0, 0))) // (resources, robots)
    Range(0, 23).foreach { minute =>
      println(s"Minute ${minute + 1}, looking at ${states(minute).size} states.")
      states(minute) foreach { (resources, robots) =>
        Range(0, 4).foreach(robotKind =>
          if (Range(0, 4).forall(resourceKind => resources(resourceKind) >= robotCosts(robotKind)(resourceKind))) {
            states(minute + 1).addOne(update(resources, robots, robotCosts(robotKind), Range(0, 4).map(idx => if (idx == robotKind) 1 else 0).toList))
          }
        )
        states(minute + 1).addOne(update(resources, robots, List(0, 0, 0, 0), List(0, 0, 0, 0)))
      }
    }

    states.last.map((resources, *) => resources(GEODE)).max
  }
}

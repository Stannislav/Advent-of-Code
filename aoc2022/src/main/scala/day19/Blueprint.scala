package day19

import scala.collection.mutable

val ORE = 0
val CLAY = 1
val OBSIDIAN = 2
val GEODE = 3
val NAMES = List("ore", "clay", "obsidian", "geode")
class Blueprint(i: Int, robotCosts: List[List[Int]]) {

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

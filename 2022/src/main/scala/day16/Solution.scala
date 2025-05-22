// TODO: Clean up code, optimise part 2.
package day16

import scala.collection.mutable

object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/16.txt")) { _
      .getLines
      .map {
        case s"Valve $source has flow rate=$rate; tunnels lead to valves $targets" =>
          (source, rate.toInt, targets.split(", ").toList)
        case s"Valve $source has flow rate=$rate; tunnel leads to valve $target" =>
          (source, rate.toInt, List(target))
        case s@_ => throw Exception(s"Unknown line: $s")
      }
      .toList
    }

    val rates = input.map((source, rate, targets) => (source, rate)).toMap
    val goodValves = rates.filter((source, rate) => rate > 0).map((source, rate) => source).toSet
    val graph = input.map((source, rate, targets) => (source, targets)).toMap
    val distances = graph.map((start, targets) => (start, dist(start, graph)))

    println(s"Part 1: ${solve(goodValves, distances, rates, 30)}")
    val part2 = partitions(goodValves)
      .map((valves1, valves2) =>
        solve(valves1, distances, rates, 26) + solve(valves2, distances, rates, 26)
      )
      .max
    println(s"Part 2: $part2")

  }

  def partitions(set: Set[String]): Set[(Set[String], Set[String])] = {
    val indexed = set.toList
    val subsets = mutable.Set[(Set[String], Set[String])]()

    for (len <- 1 to indexed.length) {
      for (ids <- Range(0, indexed.length) combinations len) {
        val subset = ids.map(idx => indexed(idx)).toSet
        subsets.add((subset, set -- subset))
      }
    }

    subsets.toSet
  }

  def solve(
    valves: Set[String],
    distances: Map[String, Map[String, Int]],
    rates: Map[String, Int],
    time: Int,
  ): Int = {
    var solution = 0
    val toSee = valves.to(mutable.Set)
    val sortedDistances = distances
      .map(
        (source, distances) => (source, distances
          .filter((valve, *) => valves.contains(valve))
          .toList
          .sortBy((valve, *) => rates(valve))
          .reverse
        )
      )

    def dfs(current: String, time: Int, pressure: Int = 0): Unit = {
      if (pressure > solution)
        solution = pressure
      if (pressure + toSee.map(rates(_)).sum * time > solution)
        for ((target, dist) <- sortedDistances(current)) {
          if (toSee.contains(target) && dist + 1 <= time) {
            toSee -= target
            dfs(target, time - dist - 1, pressure + rates(target) * (time - dist - 1))
            toSee += target
          }
        }
    }

    dfs("AA", time)
    solution
  }

  def dist(start: String, graph: Map[String, List[String]]): Map[String, Int] = {
    val q = mutable.Queue((start, 1))
    val distances = graph.map((key, dest) => (key, graph.size)).to(mutable.Map)
    while (q.nonEmpty) {
      val (node, d) = q.dequeue
      graph(node) foreach { target => {
          if (d < distances(target)) {
            distances(target) = d
            q.enqueue((target, d + 1))
          }
        }
      }
    }
    distances.remove(start)
    distances.toMap
  }
}

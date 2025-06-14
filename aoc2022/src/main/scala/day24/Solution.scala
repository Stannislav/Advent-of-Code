package day24

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source

object Solution {
  def main(args: Array[String]): Unit = {
    val (start, end, blizzards) = parseInput(Source.fromResource("input/24.txt"))
    println(s"Part 1: ${part1(start, end, blizzards)}")
    println(s"Part 2: ${part2(start, end, blizzards)}")
  }

  def part1(start: (Int, Int), end: (Int, Int), blizzards: Blizzards): Int = travel(start, end, blizzards)

  def part2(start: (Int, Int), end: (Int, Int), blizzards: Blizzards): Int = {
    val t1 = travel(start, end, blizzards)
    val t2 = travel(end, start, blizzards, t1)
    travel(start, end, blizzards, t2)
  }

  private def travel(start: (Int, Int), end: (Int, Int), blizzards: Blizzards, startTime: Int = 0): Int = {
    // Remember the shortest time taken to reach any given point.
    // The blizzards are moving, so it's possible that one needs to
    // travel through the same point with a different blizzard configuration.
    // Therefore, we need to save the shortest times for all possible blizzard
    // configurations.
    // mam(blizzardConfigIdx)(point) = shortestTime
    val mem = List.fill(blizzards.cycleLength) { mutable.Map[(Int, Int), Int]() }
    mem(startTime % blizzards.cycleLength)(start) = startTime
    var timeToEnd = -1
    val q = mutable.Queue((start, startTime))
    while (q.nonEmpty && timeToEnd == -1) {
      val (prevPos, prevTime) = q.dequeue()
      val time = prevTime + 1
      val cycleTime = time % blizzards.cycleLength
      val currentBlizzards = blizzards.atTime(time)

      Seq((0, 0), (1, 0), (-1, 0), (0, 1), (0, -1))
        .map((dRow, dCol) => (prevPos._1 + dRow, prevPos._2 + dCol))
        .filter((row, col) => (row, col) == start || (row, col) == end || (col >= 0 && col < blizzards.nCols && row >= 0 && row < blizzards.nRows))
        .filterNot(currentBlizzards.contains)
        .filterNot(mem(cycleTime).contains)
        .foreach { pos =>
          if (pos == end)
            timeToEnd = time
          mem(cycleTime)(pos) = time
          q.enqueue((pos, time))
        }
    }
    timeToEnd
  }

  def parseInput(source: Source): ((Int, Int), (Int, Int), Blizzards) = {
    val lines = source.getLines().toList
    val valley = lines
      .slice(1, lines.length - 1)
      .map(line => line.substring(1, line.length - 1))
    val blizzards = Blizzards.parse(valley)
    val start = (-1, lines.head.indexOf('.') - 1)
    val end = (lines.length - 2, lines.last.indexOf('.') - 1)

    (start, end, blizzards)
  }
}

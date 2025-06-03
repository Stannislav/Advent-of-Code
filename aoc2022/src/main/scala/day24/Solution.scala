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
    val best = List.fill(blizzards.cycleLength) { mutable.Map[(Int, Int), Int]() }
    best(startTime % blizzards.cycleLength)(start) = startTime
    val q = mutable.Queue((start, startTime))
    while (q.nonEmpty) {
      val (pos, prevTime) = q.dequeue()
      val time = prevTime + 1
      val cycleTime = time % blizzards.cycleLength
      val currentBlizzards = blizzards.atTime(time)

      Seq((0, 0), (1, 0), (-1, 0), (0, 1), (0, -1))
        .map((dRow, dCol) => (pos._1 + dRow, pos._2 + dCol))
        .filter((row, col) => (row, col) == start || (row, col) == end || (col > 0 && col < blizzards.nCols - 1 && row > 0 && row < blizzards.nRows - 1))
        .filterNot(currentBlizzards.contains)
        .filterNot(best(cycleTime).contains)
        .foreach { nextPos =>
          best(cycleTime)(nextPos) = time
          q.enqueue((nextPos, time))
        }
    }
    best.filter(_.contains(end)).map(_(end)).min
  }

  def parseInput(source: Source): ((Int, Int), (Int, Int), Blizzards) = {
    val lines = source.getLines().toList
    val blizzards = Blizzards.parse(lines)
    val start = (0, lines.head.indexOf('.'))
    val end = (lines.length - 1, lines.last.indexOf('.'))

    (start, end, blizzards)
  }
}

// TODO: Finish solving.
package day24

import scala.annotation.tailrec
import scala.io.Source

object Solution {
  def main(args: Array[String]): Unit = {
    val (start, end, blizzards) = parseInput(Source.fromResource("24.txt"))
    println(start)
    println(end)

    @tailrec
    def play(blizzards: Blizzards, minutes: Int, time: Int = 0): Unit = {
      println(s"== After minute $time ==")
      println(blizzards)
      println()
      if (time < minutes)
        play(blizzards.step, minutes, time + 1)
    }
    play(blizzards, 18)


    val moves = List((1, 0), (0, 1), (0, 0), (-1, 0), (0, -1))

    def dfs(pos: (Int, Int), blizzards: Blizzards, steps: Int = 0, best: Int = Int.MaxValue): Int = {
      if (blizzards.pos.contains(pos))
        best
      else if (pos == end)
        steps
      else
        moves.foldLeft(best) { (best, dPos) =>
          dfs((pos._1 + dPos._1, pos._2 + dPos._2), blizzards.step, steps + 1, best)
        }
    }
    println(s"Part 1: ${dfs(start, blizzards)}")

  }

  def parseInput(source: Source): ((Int, Int), (Int, Int), Blizzards) = {
    val lines = source.getLines().toList
    val blizzards = Blizzards.parse(lines)
    val start = (0, lines.head.indexOf('.'))
    val end = (lines.length - 1, lines.last.indexOf('.'))

    (start, end, blizzards)
  }
}

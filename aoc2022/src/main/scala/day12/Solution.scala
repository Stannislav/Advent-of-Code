package day12

import scala.collection.mutable


object Solution {
  def main(args: Array[String]): Unit = {
    var start = (-1, -1)
    var end = (-1, -1)
    val map = util.Using.resource(io.Source.fromResource("input/12.txt")) { _
      .getLines
      .zipWithIndex
      .map((line, row) => line.zipWithIndex.map((c, col) => c match {
        case 'S' => start = (row, col); 'a'
        case 'E' => end = (row, col); 'z'
        case c: Char => c
      }).toList).toList
    }

    println(s"Part 1: ${bfs(map, start, end)}")
    val part2 = Range(0, map.length)
      .flatMap(row => Range(0, map.head.length).map(col => (row, col)))  // = coordinate grid
      .filter((row, col) => map(row)(col) == 'a')
      .map(newStart => bfs(map, newStart, end))
      .min
    println(s"Part 2: $part2")
  }

  private def bfs(map: List[List[Char]], start: (Int, Int), end: (Int, Int)): Int = {
    val rows = map.length
    val cols = map.head.length
    val maxDist = rows * cols
    val dist = mutable.ListBuffer.tabulate(rows)(_ => mutable.ListBuffer.fill(cols)(maxDist))
    dist(start._1)(start._2) = 0

    def inBounds(r: Int, c: Int): Boolean = r >= 0 && r < rows && c >= 0 && c < cols

    val q = mutable.Queue(start)
    while (q.nonEmpty) {
      val (r, c) = q.dequeue
      for ((nr, nc) <- Seq((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))) {
        if (inBounds(nr, nc) && map(nr)(nc) - map(r)(c) <= 1) {
          val nextDist = dist(r)(c) + 1
          if (dist(nr)(nc) > nextDist) {
            dist(nr)(nc) = nextDist
            q.enqueue((nr, nc))
          }
        }
      }
    }

    dist(end._1)(end._2)
  }
}

// TODO: Clean up code
package day15

object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/15.txt")) { _
      .getLines
      .map {
        case s"Sensor at x=$sx, y=$sy: closest beacon is at x=$bx, y=$by" =>
          (sx.toInt, sy.toInt, bx.toInt, by.toInt)
        case _ => throw Exception("Invalid line")
      }
      .toList
    }

    // part 1
    val y = if (input.length == 14) 10 else 2000000
    val union = unionSize(orderedIntervalsAt(y, input))
    val nBeacons = input
      .flatMap((sx, sy, bx, by) => List((sx, sy), (bx, by)))
      .filter((*, by) => by == y)
      .toSet
      .size
    val part1 = union - nBeacons
    println(s"Part 1: $part1")

    // part 2
//    val x: Long = 3270298
//    val y: Long = 2638237
//    val part2 = x * 4000000 + y
//    assert(part2 == 13081194638237L)

    for (y <- (0 to 4000000)) {
      val size = unionSize(
        orderedIntervalsAt(y, input)
        .map((left, right) => (Math.max(left, 0), Math.min(right, 4000001)))
      )
      if (size != 4000001)
        println(s"y=$y")
    }
    println(unionSize(orderedIntervalsAt(y, input)
    .map((left, right) => (Math.max(left, 0), Math.min(right, 4000001)))))
  }

  private def orderedIntervalsAt(y: Int, input: Seq[(Int, Int, Int, Int)]): Seq[(Int, Int)] = {
    input
      .map((sx, sy, bx, by) => {
        val r = Math.abs(sx - bx) + Math.abs(sy - by)
        val dy = Math.abs(sy - y)
        (sx - (r - dy), sx + (r - dy) + 1)
      })
      .filter((left, right) => left < right)
      .sortBy((x1, _) => x1)
  }
  private def unionSize(intervals: Seq[(Int, Int)]): Int = {
    var union = 0
    var last_x2 = intervals.head._1
    for ((x1, x2) <- intervals) {
      if (x2 > last_x2) {
        union += x2 - Math.max(x1, last_x2)
        last_x2 = x2
      }
    }
    union
  }
}

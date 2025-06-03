import day24.{Blizzards, Solution}
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class Day24Test extends AnyFunSuite {
  test("Solution.parseInput") {
    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))

    val expectedBlizzards = Blizzards(
      6,
      8,
      List(
        (1, 1), (1, 2), (1, 4), (1, 5), (1, 6),
        (2, 2), (2, 5), (2, 6),
        (3, 1), (3, 2), (3, 4), (3, 5), (3, 6),
        (4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6),
      ),
      List(
        (0, 1), (0, 1), (0, -1), (-1, 0), (0, -1),
        (0, -1), (0, -1), (0, -1),
        (0, 1), (1, 0), (0, 1), (0, -1), (0, 1),
        (0, -1), (-1, 0), (1, 0), (-1, 0), (-1, 0), (0, 1),
      ),
    )

    assert(start === (0, 1))
    assert(end === (5, 6))
    assert(blizzards === expectedBlizzards)
  }

  test("Blizzards.atTime") {
    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))

    assert(blizzards.atTime(12) === blizzards.atTime(0))
    assert(blizzards.atTime(12 * 340930 + 3) === blizzards.atTime(3))
  }
}

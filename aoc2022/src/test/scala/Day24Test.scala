import day24.{Blizzards, Solution}
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class Day24Test extends AnyFunSuite {
  test("Solution.parseInput") {
    val expectedBlizzards = Blizzards(
      4,
      6,
      List(
        (0,0), (0,1), (0,3), (0,4), (0,5),
        (1,1), (1,4), (1,5),
        (2,0), (2,1), (2,3), (2,4), (2,5),
        (3,0), (3,1), (3,2), (3,3), (3,4), (3,5),
      ),
      List(
        (0,1), (0,1), (0,-1), (-1,0), (0,-1),
        (0,-1), (0,-1), (0,-1),
        (0,1), (1,0), (0,1), (0,-1), (0,1),
        (0,-1), (-1,0), (1,0), (-1,0), (-1,0), (0,1),
      ),
    )

    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))
    assert(start === (-1, 0))
    assert(end === (4, 5))
    assert(blizzards === expectedBlizzards)
  }

  test("Blizzards.atTime") {
    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))
    assert(blizzards.atTime(12) === blizzards.atTime(0))
    assert(blizzards.atTime(12 * 340930 + 3) === blizzards.atTime(3))
  }

  test("Solution.part1") {
    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))
    assert(Solution.part1(start, end, blizzards) === 18)
  }

  test("Solution.part2") {
    val (start, end, blizzards) = Solution.parseInput(Source.fromResource("24.txt"))
    assert(Solution.part2(start, end, blizzards) === 54)
  }
}

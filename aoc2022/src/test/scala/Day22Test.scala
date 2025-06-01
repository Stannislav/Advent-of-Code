import day22.{Coord, Solution}
import org.scalatest.funsuite.AnyFunSuite

import io.Source

class Day22Test extends AnyFunSuite {
  test("Solution.parseInput") {
    val (map, path) = Solution.parseInput(Source.fromResource("22.txt"))
    val expectedPath = List[Matchable](10, 'R', 5, 'L', 5, 'R', 10, 'L', 4, 'R', 5, 'L', 5)
    assert(map.size === 96)
    assert(map.values.toSet == Set('.', '#'))
    assert(path === expectedPath)
  }

  test("Solution.part1") {
    val (map, path) = Solution.parseInput(Source.fromResource("22.txt"))
    assert(Solution.part1(map, path) === 6032)
  }

  test("Solution.part2") {
    val (map, path) = Solution.parseInput(Source.fromResource("22.txt"))
    assert(Solution.part2(map, path) === 5031)
  }
}

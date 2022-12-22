import scala.annotation.tailrec
import scala.collection.mutable

object Day22 {
  def main(args: Array[String]): Unit = {
    val (map, path) = util.Using.resource(io.Source.fromFile("2022/input/22.txt")) {
      _.mkString.split("\n\n").match {
        case Array(map, path) => (parseMap(map), parsePath(path.strip()))
        case input @ _ => throw Exception(s"Invalid input:\n${input.mkString("\n\n")}")
      }
    }
    val ds = List((0, 1), (1, 0), (0, -1), (-1, 0))

    def neighbour(pos: (Int, Int), dir: Int): (Int, Int) = {
      var nextPos = (pos._1 + ds(dir)._1, pos._2 + ds(dir)._2)

      if (!map.contains(nextPos)) {
        // wrap around
        val oppositeDir = (dir + 2) % 4
        nextPos = pos
        var nextNextPos = (nextPos._1 + ds(oppositeDir)._1, nextPos._2 + ds(oppositeDir)._2)
        while (map.contains(nextNextPos)) {
          nextPos = nextNextPos
          nextNextPos = (nextPos._1 + ds(oppositeDir)._1, nextPos._2 + ds(oppositeDir)._2)
        }
      }
      nextPos
    }

    @tailrec
    def move(pos: (Int, Int), dir: Int, steps: Int): (Int, Int) = {
      if (steps == 0)
        pos
      else {
        val next = neighbour(pos, dir)
        map(next) match {
          case '.' => move(next, dir, steps - 1)
          case '#' => move(pos, dir, steps - 1)
        }
      }
    }

    @tailrec
    def walk(pos: (Int, Int), dir: Int, t: Int = 0): ((Int, Int), Int, Int) = {
      if (t >= path.length)
        (pos, dir, t)
      else
        path(t) match {
          case 'L' => walk(pos, (dir + 3) % 4, t + 1)
          case 'R' => walk(pos, (dir + 1) % 4, t + 1)
          case n: Int => walk(move(pos, dir, n), dir, t + 1)
          case other @ _ => throw Exception(s"Invalid command: $other")
        }
    }

    val (pos, dir, _) = walk(map.keys.filter(_._1 == 1).minBy(_._2), 0)
    val part1 = 1000 * pos._1 + 4 * pos._2 + dir
    println(s"Part 1: $part1")

  }

  private def parseMap(inputMap: String): Map[(Int, Int), Char] = {
    inputMap
      .linesIterator
      .zipWithIndex
      .flatMap((line, row) => line.zipWithIndex.map((c, col) => ((row + 1, col + 1), c)))
      .filter((*, c) => c == '#' || c == '.')
      .toMap
  }

  private def parsePath(inputPath: String): List[Matchable] = {
    val digit = raw"(\d+)(.*)".r
    val letter = "([LR])(.*)".r

    @tailrec
    def iterParse(input: String, acc: List[Matchable] = List()): List[Matchable] = {
      input match {
        case "" => acc
        case digit(num, rest) => iterParse(rest, acc :+ num.toInt)
        case letter(dir ,rest) => iterParse(rest, acc :+ dir.head)
        case other @ _ => throw Exception(s"Can't parse path: $other")
      }
    }
    iterParse(inputPath)
  }
}

package day22

import io.Source
import scala.annotation.tailrec
import scala.collection.mutable


type Coord = (Int, Int)
val DELTAS: List[Coord] = List((0, 1), (1, 0), (0, -1), (-1, 0))


object Solution {
  def main(args: Array[String]): Unit = {
    val (map, path) = parseInput(Source.fromResource("input/22.txt"))
    println(s"Part 1: ${part1(map, path)}")
    println(s"Part 2: ${part2(map, path)}")
  }

  def part1(map: Map[Coord, Char], path: List[Matchable]): Int = solve(map, findFlatWraps(map), path)

  def part2(map: Map[Coord, Char], path: List[Matchable]): Int = solve(map, findCubeWraps(map), path)


  def parseInput(source: Source): (Map[Coord, Char], List[Matchable]) = {
    source.mkString.split("\n\n").match {
      case Array(map, path) => (parseMap(map), parsePath(path.strip()))
      case input@_ => throw Exception(s"Invalid input:\n${input.mkString("\n\n")}")
    }
  }

  private def solve(
    map: Map[Coord, Char],
    wraps: Map[(Coord, Int), (Coord, Int)],
    path: List[Matchable],
  ): Int = {
    def step(pos: Coord, dir: Int): (Coord, Int) =
      if (wraps.contains((pos, dir)))
        wraps((pos, dir))
      else
        ((pos._1 + DELTAS(dir)._1, pos._2 + DELTAS(dir)._2), dir)

    @tailrec
    def move(pos: Coord, dir: Int, nSteps: Int): Coord = {
      if (nSteps == 0)
        pos
      else {
        val (newPos, newDir) = step(pos, dir)
        map(newPos) match {
          case '.' => move(newPos, newDir, nSteps - 1)
          case '#' => move(pos, dir, nSteps - 1)
        }
      }
    }

    @tailrec
    def walk(pos: Coord, dir: Int, t: Int = 0): (Coord, Int, Int) = {
      if (t >= path.length)
        (pos, dir, t)
      else
        path(t) match {
          case 'L' => walk(pos, (dir + 3) % 4, t + 1)
          case 'R' => walk(pos, (dir + 1) % 4, t + 1)
          case n: Int => walk(move(pos, dir, n), dir, t + 1)
          case other@_ => throw Exception(s"Invalid command: $other")
        }
    }

    val (pos, dir, _) = walk(map.keys.filter(_._1 == 1).minBy(_._2), 0)
    1000 * pos._1 + 4 * pos._2 + dir
  }

  private def findFlatWraps(map: Map[Coord, Char]): Map[(Coord, Int), (Coord, Int)] = {
    // precompute the wrap-around
    // 1. Find all surface normals.
    val normals = mutable.Set[(Coord, Int)]()
    map.keys.foreach(point => {
      Range(0, 4).foreach(dir => {
        val neighbour = (point._1 + DELTAS(dir)._1, point._2 + DELTAS(dir)._2)
        if (!map.contains(neighbour))
          normals.addOne((point, dir))
      })
    })

    // 2. Match pairs of surface points and connect them
    val wraps = mutable.Map[(Coord, Int), (Coord, Int)]()
    while (normals.nonEmpty) {
      val (point1, dir1) = normals.head
      normals.remove((point1, dir1))
      val dir2 = (dir1 + 2) % 4
      var point2 = point1
      while (!normals.remove((point2, dir2)))
        point2 = (point2._1 + DELTAS(dir2)._1, point2._2 + DELTAS(dir2)._2)
      wraps.addOne((point1, dir1), (point2, dir1))
      wraps.addOne((point2, dir2), (point1, dir2))
    }

    wraps.toMap
  }

  private def findCubeWraps(map: Map[Coord, Char]): Map[(Coord, Int), (Coord, Int)] = {
    Map()
  }

  private def parseMap(inputMap: String): Map[Coord, Char] = {
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

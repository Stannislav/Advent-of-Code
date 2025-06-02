package day22

import java.lang.Math.floorMod
import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source

val L = Vec(0, -1)
val R = Vec(0, 1)
val U = Vec(-1, 0)
val D = Vec(1, 0)
// Order significant and as given in problem statement.
val DIR_VECTORS: List[Vec] = List(R, D, L, U)


object Solution {
  def main(args: Array[String]): Unit = {
    val (map, path) = parseInput(Source.fromResource("input/22.txt"))
    println(s"Part 1: ${part1(map, path)}")
    println(s"Part 2: ${part2(map, path)}")
  }

  def part1(map: Map[Vec, Char], path: List[Matchable]): Int = solve(map, findFlatWraps(map), path)

  def part2(map: Map[Vec, Char], path: List[Matchable]): Int = solve(map, findCubeWraps(map), path)


  def parseInput(source: Source): (Map[Vec, Char], List[Matchable]) = {
    source.mkString.split("\n\n").match {
      case Array(map, path) => (parseMap(map), parsePath(path.strip()))
      case input@_ => throw Exception(s"Invalid input:\n${input.mkString("\n\n")}")
    }
  }

  private def parseMap(inputMap: String): Map[Vec, Char] = {
    inputMap
      .linesIterator
      .zipWithIndex
      .flatMap((line, row) => line.zipWithIndex.map((c, col) => (Vec(row, col), c)))
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
        case letter(dir, rest) => iterParse(rest, acc :+ dir.head)
        case other@_ => throw Exception(s"Can't parse path: $other")
      }
    }

    iterParse(inputPath)
  }

  private def solve(
                     map: Map[Vec, Char],
                     wraps: Map[(Vec, Int), (Vec, Int)],
                     path: List[Matchable],
  ): Int = {
    def step(pos: Vec, dir: Int): (Vec, Int) =
      if (wraps.contains((pos, dir)))
        wraps((pos, dir))
      else
        (pos + DIR_VECTORS(dir), dir)

    @tailrec
    def move(pos: Vec, dir: Int, nSteps: Int): (Vec, Int) = {
      if (nSteps == 0)
        (pos, dir)
      else {
        val (newPos, newDir) = step(pos, dir)
        map(newPos) match {
          case '.' => move(newPos, newDir, nSteps - 1)
          case '#' => move(pos, dir, nSteps - 1)
        }
      }
    }

    @tailrec
    def walk(pos: Vec, dir: Int, t: Int = 0): (Vec, Int, Int) = {
      if (t >= path.length)
        (pos, dir, t)
      else
        path(t) match {
          case 'L' => walk(pos, (dir + 3) % 4, t + 1)
          case 'R' => walk(pos, (dir + 1) % 4, t + 1)
          case n: Int =>
            val (newPos, newDir) = move(pos, dir, n)
            walk(newPos, newDir, t + 1)
          case other@_ => throw Exception(s"Invalid command: $other")
        }
    }

    val (pos, dir, _) = walk(map.keys.filter(_.row == 0).minBy(_.col), 0)
    1000 * (pos.row + 1) + 4 * (pos.col + 1) + dir
  }

  private def findFlatWraps(map: Map[Vec, Char]): Map[(Vec, Int), (Vec, Int)] = {
    // precompute the wrap-around
    // 1. Find all surface normals.
    val normals = mutable.Set[(Vec, Int)]()
    map.keys.foreach(pos => {
      Range(0, 4).foreach(dir => {
        val neighbour = pos + DIR_VECTORS(dir)
        if (!map.contains(neighbour))
          normals.addOne((pos, dir))
      })
    })

    // 2. Match pairs of surface points and connect them
    val wraps = mutable.Map[(Vec, Int), (Vec, Int)]()
    while (normals.nonEmpty) {
      val (pos1, dir1) = normals.head
      normals.remove((pos1, dir1))
      val dir2 = (dir1 + 2) % 4
      var pos2 = pos1
      while (!normals.remove((pos2, dir2)))
        pos2 = pos2 + DIR_VECTORS(dir2)
      wraps.addOne((pos1, dir1), (pos2, dir1))
      wraps.addOne((pos2, dir2), (pos1, dir2))
    }

    wraps.toMap
  }

  private def findCubeWraps(map: Map[Vec, Char]): Map[(Vec, Int), (Vec, Int)] = {
    val scale = Math.sqrt(map.size / 6).toInt
    val faces: Set[Vec] = map.keys.map(_ / scale).toSet
    assert(faces.size == 6)

    // Find adjacent faces. Use the fact that an adjacent face can be reached
    // via another adjacent face, e.g.:
    //  U
    // L#R
    //  D
    // Starting with the middle face ("#") one can reach the face "U" via "L" or "R":
    // U = L->U = R->U. So, if in the exploded diagram U were missing, then one could
    // look for it via L or R, e.g.:
    // U
    // L#R
    //  D
    // Additionally, we need to take into account the tile rotation: in the diagram above
    // the U should really be on its left side for the diagram to be equivalent to the
    // first one. In the first diagram, going upwards from L one enters U on its left side.
    // tile -> direction (L, R, U, D) -> (adj. tile, rotation)
    // rotation is measured counter-clockwise in multiple of 90 degrees
    val adjacent = faces.map(face => face -> DIR_VECTORS
      .filter(dir => faces.contains(face + dir))
      .map(dir => dir -> (face + dir, 0))
      .to(collection.mutable.Map)
    ).toMap

    while(!adjacent.values.forall(_.size == 4)) {
      for(face <- faces) {
        if (adjacent(face).contains(U) && adjacent(face).contains(L)) {
          val (upFace, upRot) = adjacent(face)(U)
          val (leftFace, leftRot) = adjacent(face)(L)
          if (!adjacent(leftFace).contains(U.turnBackwards(leftRot))) {
            adjacent(leftFace)(U.turnBackwards(leftRot)) = (upFace, floorMod(upRot + 1 - leftRot, 4))
            adjacent(upFace)(L.turnBackwards(upRot)) = (leftFace, floorMod(leftRot - 1 - upRot, 4))
          }
        }
        if (adjacent(face).contains(U) && adjacent(face).contains(R)) {
          val (upFace, upRot) = adjacent(face)(U)
          val (rightFace, rightRot) = adjacent(face)(R)
          if (!adjacent(rightFace).contains(U.turnBackwards(rightRot))) {
            adjacent(rightFace)(U.turnBackwards(rightRot)) = (upFace, floorMod(upRot - 1 - rightRot, 4))
            adjacent(upFace)(R.turnBackwards(upRot)) = (rightFace, floorMod(rightRot + 1 - upRot, 4))
          }
        }
        if (adjacent(face).contains(D) && adjacent(face).contains(R)) {
          val (downFace, downRot) = adjacent(face)(D)
          val (rightFace, rightRot) = adjacent(face)(R)
          if (!adjacent(rightFace).contains(D.turnBackwards(rightRot))) {
            adjacent(rightFace)(D.turnBackwards(rightRot)) = (downFace, floorMod(downRot + 1 - rightRot, 4))
            adjacent(downFace)(R.turnBackwards(downRot)) = (rightFace, floorMod(rightRot - 1 - downRot, 4))
          }
        }
        if (adjacent(face).contains(D) && adjacent(face).contains(L)) {
          val (downFace, downRot) = adjacent(face)(D)
          val (leftFace, leftRot) = adjacent(face)(L)
          if (!adjacent(leftFace).contains(D.turnBackwards(leftRot))) {
            adjacent(leftFace)(D.turnBackwards(leftRot)) = (downFace, floorMod(downRot - 1 - leftRot, 4))
            adjacent(downFace)(L.turnBackwards(downRot)) = (leftFace, floorMod(leftRot + 1 - downRot, 4))
          }
        }
      }
    }

    val wraps = mutable.Map[(Vec, Int), (Vec, Int)]()

    for (pos <- map.keys) {
      for (dir <- DIR_VECTORS) {
        if (!map.contains(pos + dir)) {
          val face = pos / scale
          val (adjFace, adjRot) = adjacent(face)(dir)
          val facePos = face * scale
          val adjFacePos = adjFace * scale

          val shiftedPos = pos - facePos
          val rotatedPos = shiftedPos.turnFaceBackwards(adjRot, scale)
          val unshiftedPos = rotatedPos + facePos
          val newDir = dir.turnBackwards(adjRot)
          val newPos = unshiftedPos + (adjFacePos - facePos) - (newDir * scale)
          wraps((pos, DIR_VECTORS.indexOf(dir))) = (newPos + newDir, DIR_VECTORS.indexOf(newDir))
        }
      }
    }

    wraps.toMap
  }
}

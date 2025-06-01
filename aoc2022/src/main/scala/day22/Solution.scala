package day22

import io.Source
import Math.floorMod
import scala.annotation.tailrec
import scala.collection.mutable


type Coord = (Int, Int)
val L: Coord = (0, -1)
val R: Coord = (0, 1)
val U: Coord = (-1, 0)
val D: Coord = (1, 0)
val DELTAS: List[Coord] = List(R, D, L, U)

@tailrec
def unrot(coord: Coord, deg: Int): Coord = {
  if (deg == 0) {
    coord
  } else {
    unrot((coord._2, -coord._1), floorMod(deg - 1, 4))
  }
}

@tailrec
def unrotFace(coord: Coord, deg: Int, scale: Int): Coord = {
  // Rotate a face if size (scale, scale) located at (0, 0) around its centre,
  // i.e. after the rotation its top left corner is still at (0, 0).
  if (deg == 0) {
    coord
  } else {
    unrotFace((coord._2, -coord._1 + scale - 1), floorMod(deg - 1, 4), scale)
  }
}


def add(c1: Coord, c2: Coord): Coord = (c1._1 + c2._1, c1._2 + c2._2)
def sub(c1: Coord, c2: Coord): Coord = (c1._1 - c2._1, c1._2 - c2._2)
def add(c1: Coord, c: Int): Coord = (c1._1 + c, c1._2 + c)
def sub(c1: Coord, c: Int): Coord = (c1._1 - c, c1._2 - c)
def times(c1: Coord, c: Int): Coord = (c1._1 * c, c1._2 * c)

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
    def move(pos: Coord, dir: Int, nSteps: Int): (Coord, Int) = {
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
    def walk(pos: Coord, dir: Int, t: Int = 0): (Coord, Int, Int) = {
      println(s"Walk on pos=$pos with dir=$dir")
      if (t >= path.length)
        (pos, dir, t)
      else
        path(t) match {
          case 'L' => walk(pos, (dir + 3) % 4, t + 1)
          case 'R' => walk(pos, (dir + 1) % 4, t + 1)
          case n: Int => {
            val (newPos, newDir) = move(pos, dir, n)
            walk(newPos, newDir, t + 1)
          }
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
    val scale = Math.sqrt(map.size / 6).toInt
    println(s"Scale: $scale")

    val faces: Set[Coord] = map
      .keys
      .map((x, y) => (x - 1, y - 1))
      .map((x, y) => (x / scale, y / scale))
      .toSet
    println(s"Faces: $faces")

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
    val adjacent = faces.map(face => face -> DELTAS
      .filter(dir => faces.contains(add(face, dir)))
      .map(dir => dir -> (add(face, dir), 0))
      .to(collection.mutable.Map)
    ).toMap
    println(s"Adjacent: $adjacent")

    while(!adjacent.values.forall(_.size == 4)) {
      for(face <- faces) {
        if (adjacent(face).contains(U) && adjacent(face).contains(L)) {
          val (upFace, upRot) = adjacent(face)(U)
          val (leftFace, leftRot) = adjacent(face)(L)
          if (!adjacent(leftFace).contains(unrot(U, leftRot))) {
            adjacent(leftFace)(unrot(U, leftRot)) = (upFace, floorMod(upRot + 1 - leftRot, 4))
            adjacent(upFace)(unrot(L, upRot)) = (leftFace, floorMod(leftRot - 1 - upRot, 4))
            println(s"U-L found - set (face: $face)")
          }
        }
        if (adjacent(face).contains(U) && adjacent(face).contains(R)) {
          val (upFace, upRot) = adjacent(face)(U)
          val (rightFace, rightRot) = adjacent(face)(R)
          if (!adjacent(rightFace).contains(unrot(U, rightRot))) {
            adjacent(rightFace)(unrot(U, rightRot)) = (upFace, floorMod(upRot - 1 - rightRot, 4))
            adjacent(upFace)(unrot(R, upRot)) = (rightFace, floorMod(rightRot + 1 - upRot, 4))
            println(s"U-R found - setting (face: $face)")
          }
        }
        if (adjacent(face).contains(D) && adjacent(face).contains(R)) {
          val (downFace, downRot) = adjacent(face)(D)
          val (rightFace, rightRot) = adjacent(face)(R)
          if (!adjacent(rightFace).contains(unrot(D, rightRot))) {
            adjacent(rightFace)(unrot(D, rightRot)) = (downFace, floorMod(downRot + 1 - rightRot, 4))
            adjacent(downFace)(unrot(R, downRot)) = (rightFace, floorMod(rightRot - 1 - downRot, 4))
            println(f"D-R found - setting (face: $face)")
          }
        }
        if (adjacent(face).contains(D) && adjacent(face).contains(L)) {
          val (downFace, downRot) = adjacent(face)(D)
          val (leftFace, leftRot) = adjacent(face)(L)
          if (!adjacent(leftFace).contains(unrot(D, leftRot))) {
            adjacent(leftFace)(unrot(D, leftRot)) = (downFace, floorMod(downRot - 1 - leftRot, 4))
            adjacent(downFace)(unrot(L, downRot)) = (leftFace, floorMod(leftRot + 1 - downRot, 4))
            println(s"D-L found - setting (face: $face)")
          }
        }
      }
    }

    println("Adjacent faces:")
    for((face, v) <- adjacent) {
      println(s"Face $face")
      println(s"  L: ${v(L)}")
      println(s"  R: ${v(R)}")
      println(s"  U: ${v(U)}")
      println(s"  D: ${v(D)}")
    }

    val wraps = mutable.Map[(Coord, Int), (Coord, Int)]()

    for (coord <- map.keys) {
      for (dir <- DELTAS) {
        if (!map.contains(add(coord, dir))) {
          val face = ((coord._1 - 1) / scale, (coord._2 - 1) / scale)
          val (adjFace, adjRot) = adjacent(face)(dir)
          val facePos = times(face, scale)
          val adjFacePos = times(adjFace, scale)

          val shiftedCoord = sub(sub(coord, facePos), (1, 1))
          val rotatedCoord = unrotFace(shiftedCoord, adjRot, scale)
          val unshiftedCoord = add(add(rotatedCoord, (1, 1)), facePos)
          val newDir = unrot(dir, adjRot)
          val newCoord = sub(add(unshiftedCoord, sub(adjFacePos, facePos)), times(newDir, scale))
          wraps((coord, DELTAS.indexOf(dir))) = (add(newCoord, newDir), DELTAS.indexOf(newDir))
          println(s"face of $coord (dir: $dir) is $face. Adjacent face: $adjFace, rot: $adjRot")
          println(s"  ($coord, ${DELTAS.indexOf(dir)} => (${add(newCoord, newDir)}, ${DELTAS.indexOf(newDir)})")
        }
      }
    }

    wraps.toMap
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

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

  /** Find wraparounds for the input map assuming that it folds to a cube.
    *
    * We perform the following steps:
    *
    *   - Find the dimension of the cube faces (`scale`).
    *   - Find the relative location of the cube faces on the map (`faces`).
    *   - For each face find the four adjacent faces and the rotation required
    *     to align their edges (`adjacent`).
    *   - Use the face alignment information to match up points on the face edges.
    *
    * @param map The cube map from the input.
    * @return    A map of wraparounds for points on the cube.
    */
  private def findCubeWraps(map: Map[Vec, Char]): Map[(Vec, Int), (Vec, Int)] = {
    // Each face has scale^2 points, and there are six faces.
    val scale = Math.sqrt(map.size / 6).toInt
    // Integer division rounds down, so we get face coordinates in face space.
    val faces: Set[Vec] = map.keys.map(_ / scale).toSet
    assert(faces.size == 6)

    // For all faces find all four neighbouring faces.
    // Start with the faces which are already attached in the flat map.
    // Map: face -> direction (L, R, U, D) -> (adjacent face, rotation)
    // Rotation is measured counter-clockwise in multiples of 90 degrees.
    val adjacent = faces.map(face => face -> DIR_VECTORS
      .filter(dir => faces.contains(face + dir))
      .map(dir => dir -> (face + dir, 0))
      .to(collection.mutable.Map)
    ).toMap

    // Iteratively add all the missing neighbouring faces. To do that use the
    // fact that all three faces with the same corner touch each other. For
    // example, given the following map:
    //   U
    //  L#
    // we know that the left neighbour of U is L and the upper neighbour of L is U.
    // Additional care is required to take into account the existing rotation of faces.
    // For example, if we're looking for the left neighbour of a face which is rotated
    // by 90 degrees, then it's actually the upper neighbour of the original face.
    while(!adjacent.values.forall(_.size == 4)) {
      for(face <- faces) {
        for((dir1, dir2) <- Seq((U, L), (L, D), (D, R), (R, U))) {
          if (adjacent(face).contains(dir1) && adjacent(face).contains(dir2)) {
            val (face1, rot1) = adjacent(face)(dir1)
            val (face2, rot2) = adjacent(face)(dir2)
            adjacent(face2).getOrElseUpdate(dir1.turnBackwards(rot2), (face1, floorMod(rot1 + 1 - rot2, 4)))
            adjacent(face1).getOrElseUpdate(dir2.turnBackwards(rot1), (face2, floorMod(rot2 - 1 - rot1, 4)))
          }
        }
      }
    }

    // Compute wraparounds for points from the cube face adjacency information.
    val wraps = mutable.Map[(Vec, Int), (Vec, Int)]()
    for (pos <- map.keys) {
      for (dir <- DIR_VECTORS) {
        if (!map.contains(pos + dir)) {
          val face = pos / scale
          val (adjFace, adjRot) = adjacent(face)(dir)
          // Face position is its top left corner.
          val facePos = face * scale
          val adjFacePos = adjFace * scale

          // We need to rotate the edge point around the face its on.
          // To do that, we shift the face to the origin, use `turnFaceBackwards`
          // to do the rotation, then shift face back to where it came from.
          val shiftedPos = pos - facePos
          val rotatedPos = shiftedPos.turnFaceBackwards(adjRot, scale)
          val unshiftedPos = rotatedPos + facePos
          
          val newDir = dir.turnBackwards(adjRot)
          // The given point is mapped to the new position as follows:
          // 1. Rotate the point to align with the target face (`unshiftedPos`)
          // 2. Shift the original face on top of the target face (`+ (adjFacePos - facePos)`).
          // 3. Shift the face again off the target face so that now the original edge and the
          //    target edge align (` - (newDir * scale)`).
          // 4. Make a single step into the path direction to complete the wraparound.
          val newPos = unshiftedPos + (adjFacePos - facePos) - (newDir * scale) + newDir
          wraps((pos, DIR_VECTORS.indexOf(dir))) = (newPos, DIR_VECTORS.indexOf(newDir))
        }
      }
    }

    wraps.toMap
  }
}

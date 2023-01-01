package day18

import scala.collection.mutable

type Coord = List[Int]
type Shape = Set[Coord] | mutable.Set[Coord]

object Solution {
  def main(args: Array[String]): Unit = {
    val droplet: Set[Coord] = util.Using.resource(io.Source.fromResource("input/18.txt")) { _
      .getLines
      .map {case s"$x,$y,$z" => List(x.toInt, y.toInt, z.toInt)}
      .toSet
    }

    println(s"Part 1: ${surface(droplet)}")
    println(s"Part 2: ${exteriorSurface(droplet)}")

  }

  private def surface(shape: Set[Coord] | mutable.Set[Coord]): Int = {
    def touchingAlong(axis: Int, shape: Shape): Int = {
      val t1 = (axis + 1) % 3
      val t2 = (axis + 2) % 3

      shape
        .groupMap(xyz => (xyz(t1), xyz(t2)))(_(axis))
        .values
        .map(_.toList)
        .filter(_.length > 1)
        .map(_.sorted.sliding(2).map(pair => pair(1) - pair(0)).count(_ == 1))
        .sum
    }

    val touching = Range(0, 3).map(touchingAlong(_, shape)).sum
    6 * shape.size - 2 * touching
  }

  private def exteriorSurface(shape: Shape): Int = {
    // Make a square mould of the shape and compute its inner surface

    def bounds(shape: Shape, axis: Int): (Int, Int) = {
      val values = shape.map(_(axis))
      (values.min - 1, values.max + 1)
    }
    val (minX, maxX) = bounds(shape, 0)
    val (minY, maxY) = bounds(shape, 1)
    val (minZ, maxZ) = bounds(shape, 2)

    // flood the mould
    def neighbours(coord: Coord): List[Coord] = {
      val x = coord(0)
      val y = coord(1)
      val z = coord(2)

      val result = mutable.ListBuffer[Coord]()
      if (x > minX)
        result += List(x - 1, y, z)
      if (x < maxX)
        result += List(x + 1, y, z)
      if (y > minY)
        result += List(x, y - 1, z)
      if (y < maxY)
        result += List(x, y + 1, z)
      if (z > minZ)
        result += List(x, y, z - 1)
      if (z < maxZ)
        result += List(x, y, z + 1)

      result.toList
    }

    val mould = mutable.Set(List(minX, minY, minZ))
    val q = mutable.Queue(List(minX, minY, minZ))
    while (q.nonEmpty) {
      for (next <- neighbours(q.dequeue)) {
        if (!mould.contains(next) && !shape.contains(next))
          mould.add(next)
          q.enqueue(next)
      }
    }

    // exterior surface of shape =
    // = interior surface of mould =
    // = (total surface of mould) * (exterior surface of mould)
    val dx = maxX - minX + 1
    val dy = maxY - minY + 1
    val dz = maxZ - minZ + 1

    surface(mould) - 2 * (dx * dy + dy * dz + dz * dx)
  }
}

package day24

import java.lang.Math.floorMod

object Blizzards {
  def parse(lines: List[String]): Blizzards = {
    val nRows = lines.length
    val nCols = lines.head.length

    val blizzards = lines
      .zipWithIndex
      .flatMap((line, row) => line
        .zipWithIndex
        .filter((c, *) => "^>v<".contains(c))
        .map((c, col) => ((row, col), c))
      )
    val blizzardPos = blizzards.map(_._1)
    val blizzardDir = blizzards.map(_._2).map {
      case '>' => (0, 1)
      case 'v' => (1, 0)
      case '<' => (0, -1)
      case '^' => (-1, 0)
    }

    Blizzards(nRows, nCols, blizzardPos, blizzardDir)
  }
}

case class Blizzards(nRows: Int, nCols: Int, startPos: List[(Int, Int)], startDir: List[(Int, Int)]) {
  val cycleLength: Int = lcm(nRows, nCols)
  private val stateCache = collection.mutable.Map[Int, Set[(Int, Int)]]()

  def atTime(t: Int): Set[(Int, Int)] = stateCache.getOrElseUpdate(t % cycleLength, {
    startPos
      .zip(startDir)
      .map((pos, dir) => (pos._1 + dir._1 * t, pos._2 + dir._2 * t))
      .map((row, col) => (floorMod(row, nRows), floorMod(col, nCols)))
      .toSet
  })
}

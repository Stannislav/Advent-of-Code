package day24


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

case class Blizzards(nRows: Int, nCols: Int, pos: List[(Int, Int)], dir: List[(Int, Int)]) {
  def step: Blizzards = {
    def wrap(coord: Int, lim: Int): Int = ((coord - 1 + lim - 2) % (lim - 2)) + 1
    val newPos = pos
      .zip(dir)
      .map((pos, dPos) => (pos._1 + dPos._1, pos._2 + dPos._2))
      .map((row, col) => (wrap(row, nRows), wrap(col, nCols))
      )
    Blizzards(nRows, nCols, newPos, dir)
  }

  override def toString: String = {
    def posCounts = pos.groupBy(identity).view.mapValues(_.length).toMap
    def symbols: Map[(Int, Int), Char] = posCounts.map((pt, count) =>
      val symbol =
        if (count == 1) dir(pos.indexOf(pt)) match {
          case (0, 1) => '>'
          case (1, 0) => 'v'
          case (0, -1) => '<'
          case (-1, 0) => '^'
          case dir @ _ => throw Exception(s"Unknown direction: $dir")
        }
        else count.toString.head
      (pt, symbol)
    )
    Range(0, nRows).map(row =>
      Range(0, nCols)
        .map(col =>
          if (row == 0 || col == 0 || row == nRows - 1 || col == nCols - 1) '#'
          else symbols.getOrElse((row, col), '.'))
        .mkString
    ).mkString("\n")
  }
}

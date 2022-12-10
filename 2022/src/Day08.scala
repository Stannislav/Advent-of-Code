import scala.collection.mutable.ListBuffer


object Day08 {
  def main(args: Array[String]): Unit = {
    val trees = util.Using.resource(io.Source.fromFile("2022/input/08.txt")) { _
      .getLines
      .map(line => line.map(_.asDigit).toList)
      .toList
    }

    val rows = trees.length
    val cols = trees(0).length
    trees.foreach(line => assert(line.length == cols))

    /* Part 1
    Traverse tree grid from all 4 directions, keep track of the highest
    tree seen (max), mark visible trees by 1s.
    */
    var vis = zeroGrid(rows, cols)
    // left-right
    for (row <- Range(0, rows)) {
      // from left
      var max = -1
      for (col <- Range(0, cols)) {
        if (trees(row)(col) > max) {
          vis(row)(col) = 1
          max = trees(row)(col)
        }
      }
      // from right
      max = -1
      for (col <- Range(0, cols).reverse) {
        if (trees(row)(col) > max) {
          vis(row)(col) = 1
          max = trees(row)(col)
        }
      }
    }
    // top-bottom
    for (col <- Range(0, cols)) {
      // from left
      var max = -1
      for (row <- Range(0, rows)) {
        if (trees(row)(col) > max) {
          vis(row)(col) = 1
          max = trees(row)(col)
        }
      }
      // from right
      max = -1
      for (row <- Range(0, rows).reverse) {
        if (trees(row)(col) > max) {
          vis(row)(col) = 1
          max = trees(row)(col)
        }
      }
    }
    println(s"Part 1: ${vis.map(_.sum).sum}")

    // Part 2
    val scores = zeroGrid(rows, cols)
    for (row <- Range(0, rows))
      for (col <- Range(0, cols))
        scores(row)(col) = scenicScore(trees, row, col)
    println(s"Part 2: ${scores.map(_.max).max}")
  }

  def zeroGrid(rows: Int, cols: Int) = ListBuffer.tabulate(rows)(i => ListBuffer.fill(cols)(0))

  def scenicScore(trees: List[List[Int]], row: Int, col: Int): Int = {
    val rows = trees.length
    val cols = trees(0).length
    val height = trees(row)(col)

    // Positions of the last visible trees
    var (left, right, top, bottom) = (col - 1, col + 1, row - 1, row + 1)

    while (left > 0 && trees(row)(left) < height)
      left -= 1
    while (right < cols - 1 && trees(row)(right) < height)
      right += 1
    while (top > 0 && trees(top)(col) < height)
      top -= 1
    while (bottom < rows - 1 && trees(bottom)(col) < height)
      bottom += 1

    (col - left) * (right - col) * (row - top) * (bottom - row)
  }
}

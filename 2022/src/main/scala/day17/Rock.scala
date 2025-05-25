package day17

object Rock {
  def fromShape(lines: Seq[String]): Rock = {
    Rock(parseLines(lines))
  }

  private def parseLines(lines: Seq[String]): Array[Int] = {
    lines
      .map(".." + _)
      .map(_.padTo(7, '.'))
      .map(_.replace(".", "0").replace("#", "1"))
      .map(Integer.parseInt(_, 2))
      .reverse
      .toArray
  }
}

private class Rock(var rockData: Array[Int]) {
  private def canMoveRight = rockData.map(_ & 1).forall(_ == 0)

  private def canMoveLeft = rockData.map(_ & 64).forall(_ == 0)

  private def moveRight = rockData.map(_ >> 1)

  private def moveLeft = rockData.map(_ << 1)

  def isCollision(chamber: Array[Int], rockPos: Int): Boolean = {
    val ids = (rockPos until rockPos + rockData.length).filter(_ < chamber.length)
    (rockData zip ids).exists((line, idx) => (line & chamber(idx)) != 0)
  }

  def maybeMoveLeft(chamber: Array[Int], rockPos: Int): Unit = {
    if (canMoveLeft) {
      rockData = moveLeft
      if (isCollision(chamber, rockPos))
        rockData = moveRight
    }
  }

  def maybeMoveRight(chamber: Array[Int], rockPos: Int): Unit = {
    if (canMoveRight) {
      rockData = moveRight
      if (isCollision(chamber, rockPos))
        rockData = moveLeft
    }
  }

  def mergeIntoChamber(chamber: Array[Int], rockPos: Int): Array[Int] = {
    val newState = chamber.padTo(rockPos + rockData.length, 0)
    val ids = rockPos until rockPos + rockData.length
    (rockData zip ids).foreach((line, idx) => newState(idx) |= line)
    newState
  }
}

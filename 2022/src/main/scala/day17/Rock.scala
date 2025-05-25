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

private class Rock(var data: Array[Int]) {
  private def canMoveRight = data.map(_ & 1).forall(_ == 0)

  private def canMoveLeft = data.map(_ & 64).forall(_ == 0)

  private def moveRight = data.map(_ >> 1)

  private def moveLeft = data.map(_ << 1)

  def isCollision(state: Array[Int], rockPos: Int): Boolean = {
    val ids = (rockPos until rockPos + data.length).filter(_ < state.length)
    (data zip ids).exists((line, idx) => (line & state(idx)) != 0)
  }

  def maybeMoveLeft(state: Array[Int], rockPos: Int): Unit = {
    if (canMoveLeft) {
      data = moveLeft
      if (isCollision(state, rockPos))
        data = moveRight
    }
  }

  def maybeMoveRight(state: Array[Int], rockPos: Int): Unit = {
    if (canMoveRight) {
      data = moveRight
      if (isCollision(state, rockPos))
        data = moveLeft
    }
  }

  def mergeIntoState(state: Array[Int], rockPos: Int): Array[Int] = {
    val newState = state.padTo(rockPos + data.length, 0)
    val ids = rockPos until rockPos + data.length
    (data zip ids).foreach((line, idx) => newState(idx) |= line)
    newState
  }
}

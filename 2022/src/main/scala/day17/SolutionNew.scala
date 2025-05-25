package day17

import io.Source

object SolutionNew {
  private val ROCK_SHAPES: Seq[Seq[String]] = Seq(
    Seq("####"),
    Seq(".#.", "###", ".#."),
    Seq("..#", "..#", "###"),
    Seq("#", "#", "#", "#"),
    Seq("##", "##"),
  )

  def parseRock(lines: Seq[String]): Array[Int] = {
    lines
      .map(".." + _)
      .map(_.padTo(7, '.'))
      .map(_.replace(".", "0").replace("#", "1"))
      .map(Integer.parseInt(_, 2))
      .reverse
      .toArray
  }

  def main(args: Array[String]): Unit = {
    val ops = parseInput(Source.fromResource("input/17.txt"))
    val rocks = ROCK_SHAPES.map(parseRock).toArray

    var shapeIdx = 0
    var opIdx = 0
    var state = Array[Int]()


    def canMoveRight(shape: Array[Int]) = shape.map(_ & 1).forall(_ == 0)
    def canMoveLeft(shape: Array[Int]) = shape.map(_ & 64).forall(_ == 0)
    def moveRight(shape: Array[Int]) = shape.map(_ >> 1)
    def moveLeft(shape: Array[Int]) = shape.map(_ << 1)
    def isCollision(shape: Array[Int], state: Array[Int], shapePos: Int): Boolean = {
      val ids = (shapePos until shapePos + shape.length).filter(_ < state.length)
      (shape zip ids).exists((line, idx) => (line & state(idx)) != 0)
    }
    def mergeShapeIntoState(shape: Array[Int], state: Array[Int], shapePos: Int): Array[Int] = {
      val newState = state.padTo(shapePos + shape.length, 0)
      val ids = shapePos until shapePos + shape.length
      (shape zip ids).foreach((line, idx) => newState(idx) |= line)
      newState
    }

    def fall(initialShape: Array[Int], state: Array[Int]): Array[Int] = {
      var pos = state.length + 3
      var done = false
      var shape = initialShape.clone()
      while(!done) {
        // 1. Shift left-right
        if (ops(opIdx) == '<' && canMoveLeft(shape)) {
          shape = moveLeft(shape)
          if (isCollision(shape, state, pos)) {
            shape = moveRight(shape)
          }
        } else if(ops(opIdx) == '>' && canMoveRight(shape)) {
          shape = moveRight(shape)
          if (isCollision(shape, state, pos)) {
            shape = moveLeft(shape)
          }
        }
        opIdx = (opIdx + 1) % ops.length

        // 2. Shift down
        pos -= 1
        if (pos < 0 || isCollision(shape, state, pos)) {
          pos += 1
          done = true
        }
      }
      mergeShapeIntoState(shape, state, pos)
    }

    val cache = collection.mutable.Map[(Int, Int, Seq[Int]), (Int, Int)]()
    def reduceState(state: Array[Int]): (Array[Int], Int) = {
      val fullLine = 127 // 0b1111111
      val cutOff = state.lastIndexOf(fullLine) + 1
      (state.slice(cutOff, state.length), cutOff)
    }

    var prunedHeight = 0
    var nSteps = 0
    while(!cache.contains((opIdx, shapeIdx, state))) {
      cache((opIdx, shapeIdx, state)) = (nSteps, state.length + prunedHeight)
      state = fall(rocks(shapeIdx), state)
      shapeIdx = (shapeIdx + 1) % rocks.length
      val (reducedState, reducedLines) = reduceState(state)
      state = reducedState
      prunedHeight += reducedLines
      nSteps += 1
    }
    val (loopStart, startHeight) = cache((opIdx, shapeIdx, state))
    val loopLength = nSteps - loopStart
    val heights = Array.fill(cache.size) { 0 }
    for((idx, height) <- cache.values) {
      heights(idx) = height
    }
    val loopHeight = state.length + prunedHeight - heights(loopStart)
    def getHeightAfter(steps: Long): Long = {
      if (steps < heights.length) {
        heights(steps.toInt)
      } else {
        val nLoops = (steps - loopStart) / loopLength
        val loopIdx = (steps - loopStart) % loopLength
        heights(loopStart + loopIdx.toInt) + nLoops * loopHeight
      }
    }

    println(s"Part 1: ${getHeightAfter(2022)}")
    println(s"Part 2: ${getHeightAfter(1000000000000L)}")
  }

  def parseInput(stream: Source): Array[Char] = stream.mkString.strip().toArray
}

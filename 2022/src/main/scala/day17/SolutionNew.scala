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

  def main(args: Array[String]): Unit = {
    val ops = parseInput(Source.fromResource("input/17.txt"))

    var rockIdx = 0
    var opIdx = 0
    var state = Array[Int]()

    def fall(rockShape: Seq[String], state: Array[Int]): Array[Int] = {
      var pos = state.length + 3
      var done = false
      val rock = Rock.fromShape(rockShape)
      while(!done) {
        // 1. Shift left-right
        ops(opIdx) match {
          case '<' => rock.maybeMoveLeft(state, pos)
          case '>' => rock.maybeMoveRight(state, pos)
        }
        opIdx = (opIdx + 1) % ops.length

        // 2. Shift down
        pos -= 1
        if (pos < 0 || rock.isCollision(state, pos)) {
          pos += 1
          done = true
        }
      }
      rock.mergeIntoState(state, pos)
    }

    val cache = collection.mutable.Map[(Int, Int, Seq[Int]), (Int, Int)]()
    def reduceState(state: Array[Int]): (Array[Int], Int) = {
      val fullLine = 127 // 0b1111111
      val cutOff = state.lastIndexOf(fullLine) + 1
      (state.slice(cutOff, state.length), cutOff)
    }

    var prunedHeight = 0
    var nSteps = 0
    while(!cache.contains((opIdx, rockIdx, state))) {
      cache((opIdx, rockIdx, state)) = (nSteps, state.length + prunedHeight)
      state = fall(ROCK_SHAPES(rockIdx), state)
      rockIdx = (rockIdx + 1) % ROCK_SHAPES.length
      val (reducedState, reducedLines) = reduceState(state)
      state = reducedState
      prunedHeight += reducedLines
      nSteps += 1
    }
    val (loopStart, startHeight) = cache((opIdx, rockIdx, state))
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

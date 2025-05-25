package day17

import io.Source

object Solution {
  private val ROCK_SHAPES: Seq[Seq[String]] = Seq(
    Seq("####"),
    Seq(".#.", "###", ".#."),
    Seq("..#", "..#", "###"),
    Seq("#", "#", "#", "#"),
    Seq("##", "##"),
  )

  def main(args: Array[String]): Unit = {
    val jets = parseInput(Source.fromResource("input/17.txt"))

    var rockIdx = 0
    var jetIdx = 0
    var chamber = Array[Int]()

    def fall(rockShape: Seq[String], chamber: Array[Int]): Array[Int] = {
      var pos = chamber.length + 3
      var done = false
      val rock = Rock.fromShape(rockShape)
      while(!done) {
        // 1. Shift left-right
        jets(jetIdx) match {
          case '<' => rock.maybeMoveLeft(chamber, pos)
          case '>' => rock.maybeMoveRight(chamber, pos)
        }
        jetIdx = (jetIdx + 1) % jets.length

        // 2. Shift down
        pos -= 1
        if (pos < 0 || rock.isCollision(chamber, pos)) {
          pos += 1
          done = true
        }
      }
      rock.mergeIntoChamber(chamber, pos)
    }

    // (jetIdx, rockIdx, reducedChamber) => (nSteps, height)
    val cache = collection.mutable.Map[(Int, Int, Seq[Int]), (Int, Int)]()
    def reduceChamber(chamber: Array[Int]): (Array[Int], Int) = {
      val fullLine = 127 // 0b1111111
      val cutOff = chamber.lastIndexOf(fullLine) + 1
      (chamber.slice(cutOff, chamber.length), cutOff)
    }

    var prunedHeight = 0
    var nSteps = 0
    while(!cache.contains((jetIdx, rockIdx, chamber))) {
      cache((jetIdx, rockIdx, chamber)) = (nSteps, chamber.length + prunedHeight)
      chamber = fall(ROCK_SHAPES(rockIdx), chamber)
      rockIdx = (rockIdx + 1) % ROCK_SHAPES.length
      val (reducedChamber, reducedLines) = reduceChamber(chamber)
      chamber = reducedChamber
      prunedHeight += reducedLines
      nSteps += 1
    }
    val (loopStart, startHeight) = cache((jetIdx, rockIdx, chamber))
    val loopLength = nSteps - loopStart
    val heights = Array.fill(cache.size) { 0 }
    for((idx, height) <- cache.values) {
      heights(idx) = height
    }
    val loopHeight = chamber.length + prunedHeight - heights(loopStart)

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

  def parseInput(stream: Source): String = stream.mkString.strip()
}

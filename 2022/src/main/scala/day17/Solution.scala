package day17

import io.Source

object Solution {

  def parseInput(stream: Source): String = stream.mkString.strip()

  def main(args: Array[String]): Unit = {
    val jets = parseInput(Source.fromResource("input/17.txt"))
    val (heights, loopStart, loopHeightGain) = ChamberSimulation.run(jets)
    val loopLength = heights.length - loopStart

    def getHeightAfter(nSteps: Long): Long = {
      if (nSteps < heights.length) {
        heights(nSteps.toInt)
      } else {
        val nLoops = (nSteps - loopStart) / loopLength
        val idxInLoop = (nSteps - loopStart) % loopLength
        heights(loopStart + idxInLoop.toInt) + nLoops * loopHeightGain
      }
    }

    println(s"Part 1: ${getHeightAfter(2022)}")
    println(s"Part 2: ${getHeightAfter(1000000000000L)}")
  }
}

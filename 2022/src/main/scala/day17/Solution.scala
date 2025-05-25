package day17

import io.Source

object Solution {

  def parseInput(stream: Source): String = stream.mkString.strip()

  def main(args: Array[String]): Unit = {
    val jets = parseInput(Source.fromResource("input/17.txt"))
    val (heights, loopStart, loopHeightGain) = ChamberSimulation.run(jets)
    val solution = Solution(heights, loopStart, loopHeightGain)
    println(s"Part 1: ${solution.part1}")
    println(s"Part 2: ${solution.part2}")
  }
}

class Solution(heights: Array[Int], loopStart: Int, loopHeightGain: Int) {
  private val loopLength = heights.length - loopStart

  private def getHeightAfter(nSteps: Long): Long = {
    if (nSteps < heights.length) {
      heights(nSteps.toInt)
    } else {
      val nLoops = (nSteps - loopStart) / loopLength
      val idxInLoop = (nSteps - loopStart) % loopLength
      heights(loopStart + idxInLoop.toInt) + nLoops * loopHeightGain
    }
  }

  def part1: Long = getHeightAfter(2022)
  def part2: Long = getHeightAfter(1000000000000L)
}

// TODO: Finish solving.
package day19

import io.Source

object Solution {
  def main(args: Array[String]): Unit = {
//    val input = util.Using.resource(io.Source.fromResource("input/19-debug.txt")) {
//      _.getLines.map(Blueprint.fromString).toList
//    }
//    println(input.map(_.run()))
  }

  def parseInput(stream: Source): Seq[Array[Array[Int]]] = {
    stream
      .getLines()
      .map(parseBlueprint)
      .toSeq
  }

  private def parseBlueprint(line: String): Array[Array[Int]] = {
    line.match {
      case s"Blueprint $i: Each ore robot costs $oreOre ore. Each clay robot costs $clayOre ore. Each obsidian robot costs $obsidianOre ore and $obsidianClay clay. Each geode robot costs $geodeOre ore and $geodeObsidian obsidian." =>
        Array(
          Array(oreOre.toInt, 0, 0, 0),
          Array(clayOre.toInt, 0, 0, 0),
          Array(obsidianOre.toInt, obsidianClay.toInt, 0, 0),
          Array(geodeOre.toInt, 0, geodeObsidian.toInt, 0),
        )
      case _ => throw Exception(s"Can't parse line: $line")
    }
  }
}

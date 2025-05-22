// TODO: Finish solving.
package day19

object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/19-debug.txt")) {
      _.getLines.map(parseBlueprint).toList
    }
    println(input.map(_.run()))
  }

  private def parseBlueprint(line: String): Blueprint = {
    line.match {
      case s"Blueprint $i: Each ore robot costs $oreOre ore. Each clay robot costs $clayOre ore. Each obsidian robot costs $obsidianOre ore and $obsidianClay clay. Each geode robot costs $geodeOre ore and $geodeObsidian obsidian." =>
        new Blueprint(
          i.toInt,
          List(
            List(oreOre.toInt, 0, 0, 0),
            List(clayOre.toInt, 0, 0, 0),
            List(obsidianOre.toInt, obsidianClay.toInt, 0, 0),
            List(geodeOre.toInt, 0, geodeObsidian.toInt, 0),
          )
        )
    }
  }
}

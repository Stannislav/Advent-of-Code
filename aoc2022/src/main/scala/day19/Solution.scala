// TODO: Finish solving.
package day19

object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/19-debug.txt")) {
      _.getLines.map(Blueprint.fromString).toList
    }
    println(input.map(_.run()))
  }
}

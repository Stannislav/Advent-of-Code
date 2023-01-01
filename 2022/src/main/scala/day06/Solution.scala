package day06

import scala.util.control.NonLocalReturns.{returning, throwReturn}

object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/06.txt")) {_.mkString}

    println(s"Part 1: ${solve(input, 4)}")
    println(s"Part 2: ${solve(input, 14)}")
  }

  def solve(input: String, len: Int): Int = returning {
    for (received <- Range(len, input.length)) {
      if (input.slice(received - len, received).toSet.size == len)
        throwReturn(received)
    }
    -1
  }
}

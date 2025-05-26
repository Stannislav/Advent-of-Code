package day02

object Solution {
  /**
   * ==Idea==
   * if the shape of them/me is represented by `(rock, paper, scissors) = (0, 1, 2)` and
   * `outcome = (loss, draw, win) = (0, 1, 2)` then
   *
   * `outcome = (me - them + 1) % 3`
   *
   * and conversely
   *
   * `me = (outcome + them - 1) % 3`
   */
  def main(args: Array[String]): Unit = {
    // Read data
    val lines = util.Using.resource(io.Source.fromResource("input/02.txt")) { _
      .getLines
      .map(line => (line(0) - 'A', line(2) - 'X'))
      .toArray
    }

    // Part 1
    val part1 = lines.map((them, me) => score(mod(me - them + 1, 3), me)).sum
    println(s"Part 1: $part1")

    // Part 2
    val part2 = lines.map((them, outcome) => score(outcome, mod(outcome + them - 1, 3))).sum
    println(s"Part 2: $part2")
  }


  /**
   * Positive modulo.
   *
   * @param num     An integer.
   * @param divisor A divisor.
   * @return The positive version of `num % divisor`.
   */
  private def mod(num: Int, divisor: Int): Int = {
    val result = num % divisor
    if (result < 0)
      result + divisor
    else
      result
  }

  /**
   * Compute the score of a game round.
   *
   * @param outcome The round outcome: 0=loss, 1=draw, 2=win.
   * @param me      My shape: 0=rock, 1=paper, 2=scissors.
   * @return The round score.
   */
  private def score(outcome: Int, me: Int) = outcome * 3 + (me + 1)
}

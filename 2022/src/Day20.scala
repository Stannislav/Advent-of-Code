import scala.annotation.tailrec
import scala.collection.mutable

object Day20 {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromFile("2022/input/20.txt")) {
      _.getLines.map(_.toLong).toList
    }

    println(s"Part 1: ${decrypt(input, 1)}")
    println(s"Part 2: ${decrypt(input.map(_ * 811589153), 10)}")
  }

  private def decrypt(input: List[Long], nRounds: Int): Long = {
    val len = input.length

    // Model using a doubly-linked list
    val next = Range(1, len + 1).to(Array)
    next(len - 1) = 0
    val prev = Range(-1, len - 1).to(Array)
    prev(0) = len - 1

    def posMod(x: Long, div: Int): Long = {
      val res = x % div
      if (res < 0) res + div else res
    }

    @tailrec
    def shiftRight(idx: Int, n: Long): Int = if (n <= 0) idx else shiftRight(next(idx), n - 1)

    (1 to nRounds) foreach { * =>
      input.zipWithIndex.filter(_._1 != 0).foreach((x, idx) => {
        val oldPrev = prev(idx)
        val oldNext = next(idx)
        val newPrev = shiftRight(idx, posMod(x, len - 1)) // back to same position after n - 1 jumps
        val newNext = next(newPrev)
        // rewire the original place
        next(oldPrev) = oldNext
        prev(oldNext) = oldPrev
        // wire into the new place
        prev(idx) = newPrev
        next(idx) = newNext
        next(newPrev) = idx
        prev(newNext) = idx
      })
    }

    val zero = input.indexOf(0)
    List(1000, 2000, 3000).map(i => input(shiftRight(zero, i))).sum
  }
}

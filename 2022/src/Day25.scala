import scala.annotation.tailrec
import scala.annotation.targetName

object Day25 {
  def main(args: Array[String]): Unit = {
    val snafus = util.Using.resource(io.Source.fromFile("2022/input/25.txt")) {
      _.getLines.map(SNAFU.parse).toList
    }

    println(s"Part 1: ${snafus.reduce(_ + _)}")
  }

}

object SNAFU {
  def parse(s: String): SNAFU = {
    SNAFU(s.reverse.map({
      case '=' => -2
      case '-' => -1
      case c @ _ => c - '0'
    }).toList)
  }
}

class SNAFU(val digits: List[Int]) {
  @targetName("add")
  def +(other: SNAFU): SNAFU = {
    val nDigits = Math.max(digits.length, other.digits.length)
    val d1 = digits.padTo(nDigits, 0)
    val d2 = other.digits.padTo(nDigits, 0)

    @tailrec
    def addDigits(pos: Int = 0, carry: Int = 0, acc: List[Int] = List()): List[Int] = {
      if (pos == nDigits)
        if (carry > 0) acc :+ carry else acc
      else {
        val total = d1(pos) + d2(pos) + carry
        val nextDigit = Math.floorMod(total + 2, 5) - 2
        val nextCarry = Math.floor((total + 2) / 5.0).toInt

        addDigits(pos + 1, nextCarry, acc :+ nextDigit)
      }

    }

    SNAFU(addDigits())
  }

  override def toString: String =
    digits.reverse.map {
      case -2 => '='
      case -1 => '-'
      case x => x
    }.mkString
}

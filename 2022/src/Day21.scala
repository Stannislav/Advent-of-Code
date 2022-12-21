abstract class aMonkey {
  def yell(allMonkeys: Map[String, aMonkey]): BigInt
}

class NumMonkey(var num: BigInt) extends aMonkey {
  override def yell(allMonkeys: Map[String, aMonkey]): BigInt = num
}

class OpMonkey(
  leftName: String,
  rightName: String,
  var op: (BigInt, BigInt) => BigInt
) extends aMonkey {
  override def yell(allMonkeys: Map[String, aMonkey]): BigInt = {
    val leftNum = allMonkeys(leftName).yell(allMonkeys)
    val rightNum = allMonkeys(rightName).yell(allMonkeys)
    op(leftNum, rightNum)
  }
}

object Day21 {
  def main(args: Array[String]): Unit = {
    val monkeys = util.Using.resource(io.Source.fromFile("2022/input/21.txt")) {
      _.getLines.map(parseMonkey).toMap
    }
    println(s"Part 1: ${monkeys("root").yell(monkeys)}")
    println(s"Part 2: ${findHumn(monkeys).sorted.mkString("\n        ")}")
  }

  private def findHumn(monkeys: Map[String, aMonkey]): List[BigInt] = {
    val root = monkeys("root") match {
      case m: OpMonkey => m
      case _ => throw Exception("Root monkey is not op.")
    }
    val humn = monkeys("humn") match {
      case m: NumMonkey => m
      case _ => throw Exception("Humn is not num.")
    }

    def tryHumn(num: BigInt): BigInt = {
      humn.num = num
      root.yell(monkeys)
    }

    root.op = (left, right) => left - right
    // Make sure humn.num and root.yell are positively correlated
    if (tryHumn(0) > tryHumn(100))
      root.op = (left, right) => right - left


    def binarySearch(left: BigInt, right: BigInt): BigInt = {
      if (right - left <= 1)
        left
      else {
        val mid = (left + right) / 2
        if (tryHumn(mid) > 0)
          binarySearch(left, mid)
        else
          binarySearch(mid, right)
      }
    }

    val result =
      if (tryHumn(0) > 0)
        binarySearch(BigInt(Long.MinValue), BigInt(0))
      else
        binarySearch(BigInt(0), BigInt(Long.MaxValue))

    // Seems there can be several solutions using integers, so we collect all of them.
    // Not sure if I was supposed to work with floating-point numbers or if something
    // else is wrong. Using floating point numbers doesn't seem right.
    var allResults = List(result)
    var otherResult = result - 1
    while (tryHumn(otherResult) == 0) {
      allResults = otherResult +: allResults
      otherResult -= 1
    }
    otherResult = result + 1
    while (tryHumn(otherResult) == 0) {
      allResults = allResults :+ otherResult
      otherResult += 1
    }

    allResults
  }

  private def parseMonkey(line: String): (String, aMonkey) =
  line match {
    case s"$name: $m1 $op $m2" =>
      val opFn = op match {
        case "+" => (left: BigInt, right: BigInt) => left + right
        case "-" => (left: BigInt, right: BigInt) => left - right
        case "*" => (left: BigInt, right: BigInt) => left * right
        case "/" => (left: BigInt, right: BigInt) => left / right
        case _ => throw Exception(s"Unknown op: $op")
      }
      (name, OpMonkey(m1, m2, opFn))
    case s"$name: $num" => (name, NumMonkey(BigInt(num.toLong)))
    case line@_ => throw Exception(s"Malformed line: $line")
  }
}

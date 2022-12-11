import scala.collection.mutable

object Day11 {
  def main(args: Array[String]): Unit = {
    val monkeys = util.Using.resource(io.Source.fromFile("2022/input/11.txt")) { _
      .mkString
      .split("\n\n")
      .map(parseMonkey)
      .toList
    }

    val mod = monkeys.map(_.divisor).product
    println(s"Part 1: ${solve(monkeys, 20)}")
    println(s"Part 2: ${solve(monkeys, 10000, mod)}")
  }

  private def solve(monkeys: Seq[Monkey], nRounds: Int, mod: Int = 1): Long = {
    for (_ <- 1 to nRounds) {
      for (monkey <- monkeys) {
        while (monkey.hasItems) {
          val (item, target) = monkey.step(mod)
          monkeys(target).receive(item)
        }
      }
    }
    val solution = monkeys.map(_.nSteps).sorted.reverse.take(2).product
    monkeys.foreach(_.reset())

    solution
  }

  private def parseMonkey(input: String): Monkey = {
    val pattern = """Monkey \d+:
                    |  Starting items: (.+)
                    |  Operation: new = (.+)
                    |  Test: divisible by (\d+)
                    |    If true: throw to monkey (\d+)
                    |    If false: throw to monkey (\d+)""".stripMargin
    input.strip match {
      case pattern.r(items, op, divisor, throwTrue, throwFalse) =>
        Monkey(
          items.split(", ").toSeq.map(_.toLong),
          op match {
            case "old + old" => (old: Long) => old + old
            case "old * old" => (old: Long) => old * old
            case s"old + $n" => (old: Long) => old + n.toInt
            case s"old * $n" => (old: Long) => old * n.toInt
            case _ => throw Exception("Unknown op")
          },
          divisor.toInt,
          throwTrue.toInt,
          throwFalse.toInt,
        )
      case _ => throw Exception(s"Unexpected input")
    }
  }
}

type Op = Long => Long

class Monkey(
  initialItems: Seq[Long],
  op: Op,
  val divisor: Int,
  throwTrue: Int,
  throwFalse: Int,
) {
  var nSteps = 0L
  private val items = initialItems.to(mutable.Stack)

  def hasItems: Boolean = items.nonEmpty
  def receive(item: Long): Unit = items.push(item)
  def step(mod: Int = 1): (Long, Int) = {
    nSteps += 1
    val item =
      if (mod == 1)  // part 1
        op(items.pop) / 3
      else // part 2
        op(items.pop) % mod

    if (item % divisor == 0)
      (item, throwTrue)
    else
      (item, throwFalse)
  }
  def reset(): Unit = {
    nSteps = 0
    items.clear()
    items.pushAll(initialItems.reverse)
  }
}

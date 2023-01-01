package day11

import scala.collection.mutable

object Solution {
  def main(args: Array[String]): Unit = {
    val monkeys = util.Using.resource(io.Source.fromResource("input/11.txt")) { _
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

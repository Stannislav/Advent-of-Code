import scala.collection.mutable

object Day11 {
  def main(args: Array[String]): Unit = {
    val monkeys = util.Using.resource(io.Source.fromFile("2022/input/11.txt")) { _
      .mkString
      .split("\n\n")
      .map(parseMonkey)
      .toList
    }

    for (_ <- Range(0, 20)) {
      for ((monkey, i) <- monkeys.zipWithIndex) {
        while(monkey.hasItems()) {
          val (item, target) = monkey.step()
          monkeys(target).receive(item)
        }
      }
    }
    val part1 = monkeys
      .map(_.nSteps())
      .sorted
      .reverse
      .slice(0, 2)
      .product
    println(s"Part 1: $part1")
  }

  private def parseMonkey(input: String): Monkey = {
    val pattern = """Monkey (\d+):
                    |  Starting items: (.+)
                    |  Operation: new = (.+)
                    |  Test: divisible by (\d+)
                    |    If true: throw to monkey (\d+)
                    |    If false: throw to monkey (\d+)""".stripMargin
    input.strip match {
      case pattern.r(i, items, op, divisor, throwTrue, throwFalse) =>
        Monkey(
          i.toInt,
          items.split(", ").map(_.toInt),
          op match {
            case "old + old" => (old: Int) => old + old
            case "old * old" => (old: Int) => old * old
            case s"old + $n" => (old: Int) => old + n.toInt
            case s"old * $n" => (old: Int) => old * n.toInt
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

type Op = Int => Int

class Monkey(
  i: Int,
  items: Iterable[Int],
  op: Op,
  divisor: Int,
  throwTrue: Int,
  throwFalse: Int,
) {
  private var nInspect = 0
  private val itemStack = items.to(mutable.Stack)

  override def toString(): String = s"Monkey $i: ${itemStack.mkString(", ")}"
  def nSteps(): Int = nInspect
  def hasItems(): Boolean = itemStack.nonEmpty
  def receive(item: Int): Unit = itemStack.push(item)
  def step(): (Int, Int) = {
    val item = op(itemStack.pop) / 3
    nInspect += 1
    if (item % divisor == 0)
      (item, throwTrue)
    else
      (item, throwFalse)
  }
}

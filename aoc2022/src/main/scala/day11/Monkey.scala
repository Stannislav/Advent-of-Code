package day11

import scala.collection.mutable

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

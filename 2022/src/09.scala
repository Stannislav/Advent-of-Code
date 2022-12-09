import scala.collection.mutable.ListBuffer


implicit class TupleAdd(self: (Int, Int)) {
  def +(other: (Int, Int)) = (self._1 + other._1, self._2 + other._2)
}


def follow(tail: (Int, Int), head: (Int, Int)): (Int, Int) = {
  val dx = head._1 - tail._1
  val dy = head._2 - tail._2
  if (dx.abs == 2 || dy.abs == 2)
    (tail._1 + dx.sign, tail._2 + dy.sign)
  else
    tail
}


def run(steps: Seq[(Int, Int)], snakeLength: Int): Int = {
  var rope = ListBuffer.fill(snakeLength)((0, 0))
  var seen = Set(rope.last)
  
  steps.foreach(step => {
    val updatedRope = ListBuffer(rope.head + step)
    rope.tail.foreach(knot => updatedRope += follow(knot, updatedRope.last))
    rope = updatedRope
    seen += rope.last
  })
  
  seen.size
}


@main
def main() = {
  val steps = 
    io.Source
    .fromFile("input/09.txt")
    .getLines
    .toList
    .map {
      case s"$dir $n" => (dir, n.toInt)
      case _ => throw Exception("Invalid command")
    }
    .flatMap((dir, n) => List.fill(n)(
      dir match {
        case "U" => (0, 1)
        case "D" => (0, -1)
        case "L" => (-1, 0)
        case "R" => (1, 0)
        case _ => throw Exception(s"Unknown direction $dir.")
      }
    ))
  
  println(s"Part 1: ${run(steps, 2)}")
  println(s"Part 2: ${run(steps, 10)}")
}

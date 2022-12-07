import util.control.Breaks.*


def solve(input: String, len: Int): Int = {
  for (received <- Range(len, input.length)) {
    if (input.slice(received - len, received).toSet.size == len)
      return received
  }
  
  return -1
}


@main
def main(): Unit = {
  var input = io.Source.fromFile("input/06.txt").mkString

  println(s"Part 1: ${solve(input, 4)}")
  println(s"Part 2: ${solve(input, 14)}")
}
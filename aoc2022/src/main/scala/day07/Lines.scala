package day07

class Lines(lines: List[String]) {
  private var ptr = -1

  def next(): String = {
    ptr += 1
    lines(ptr)
  }

  def done(): Boolean = ptr + 1 == lines.length
}

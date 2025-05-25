package day17

class Jets(seq: String) {
  private var i = 0

  def next(): Char = {
    val c = seq(i)
    i = (i + 1) % seq.length
    c
  }

  def idx: Int = i
  def length: Int = seq.length
}


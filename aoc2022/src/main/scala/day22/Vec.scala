package day22

case class Vec(c1: Int, c2: Int) {
  def +(other: Vec): Vec = Vec(c1 + other.c1, c2 + other.c2)

  def +(c: Int): Vec = Vec(c1 + c, c2 + c)

  def -(other: Vec): Vec = Vec(c1 - other.c1, c2 - other.c2)

  def -(c: Int): Vec = Vec(c1 - c, c2 - c)

  def *(c: Int): Vec = Vec(c1 * c, c2 * c)

  def /(c: Int): Vec = Vec(c1 / c, c2 / c)
}

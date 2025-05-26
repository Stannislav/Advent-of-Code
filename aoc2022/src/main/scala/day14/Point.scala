package day14

case class Point(x: Int, y: Int) {
  def down(): Point = this.copy(y = y + 1)

  def downLeft(): Point = this.copy(x = x - 1, y = y + 1)

  def downRight(): Point = this.copy(x = x + 1, y = y + 1)
}

object Point {
  def fromString(s: String): Point = {
    s.split(",").map(_.toInt) match {
      case Array(x: Int, y: Int) => Point(x, y)
      case invalid@_ => throw Exception(s"Invalid point: ${invalid.mkString(",")}")
    }
  }
}

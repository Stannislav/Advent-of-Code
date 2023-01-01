package day09

import scala.annotation.targetName

implicit class TupleAdd(self: (Int, Int)) {
  @targetName("add")
  def +(other: (Int, Int)): (Int, Int) = (self._1 + other._1, self._2 + other._2)

  @targetName("sub")
  def -(other: (Int, Int)): (Int, Int) = (self._1 - other._1, self._2 - other._2)
}

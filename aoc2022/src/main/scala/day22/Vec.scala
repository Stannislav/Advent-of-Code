package day22

import Math.floorMod
import scala.annotation.tailrec

case class Vec(row: Int, col: Int) {
  def +(other: Vec): Vec = Vec(row + other.row, col + other.col)

  def +(c: Int): Vec = Vec(row + c, col + c)

  def -(other: Vec): Vec = Vec(row - other.row, col - other.col)

  def -(c: Int): Vec = Vec(row - c, col - c)

  def *(c: Int): Vec = Vec(row * c, col * c)

  def /(c: Int): Vec = Vec(row / c, col / c)

  /** Rotate vector clock-wise by multiples of 90 degrees.
    *
    * @param times The number of clock-wise rotations by 90 degrees.
    * @return      The rotated vector.
    */
  @tailrec
  final def turnBackwards(times: Int): Vec = {
    if (times == 0) {
      this.copy()
    } else {
      Vec(col, -row).turnBackwards(floorMod(times - 1, 4))
    }
  }

  /** Rotate a face of size `(scale, scale)` located at `(0, 0)` around its centre
    * by multiples of 90 degrees, i.e. after the rotation its top left corner is
    * still at (0, 0).
    *
    * @param times The number of clock-wise rotations by 90 degrees.
    * @param scale The size of the face.
    * @return
    */
  @tailrec
  final def turnFaceBackwards(times: Int, scale: Int): Vec = {
    if (!(0 <= row && row < scale && 0 <= col && col < scale))
      throw RuntimeException(s"Trying to rotate vector $this, which is not part of face with scale $scale")
    if (times == 0) {
      this.copy()
    } else {
      // Note the shift in the second coordinate to bring the vector back
      // to the original quadrant.
      Vec(col, -row + scale - 1).turnFaceBackwards(floorMod(times - 1, 4), scale)
    }
  }
}

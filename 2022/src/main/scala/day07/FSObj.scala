package day07

import scala.collection.mutable.ListBuffer

sealed abstract class FSObj {
  def get_size(): Int
  def children(): ListBuffer[FSObj]
}

class Dir extends FSObj {
  private val _children: ListBuffer[FSObj] = ListBuffer()

  def get_size(): Int = _children.map(child => child.get_size()).sum
  def children(): ListBuffer[FSObj] = _children
  def add_child(child: FSObj): Unit = _children.addOne(child)
}

class File(size: Int) extends FSObj {
  private val _children: ListBuffer[FSObj] = ListBuffer()

  def get_size(): Int = size
  def children(): ListBuffer[FSObj] = _children
}

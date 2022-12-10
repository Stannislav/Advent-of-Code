import scala.collection.mutable.ListBuffer

trait FSObj {
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

class Lines(lines: List[String]) {
  private var ptr = -1
  
  def next(): String = {
    ptr += 1
    lines(ptr)
  }
  
  def done(): Boolean = ptr + 1 == lines.length
}


object Day07 {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromFile("2022/input/07.txt")) { _
      .getLines
      .toList
    }
    val lines = Lines(input)
    val s"$$ cd $_" = lines.next(): @unchecked
    val root = Dir()
    val dir_idx: ListBuffer[Dir] = ListBuffer(root)
    parseDir(root, lines, dir_idx)

    val part1 = dir_idx.map(_.get_size()).filter(_ <= 100000).sum
    println(s"Part 1: $part1")

    val have = 70000000 - root.get_size()
    val need = 30000000 - have
    val part2 = dir_idx.map(_.get_size()).filter(_ >= need).min
    println(s"Part 2: $part2")
  }

  private def parseDir(parent: Dir, lines: Lines, dir_idx: ListBuffer[Dir]): Unit = {
    assert(lines.next() == "$ ls")
    val file = "([0-9]+) ([a-z.]+)".r

    while (!lines.done()) {
      lines.next() match {
        case s"dir $_" => // dirs from ls unused - they'll be parsed in $ cd
        case file(size, _) => parent.add_child(File(size.toInt))
        case "$ cd .." => return
        case s"$$ cd $_" =>
          val child = Dir()
          parseDir(child, lines, dir_idx)
          parent.add_child(child)
          dir_idx.addOne(child)
      }
    }
  }
}

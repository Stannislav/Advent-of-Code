package day07

import scala.collection.mutable.ListBuffer


object Solution {
  def main(args: Array[String]): Unit = {
    val input = util.Using.resource(io.Source.fromResource("input/07.txt")) { _
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

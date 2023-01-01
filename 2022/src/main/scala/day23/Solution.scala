package day23

import scala.annotation.tailrec
import scala.collection.mutable

object Solution {
  def main(args: Array[String]): Unit = {
    val elves = util.Using.resource(io.Source.fromResource("input/23.txt")) { stream =>
      parseElves(stream.mkString)
    }

    println(s"Part 1: ${part1(elves)}")
    println(s"Part 2: ${part2(elves)}")
  }

  private def part1(elves: Set[(Int, Int)]): Int = {
    val (newElves, _) = evolve(elves, 10)
    score(newElves)
  }

  private def part2(elves: Set[(Int, Int)]): Int = {
    val (_, doneRounds) = evolve(elves)
    doneRounds
  }

  private def evolve(elves: Set[(Int, Int)], maxRounds: Int = -1): (Set[(Int, Int)], Int) = {
    val neighbours = List(
      (-1, -1), (-1, 0), (-1, 1),
      (0, -1), (0, 1),
      (1, -1), (1, 0), (1, 1),
    )
    val look = Map(
      0 -> Set(neighbours(0), neighbours(1), neighbours(2)), // N
      1 -> Set(neighbours(5), neighbours(6), neighbours(7)), // S
      2 -> Set(neighbours(0), neighbours(3), neighbours(5)), // W
      3 -> Set(neighbours(2), neighbours(4), neighbours(7)), // E
    )
    val move = Map(
      0 -> neighbours(1),
      1 -> neighbours(6),
      2 -> neighbours(3),
      3 -> neighbours(4),
    )

    def add(p1: (Int, Int), p2: (Int, Int)): (Int, Int) =
      (p1._1 + p2._1, p1._2 + p2._2)

    @tailrec
    def iter(elves: Set[(Int, Int)], dir: Int = 0, doneRounds: Int = 0): (Set[(Int, Int)], Int) = {
      if (maxRounds >= 0 && doneRounds >= maxRounds)
        return (elves, doneRounds)

      val proposed = mutable.Map[(Int, Int), (Int, Int)]()
      val clashed = mutable.Set[(Int, Int)]()

      // First part
      elves.foreach(elf => {
        if (neighbours.exists(dElf => elves.contains(add(elf, dElf)))) {
          val canPropose = Range(dir, dir + 4)
            .map(_ % 4)
            .filter(
              dir => look(dir).forall(dElf => !elves.contains(add(elf, dElf)))
            )
          if (canPropose.nonEmpty) {
            val dest = add(elf, move(canPropose.head))
            if (proposed.contains(dest))
              clashed.add(dest)
            else
              proposed(dest) = elf
          }
        }
      })

      // Second part
      clashed.foreach(dest => proposed.remove(dest))
      if (proposed.nonEmpty) {
        val newElves = elves.removedAll(proposed.values) ++ proposed.keys
        iter(newElves, (dir + 1) % 4, doneRounds + 1)
      } else
        (elves, doneRounds + 1)
    }

    iter(elves)
  }

  private def score(elves: Set[(Int, Int)]): Int = {
    val minRow = elves.map(_._1).min
    val maxRow = elves.map(_._1).max
    val minCol = elves.map(_._2).min
    val maxCol = elves.map(_._2).max

    (maxRow - minRow + 1) * (maxCol - minCol + 1) - elves.size
  }

  private def parseElves(input: String): Set[(Int, Int)] = {
    input
      .linesIterator
      .zipWithIndex
      .flatMap((line, row) => line.zipWithIndex.filter(_._1 == '#').map((*, col) => (row, col)))
      .toSet
  }
}

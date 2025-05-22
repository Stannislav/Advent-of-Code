// TODO: Clean up.
package day17

import scala.annotation.targetName
import scala.collection.mutable

type Point = (Int, Int)

object Solution {
  def main(args: Array[String]): Unit = {
    val jet = util.Using.resource(io.Source.fromResource("input/17.txt")) {
      _.mkString.strip.toList.map {
        case '<' => (0, -1)
        case '>' => (0, 1)
        case cmd@_ => throw Exception(s"Unknown command: $cmd")
      }
    }

    val rocks = List(
      parseRock("####"),
      parseRock(".#.\n###\n.#."),
      parseRock("..#\n..#\n###"),
      parseRock("#\n#\n#\n#"),
      parseRock("##\n##"),
    )
//    rocks foreach { shape => println(shape.points) }

    val part1 = run(2022, jet, rocks)
    println(s"Part 1: $part1")
  }
}

def run(nRocks: Int, jet: Seq[Point], rocks: Seq[Set[Point]]): Int = {
  val chamber = mutable.Set[Point]()
  val jetIter = Iterator.continually(jet.iterator).flatten

  Range(0, nRocks).map(i => rocks(i % rocks.length)).foreach(initialRock => {

    var resting = false
    var rock = shift(initialRock, ((chamber.map(_._1) + -1).max + 4, 2))

    //      println("New rock")
    //      show(chamber.toSet, rock)
    //      io.StdIn.readLine("Continue?")

    while (!resting) {
      // apply jet
      val dp = jetIter.next
      //        println(s"Jet with ${if (dp == (0, 1)) '>' else '<'}")
      //        io.StdIn.readLine("Continue?")

      rock = shift(rock, dp)
      if (hitWall(rock) || collide(chamber, rock)) {
        rock = unshift(rock, dp)
        //          println("(fail)")
      }

      //        show(chamber.toSet, rock)

      // fall
      //        println("Fall")
      //        io.StdIn.readLine("Continue?")
      rock = shift(rock, (-1, 0))
      if (hitBottom(rock) || collide(chamber, rock)) {
        rock = unshift(rock, (-1, 0))
        resting = true

        //          println("Can't fall")
        //          show(chamber.toSet, rock)

        chamber.addAll(rock)
      } //else
      //          show(chamber.toSet, rock)
    }

  })
  chamber.map(_._1).max + 1
}

def shift(shape: Set[Point], dp: Point): Set[Point] = shape.map((row, col) => (row + dp._1, col + dp._2))
def unshift(shape: Set[Point], dp: Point): Set[Point] = shape.map((row, col) => (row - dp._1, col - dp._2))
def hitWall(shape: Set[Point]) = shape.exists((*, col) => col < 0 || col >= 7)
def hitBottom(shape: Set[Point]) = shape.exists((row, *) => row < 0)
def collide(chamber: mutable.Set[Point], shape: Set[Point]) = chamber.intersect(shape).nonEmpty

def show(chamber: Set[Point], shape: Set[Point]): Unit = {
  println("+-------+")
  val height = List(shape.map(_._1).max, (chamber.map(_._1) + 0).max).max
  for (row <- Range(0, height + 2)) {
    print("|")
    for (col <- Range(0, 7)) {
      if (chamber.contains(row, col))
        print("#")
      else if (shape.contains(row, col))
        print("@")
      else
        print(".")
    }
    println("|")
  }
  println()
}

//implicit class TupleAdd(self: Point) {
//  @targetName("add")
//  def +(other: Point): Point = (self._1 + other._1, self._2 + other._2)
//
//  @targetName("sub")
//  def -(other: Point): Point = (self._1 - other._1, self._2 - other._2)
//}

def parseRock(drawing: String): Set[Point] = {
  drawing
    .strip
    .split("\n")
    .toList
    .reverse // turn upside down
    .zipWithIndex
    .flatMap((line, row) =>
      line.zipWithIndex.collect {case (c: Char, col: Int) if c == '#' => (row, col)}
    )
    .toSet
}

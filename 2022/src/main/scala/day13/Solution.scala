package day13

import scala.collection.mutable

object Solution {
  def main(args: Array[String]): Unit = {
    val packets = util.Using.resource(io.Source.fromResource("input/13.txt")) {
      _
        .getLines
        .filter(_.nonEmpty)
        .map(parsePacket)
        .toList
    }
    val packetPairs = Range(0, packets.length, 2).map(idx => (packets(idx), packets(idx + 1))).toList

    val part1 = Range(0, packetPairs.length)
      .filter(idx => compare(packetPairs(idx)._1, packetPairs(idx)._2) != 1)
      .map(_ + 1)
      .sum
    println(s"Part 1: $part1")

    val newPackets = packets
      .concat(Seq(parsePacket("[[2]]"), parsePacket("[[6]]")))
      .sortWith(compare(_, _) == -1)
    val part2 = Range(0, newPackets.length)
      .filter(idx => newPackets(idx).toString == "[[2]]" || newPackets(idx).toString == "[[6]]")
      .map(_ + 1)
      .product
    println(s"Part 2: $part2")
  }

  private def compare(p1: Packet, p2: Packet): Int = {
    (p1, p2) match {
      case (i1: IntPacket, i2: IntPacket) =>
        if (i1.value == i2.value)
          0
        else
          (i1.value - i2.value) / Math.abs(i1.value - i2.value)
      case (i: IntPacket, seq: SeqPacket) => compare(SeqPacket(Seq(i)), seq)
      case (seq: SeqPacket, i: IntPacket) => compare(seq, SeqPacket(Seq(i)))
      case (seq1: SeqPacket, seq2: SeqPacket) =>
        val n = Math.min(seq1.seq.length, seq2.seq.length)
        var idx = 0
        while (idx < n) {
          val cmp = compare(seq1.seq(idx), seq2.seq(idx))
          if (cmp != 0)
            return cmp
          idx += 1
        }
        compare(IntPacket(seq1.seq.length), IntPacket(seq2.seq.length))
      case _ => throw Exception("Unexpected case")
    }
  }

  private def parsePacket(line: String): Packet = {
    val digit = raw"(\d+)"
    line match {
      case digit.r(num) => IntPacket(num.toInt)
      case s"[$inside]" =>
        val elems = mutable.ListBuffer[Packet]()
        var level = 0
        var startIdx = 0
        for ((c, idx) <- inside.zipWithIndex) {
          c match {
            case '[' => level += 1
            case ']' => level -= 1
            case ',' =>
              if (level == 0) {
                elems.append(parsePacket(inside.slice(startIdx, idx)))
                startIdx = idx + 1
              }
            case _ =>
          }
        }
        if (startIdx != inside.length)
          elems.append(parsePacket(inside.slice(startIdx, inside.length)))
        SeqPacket(elems.toList)
      case unknown: String => throw Exception(s"Invalid packet: $unknown")
    }
  }
}

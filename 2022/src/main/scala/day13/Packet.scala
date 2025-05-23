package day13

// TODO:
//   Ugly: Need to wrap List/Int into classes to define recursive container type...
//   All I want is "type Packet = Seq[Packet] | Int"... there must be a better way.
sealed abstract class Packet

class SeqPacket(val seq: Seq[Packet]) extends Packet {
  override def toString: String = s"[${seq.mkString(",")}]"
}

class IntPacket(val value: Int) extends Packet {
  override def toString: String = value.toString
}

"""Solutions for day 16."""
import math
from dataclasses import dataclass
from typing import Protocol, Sequence


class Packet(Protocol):
    """A generic packet protocol."""

    @property
    def version(self) -> int:
        """Get the packet version."""

    @property
    def type_id(self) -> int:
        """Get the packet type ID."""

    @property
    def value(self) -> int:
        """Get the packet value."""

    @property
    def version_sum(self) -> int:
        """Get the sum the packet version and all its sub-packets' versions."""


@dataclass(frozen=True)
class LiteralPacket:
    """A literal packet."""

    version: int
    type_id: int
    value: int

    @property
    def version_sum(self) -> int:
        """Get the sum the packet version and all its sub-packets' versions."""
        return self.version


@dataclass(frozen=True)
class OperatorPacket:
    """An operator packet."""

    version: int
    type_id: int
    packets: Sequence[Packet]

    @property
    def value(self) -> int:
        """Compute the packet value by applying the operator on sub-packets."""
        values: list[int] = [packet.value for packet in self.packets]
        match self.type_id:
            case 0:
                return sum(values)
            case 1:
                return math.prod(values)
            case 2:
                return min(values)
            case 3:
                return max(values)
            case 5:
                v1, v2 = values
                return int(v1 > v2)
            case 6:
                v1, v2 = values
                return int(v1 < v2)
            case 7:
                v1, v2 = values
                return int(v1 == v2)
            case _:
                raise ValueError(f"Unknown packet type: {self.type_id}")

    @property
    def version_sum(self) -> int:
        """Get the sum the packet version and all its sub-packets' versions."""
        return self.version + sum(packet.version_sum for packet in self.packets)


class Bits:
    """A sequence of bits."""

    def __init__(self, data: str) -> None:
        """Initialise the bit sequence.

        Args:
            data: The bits encoded as a hexadecimal sequence.
        """
        self.bits = "".join(f"{int(c, base=16):04b}" for c in data.strip())
        self.ptr = 0

    def read(self, n: int) -> int:
        """Read n bits from the stream and convert them to an integer.

        Args:
            n: The number of bits to read from the stream.

        Returns:
            The n bits read from the stream converted to an integer.
        """
        ret = int(self.bits[self.ptr : self.ptr + n], base=2)
        self.ptr += n

        return ret

    def read_literal(self) -> int:
        """Read the body of a literal packet.

        Returns:
            The value of the literal packet.
        """
        value = 0
        flag = 1
        while flag:
            flag = self.read(1)
            value = (value << 4) | self.read(4)

        return value

    def read_operator(self) -> list[Packet]:
        """Read the body of an operator packet.

        Returns:
            The sequence of sub-packets of the operator packet.
        """
        length_type = self.read(1)
        if length_type == 0:
            total_length = self.read(15)
            start = self.ptr
            packets = []
            while self.ptr - start < total_length:
                packet = self.read_packet()
                packets.append(packet)
        else:
            n_packets = self.read(11)
            packets = [self.read_packet() for _ in range(n_packets)]

        return packets

    def read_packet(self) -> Packet:
        """Read a packet.

        Returns:
            The packet read from the bit stream.
        """
        version = self.read(3)
        type_id = self.read(3)
        if type_id == 4:
            return LiteralPacket(version, type_id, self.read_literal())
        else:
            return OperatorPacket(version, type_id, self.read_operator())

    def remainder(self) -> str:
        """The bits that have not been read yet."""
        return self.bits[self.ptr :]


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    bits = Bits(data_s)
    packet = bits.read_packet()
    if not all(c == "0" for c in bits.remainder()):
        raise RuntimeError(f"A non-zero bit stream remainder: {bits.remainder()}")

    return packet.version_sum, packet.value

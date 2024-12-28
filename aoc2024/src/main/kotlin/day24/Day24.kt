package day24

import java.io.File
import java.io.InputStream

fun main() {
    val device = Device.fromStream(File("input/24.txt").inputStream())
    println("Part 1: ${part1(device)}")
    println("Part 2: ${part2(device)}")
}

fun part1(device: Device): Long {
    val outputWires = device.gates.filter { it.out.startsWith("z") }.map { it.out }
    return outputWires
        .sorted()
        .map(device::getValue)
        .reversed()
        .fold(0L) { acc, value -> (acc shl 1) or value.toLong() }
}

/**
 * Find which four pairs of output wires need to be swapped so that
 * the device computes the sum of its x and y inputs.
 *
 * The solution was found by first iterating through all 44 input bit positions and
 * finding those for which the given device doesn't compute the correct sum.
 *
 * Each bit sum is computed by the same logical bit operations. So, once we know
 * which bits don't sum correctly we can reconstruct the corresponding logical
 * gates and wires from the input and manually find the error.
 *
 * The bit summing blocks have the following form:
 *
 * ```text
 *                +---+   +---+       +---+
 *                |   |-+-|and|-------|   |
 * +---+          +---+ | +---+       +---+
 * | x |-+              |               |
 * +---+ | +---+  +---+ | +---+  +---+  |
 *       |-|xor|--|   |-+-|xor|--| z |  |
 * +---+ | +---+  +---+   +---+  +---+  |
 * | y |-|                              |
 * +---+ | +---+  +---+               +---+    +---+
 *       +-|and|--|   |---------------| or|----|   |
 *         +---+  +---+               +---+    +---+
 *
 * ```
 *
 * Blank boxes are gate names that are different for each bit. The blank gates
 * on the top left and bottom right connect to the previous and next bit
 * blocks, respectively.
 *
 * There must be an algorithmic way of finding the gates to swap. Will
 * work this out later or check on Reddit.
 */
fun part2(brokenDevice: Device): String {
    val toSwap = listOf(
        Pair("z10", "ggn"),
        Pair("ndw", "jcb"),
        Pair("grm", "z32"),
        Pair("twr", "z39"),
    )
    val device = toSwap.fold(brokenDevice) { it, wirePair -> it.swap(wirePair.first, wirePair.second) }
    // Find which bits don't sum correctly. If the swaps above are correct, then no errors should be reported.
    for (shift in 0..44) {
        val num = 1L shl shift
        val result = device.runWith(num, num)
        if (result != num + num) {
            println("Error at bit $shift: expected $num + $num = ${num + num}, but got $result")
        }
    }
    return toSwap.flatMap { it.toList() }.sorted().joinToString(",")
}

data class Gate(val op: (Int, Int) -> Int, val in1: String, val in2: String, val out: String) {
    fun run(getValue: (String) -> Int): Int {
        return op(getValue(in1), getValue(in2))
    }
}

data class Device (val inputValues: Map<String, Int>, val gates: List<Gate>) {
    private val values = inputValues.toMutableMap()
    companion object {
        fun fromStream(stream: InputStream): Device {
            val (top, bottom) = stream.bufferedReader().readText().split("\n\n")
            val inputValues = top.trim().lineSequence().map {
                val (k, v) = it.split(": ")
                k to v.toInt()
            }.toMap()
            val pattern = "([0-9a-z]+) (AND|OR|XOR) ([0-9a-z]+) -> ([0-9a-z]+)".toRegex()
            val gates = bottom.trim().lineSequence().map {
                val match = pattern.matchEntire(it)
                val (_, in1, kind, in2, out) = match?.groupValues ?: error("Can't match line: $it")
                val op = when(kind) {
                    "AND" -> Int::and
                    "OR" -> Int::or
                    "XOR" -> Int::xor
                    else -> error("Unknown gate kind: $kind")
                }
                Gate(op, in1, in2, out)
            }.toList()
            return Device(inputValues, gates)
        }
    }

    fun getValue(wire: String): Int = values.getOrPut(wire) { gates.first { it.out == wire }.run(::getValue) }

    fun swap(out1: String, out2: String): Device {
        return this.copy(gates = gates.map { gate ->
            when (gate.out) {
                out1 -> gate.copy(out = out2)
                out2 -> gate.copy(out = out1)
                else -> gate
            }
        })
    }

    fun runWith(x: Long, y: Long): Long {
        values.clear()
        values.putAll(makeInput(x, "x"))
        values.putAll(makeInput(y, "y"))
        return gates
            .map { it.out }
            .filter { it.startsWith("z") }
            .sorted()
            .map(::getValue)
            .reversed()
            .fold(0L) { acc, value -> (acc shl 1) or value.toLong() }
    }

    private fun makeInput(num: Long, prefix: String): Map<String, Int> {
        require(prefix == "x" || prefix == "y") { "Unknown input prefix: $prefix" }
        val bits = toBits(num)
        val allWireNames = inputValues.keys.filter { it.startsWith(prefix) }.sorted()
        require(bits.size <= allWireNames.size)
        val setWires = allWireNames.zip(bits).toMap()
        return allWireNames.associateWith { setWires.getOrDefault(it, 0) }
    }

    private fun toBits(num: Long): List<Int> {
        val bits = mutableListOf<Int>()
        var rem = num
        while (rem != 0L) {
            bits.add((rem and 1).toInt())
            rem = rem shr 1
        }
        return bits
    }
}

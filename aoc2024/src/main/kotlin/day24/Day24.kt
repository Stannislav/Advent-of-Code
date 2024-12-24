package day24

import java.io.File
import java.io.InputStream

fun main() {
    val device = Device.fromStream(File("input/24.txt").inputStream())
    println("Part 1: ${part1(device)}")
}

fun part1(device: Device): Long {
    val outputWires = device.gates.filter { it.out.startsWith("z") }.map { it.out }
    return outputWires
        .sorted()
        .map(device::getValue)
        .reversed()
        .fold(0L) { acc, value -> (acc shl 1) or value.toLong() }
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
}

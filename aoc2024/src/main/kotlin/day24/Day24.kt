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

fun part2(device: Device): String {
    val outputWires = device.gates.filter { it.out.startsWith("z") }.map { it.out }
    outputWires.sorted().map(device::getValue)
    println(device.values.values.count { it == 1 })
    println(device.values.values.count { it == 0 })
    println(device.inputValues)
//    return outputWires
//        .sorted()
//        .map(device::getValue)
//        .reversed()
//        .fold(0L) { acc, value -> (acc shl 1) or value.toLong() }
    val x = toDecimal(device.values, "x")
    val y = toDecimal(device.values, "y")
    println(x)
    println(y)
    println(x + y)
    val have = device.values.filterKeys { it.startsWith("z") }
    val want = toBinary(x + y)
    println(have)
    println(want)
    println((x + y).toString(2))
    println(toDecimal(device.values, "z").toString(2))
    val badOutputs = want.filter { it.value != have.getValue(it.key) }.keys.toList()
    println(badOutputs)
    val significant = badOutputs.associateWith { findAffectingWires(it, device) }
    println("Significant: $significant")
    val uniqueSignificant = significant.values.reduce { a, b -> a + b }
    println("Unique significant: ${uniqueSignificant.size}")
    device.reset()
    val uniqueSignificantValues = uniqueSignificant.groupBy { device.getValue(it) }.toMap()
    println("Unique significant values: $uniqueSignificantValues (${uniqueSignificantValues.mapValues { it.value.size }})")
    val upstreams = uniqueSignificantValues.values.flatten().toSet().associateWith { upstreamOuts(it, device) }
    println("Upstreams: $upstreams")
    // Only those pairs of 0/1 keys which are not mutually dependent can be swapped.
    val candidatePairs = mutableSetOf<Pair<String, String>>()
    for (key1 in uniqueSignificantValues[1]!!) {
        for (key0 in uniqueSignificantValues[0]!!) {
            if (!upstreams[key1]!!.contains(key0) && !upstreams[key0]!!.contains(key1))
                candidatePairs.add(Pair(key1, key0))
        }
    }
//    val candidateFlips = mutableSetOf<Set<String>>()
//    val candidatePairsList = candidatePairs.toList()
//    for (i1 in candidatePairsList.indices) {
//        for (i2 in i1 + 1 until candidatePairsList.size) {
//            for (i3 in i2 + 1 until candidatePairsList.size) {
//                for (i4 in i3 + 1 until candidatePairsList.size) {
//                    val p1 = candidatePairsList[i1]
//                    val p2 = candidatePairsList[i2]
//                    val p3 = candidatePairsList[i3]
//                    val p4 = candidatePairsList[i4]
//                    val flipSet = setOf(p1.first, p1.second, p2.first, p2.second, p3.first, p3.second, p4.first, p4.second)
//                    if (flipSet.size == 8) {
//                        val attempt = outputWires.sorted().map { it to device.getValueWithFlip(it, flipSet) }.toMap()
//                        println("---")
//                        println(want)
//                        println(attempt)
//                        println(want == attempt)
//                        if (want == attempt)
//                            return flipSet.sorted().joinToString(",")
//                    }
//                }
//            }
//        }
//    }
//    println("Flip sets count: ${candidateFlips.size}")
    println("Candidate pairs: $candidatePairs (size: ${candidatePairs.size})")

    // For each bad output find unique significant wires
    val unique = significant.mapValues {
        it.value - significant.filterKeys { key -> it.key != key }.values.reduce { acc, value -> acc + value }
    }
    println("Unique: $unique")
    println("Number of gates: ${device.gates.size}")
    // Any output bit zN should only depend on xM and yK with M <= N and K <=N.
    // Likewise, any input bits xM, yK should only affect output bits zN with M <= N and K <= N
    val xs = device.gates.flatMap { listOf(it.in1, it.in2) }.filter { it.startsWith("x") }.toSet()
    val ys = device.gates.flatMap { listOf(it.in1, it.in2) }.filter { it.startsWith("x") }.toSet()
    val zs = device.gates.map { it.out }.filter { it.startsWith("z") }.toSet()
    println("xs = ${xs.sorted()}")
    println("ys = ${ys.sorted()}")
    println("zs = ${zs.sorted()}")
    val xAffected = xs.associateWith { findAffectedOutputs(it, device).sorted() }
    val yAffected = ys.associateWith { findAffectedOutputs(it, device).sorted() }
    // Find which zs are wrongly affected.
    val xAffectedWrong = xAffected.mapValues { entry ->
        val keyN = entry.key.substring(1).toInt()
        entry.value.filter { it.substring(1).toInt() < keyN }
    }
    println(xAffected)
    println(xAffectedWrong)
    println("Total count of bad x targets: ${xAffectedWrong.values.sumOf { it.size }}")
    val yAffectedWrong = yAffected.mapValues { entry ->
        val keyN = entry.key.substring(1).toInt()
        entry.value.filter { it.substring(1).toInt() < keyN }
    }
    println(yAffected)
    println(yAffectedWrong)
    println("Total count of bad y targets: ${yAffectedWrong.values.sumOf { it.size }}")
    // It turns out there are no bad targets.
    return ""
}

fun findAffectedOutputs(input: String, device: Device): Set<String> {
    fun recurse(wire: String): Set<String> {
        if (wire.startsWith("z"))
            return setOf(wire)
        return device.gates.filter { it.in1 == wire || it.in2 == wire }.flatMap { recurse(it.out) }.toSet()
    }

    return recurse(input)
}

fun findAffectingWires(key: String, device: Device): Set<String> {
    device.reset()
    val value = device.getValue(key)
    return device.values.keys
        .filter { !it.startsWith("z") && !it.startsWith("x") && !it.startsWith("y") }
        .filter { device.getValueWithFlip(key, setOf(it)) != value }
        .toSet()
}

fun upstreamOuts(out: String, device: Device): Set<String> {
    fun recurse(gate: Gate?): Set<String> {
        if (gate == null) {
            return setOf()
        }
        return setOf(gate.in1, gate.in2) + recurse(device.gates.firstOrNull { it.out == gate.in1 }) + recurse(device.gates.firstOrNull { it.out == gate.in2 })
    }
    return recurse(device.gates.first { it.out == out })
        .filter { !it.startsWith("x") && !it.startsWith("y")}
        .toSet()
}

fun toBinary(x: Long): Map<String, Int> = x
    .toString(2)
    .reversed()
    .mapIndexed{ idx, c -> "z${idx.toString().padStart(2, '0')}" to c.digitToInt() }
    .toMap()

fun toDecimal(values: Map<String, Int>, prefix: String): Long {
    return values
        .keys
        .filter { it.startsWith(prefix) }
        .sorted()
        .map(values::getValue)
        .reversed()
        .fold(0L) { acc, value -> (acc shl 1) or value.toLong() }
}

data class Gate(val op: (Int, Int) -> Int, val in1: String, val in2: String, val out: String) {
    fun run(getValue: (String) -> Int): Int {
        return op(getValue(in1), getValue(in2))
    }
}


data class Device (val inputValues: Map<String, Int>, val gates: List<Gate>) {
    val values = inputValues.toMutableMap()
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

    fun reset() {
        values.clear()
        values.putAll(inputValues)
    }

    fun getValueWithFlip(wire: String, flip: Set<String>): Int {
        reset()
        fun newGetValue(w: String): Int = values.getOrPut(w) {
            val result = gates.first { it.out == w }.run(::newGetValue)
            if (flip.contains(w))
                1 - result
            else
                result
        }
        return newGetValue(wire)
    }

    fun swap(out1: String, out2: String): Device {
        return this.copy(gates = gates.map { gate ->
            when (gate.out) {
                out1 -> gate.copy(out = out2)
                out2 -> gate.copy(out = out1)
                else -> gate
            }
        })
    }
}

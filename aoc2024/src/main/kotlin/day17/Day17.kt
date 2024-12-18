package day17

import java.io.File
import java.io.InputStream

data class Computer(val a: Long, val b: Long, val c: Long, val program: List<Long>) {
    private var ptr = 0
    private var regA = 0L
    private var regB = 0L
    private var regC = 0L
    private val out = mutableListOf<Long>()

    init { reset() }

    val getA = { regA }
    val getB = { regB }

    fun reset() {
        ptr = 0
        regA = a
        regB = b
        regC = c
        out.clear()
    }

    fun run(): Computer {
        while (ptr < program.size) { step() }
        return this
    }

    fun next(): Long = program[ptr++]

    private fun step() {
        val op = next()
        val n = next()
        when (op) {
            0L -> regA = regA shr combo(n).toInt()
            1L -> regB = regB xor n
            2L -> regB = combo(n) and 0b111
            3L -> if (regA > 0) ptr = combo(n).toInt()
            4L -> regB = regB xor regC
            5L -> out.add(combo(n) and 0b111)
            6L -> regB = regA shr combo(n).toInt()
            7L -> regC = regA shr combo(n).toInt()
            else -> error("Unknown op: $op")
        }
    }

    private fun combo(n: Long) = if (n < 4) n else when(n) {
        4L -> regA
        5L -> regB
        6L -> regC
        else -> error("Unknown operand: $n")
    }

    fun readout(): List<Long> = out

    fun findReplicating(): Long {
        val seeds = mutableMapOf<Long, List<Long>>().withDefault { listOf() }
        (0L until 1024L).forEach {
            val x = this.copy(a = it).run().readout().first()
            seeds[x] = seeds.getValue(x) + it
        }

        fun findParts(ptr: Int, next: List<Long> = listOf()): List<Long>? {
            if (ptr == -1) return next

            val candidates = seeds.getValue(program[ptr])
            for (candidate in candidates) {
                if (next.size >= 1 && (candidate shr 3 != next[0] and 0b1111111)) {
                    continue
                }
                if (next.size > 2 && (candidate shr 6 != next[1] and 0b1111)) {
                    continue
                }
                if (next.size > 3 && (candidate shr 9 != next[2] and 0b1)) {
                    continue
                }
                findParts(ptr - 1, listOf(candidate) + next)?.let { return it }
            }
            return null
        }

        val parts = findParts(program.size - 1) ?: error("No solution found")
        var a = 0L
        for (i in parts.indices)
            a = a or (parts[i] shl (3 * i))
        return a
    }
}

fun main() {
    val computer = parseInput(File("input/17.txt").inputStream())
    println("Part 1: ${part1(computer)}")
    println("Part 2: ${part2(computer)}")
}

fun parseInput(stream: InputStream): Computer {
    val pattern = """
        Register A: (\d+)
        Register B: (\d+)
        Register C: (\d+)

        Program: ([\d,]+)
    """.trimIndent().toRegex()
    val values = pattern.matchEntire(stream.bufferedReader().readText().trim())?.groupValues ?: error("Can't match input")
    return Computer(values[1].toLong(), values[2].toLong(), values[3].toLong(), values[4].split(",").map { it.toLong() })
}

fun part1(computer: Computer): String {
    return computer.run().readout().joinToString(",")
}

fun part2(computer: Computer): Long {
    return computer.findReplicating()
}

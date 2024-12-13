package day13

import java.io.File
import java.io.InputStream

fun main() {
    val machines = parseInput(File("input/13.txt").inputStream())
    println("Part 1: ${part1(machines)}")
    println("Part 2: ${part2(machines)}")
}

data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val x: Long, val y: Long) {
    private val shift = 10000000000000L
    val enhance = { copy(x = x + shift, y = y + shift) }

    companion object {
        fun fromString(str: String): Machine {
            val match = "(\\d+)".toRegex().findAll(str)
            val ints = match.map { it.value.toLong() }.toList()
            check(ints.size == 6)
            return Machine(ints[0], ints[1], ints[2], ints[3], ints[4], ints[5])
        }
    }

    fun getCost(): Long? {
        val det = ax * by - ay * bx
        if (det == 0L) error("Buttons are collinear")

        val inv = Pair(by * x - bx * y, -ay * x + ax * y)
        return if (inv.first % det == 0L && inv.second % det == 0L)
            (3 * inv.first + inv.second) / det
        else
            null
    }
}

fun parseInput(stream: InputStream): List<Machine> {
    return stream.bufferedReader().readText().split("\n\n").map(Machine::fromString)
}

fun part1(machines: List<Machine>): Long {
    return machines.mapNotNull { it.getCost() }.sum()
}

fun part2(machines: List<Machine>): Long {
    return machines.map { it.enhance() }.mapNotNull { it.getCost() }.sum()
}

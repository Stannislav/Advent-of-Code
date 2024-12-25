package day25

import java.io.File
import java.io.InputStream

fun main() {
    val (locks, keys) = parseInput(File("input/25.txt").inputStream())
    println("Part 1: ${part1(locks, keys)}")
}

fun part1(locks: Set<List<Int>>, keys: Set<List<Int>>): Int {
    var count = 0
    for (lock in locks) {
        for (key in keys) {
            if (lock.zip(key).all { it.first + it.second <=5 })
                count++
        }
    }
    return count
}

fun parseInput(stream: InputStream): Pair<Set<List<Int>>, Set<List<Int>>> {
    val blocks = stream.bufferedReader().readText().split("\n\n")
    val locks = mutableSetOf<List<Int>>()
    val keys = mutableSetOf<List<Int>>()
    blocks.forEach { block ->
        val lines = block.trim().lines()
        require(lines.size == 7) { "expected a block of 7 lines, but got one with ${lines.size}:\n${block}" }
        val counts = transpose(lines.slice(1 until lines.lastIndex)).map { it.count { c -> c == '#' }}
        if (lines[0].startsWith('#'))
            locks.add(counts)
        else
            keys.add(counts)
    }

    return Pair(locks, keys)
}

fun transpose(lines: List<String>): List<String> {
    val transposed = mutableListOf<String>()
    require(lines.map { it.length }.toSet().size == 1) { "lines are not all the same length: $lines"}
    for (i in 0 until lines[0].length)
        transposed.add(lines.map { it[i] }.toString())
    return transposed
}

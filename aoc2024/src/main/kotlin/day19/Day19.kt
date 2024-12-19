package day19

import java.io.File
import java.io.InputStream

fun main() {
    val (towels, patterns) = parseInput(File("input/19.txt").inputStream())
    println("Part 1: ${part1(towels, patterns)}")
    println("Part 2: ${part2(towels, patterns)}")
}

fun parseInput(stream: InputStream): Pair<List<String>, List<String>> {
    val (top, bottom) = stream.bufferedReader().readText().split("\n\n", limit = 2)
    val towels = top.trim().split(", ")
    val patterns = bottom.trim().split("\n")
    return Pair(towels, patterns)
}

fun part1(towels: List<String>, patterns: List<String>): Int {
    return patterns.count { arrangements(it, towels) != 0L }
}

fun part2(towels: List<String>, patterns: List<String>): Long {
    return patterns.sumOf { arrangements(it, towels) }
}

fun arrangements(pattern: String, towels: List<String>): Long {
    val towelsForChar = towels.groupBy { it[0] }.toMap()
    val cache = mutableMapOf<Int, Long>()

    fun matchAt(idx: Int): Long {
        cache[idx]?.let { return it }
        if (idx == pattern.length) return 1
        val result = towelsForChar[pattern[idx]]
            ?.filter { idx + it.length <= pattern.length }
            ?.filter { pattern.substring(idx, idx + it.length) == it }
            ?.sumOf { matchAt(idx + it.length) }
            ?: 0
        cache[idx] = result
        return result
    }
    return matchAt(0)
}

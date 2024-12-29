package day19

import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 19: Linen Layout ---")
    val day19 = Day19.fromStream(File("input/19.txt").inputStream())
    println("Part 1: ${day19.part1()}")
    println("Part 2: ${day19.part2()}")
}

/**
 * Day 19 solution.
 *
 * Refactoring improvements thanks to users from Reddit:
 *
 * * [Jadarma](https://github.com/Jadarma/advent-of-code-kotlin-solutions/blob/256ca9aef9b08e50332bc772ca0a1364d2c3bfe0/solutions/aockt/y2024/Y2024D19.kt)
 * * [ckainz11](https://github.com/ckainz11/AdventOfCode2024/blob/main/src/main/kotlin/days/day19/Day19.kt)
 */
class Day19(val towels: List<String>, val patterns: List<String>) {
    companion object {
        fun fromStream(stream: InputStream): Day19 {
            val (top, bottom) = stream.bufferedReader().readText().split("\n\n", limit = 2)
            val towels = top.trim().split(", ")
            val patterns = bottom.trim().split("\n")
            return Day19(towels, patterns)
        }
    }

    private val cache = mutableMapOf("" to 1L)
    private fun String.countArrangements(): Long = cache.getOrPut(this) {
        towels
            .filter { startsWith(it) }
            .sumOf { removePrefix(it).countArrangements() }
    }

    fun countAll(): List<Long> = patterns.map { it.countArrangements() }
    fun part1(): Int = countAll().count { it != 0L }
    fun part2(): Long = countAll().sum()
}

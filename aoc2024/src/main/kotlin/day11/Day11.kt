package day11

import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 11: Plutonian Pebbles ---")
    val solution = Day11(File("input/11.txt").inputStream())
    println("Part 1: ${solution.blink(25)}")
    println("Part 2: ${solution.blink(75)}")
}

class Day11(stream: InputStream) {
    val input = stream
        .bufferedReader()
        .readText()
        .trim()
        .split(" ")
        .map { it.toLong() }
    private val memo = mutableMapOf<Pair<Long, Int>, Long>()

    fun blink(nTimes: Int): Long = input.sumOf { countEvolved(it, nTimes) }

    private fun countEvolved(stone: Long, nTimes: Int): Long {
        if (nTimes == 0)
            return 1

        memo[Pair(stone, nTimes)]?.let { return it }

        return if (stone == 0L) {
            countEvolved(1, nTimes - 1)
        } else if (stone.toString().length % 2 == 0) {
            val str = stone.toString()
            val first = str.substring(0, str.length / 2).toLong()
            val second = str.substring(str.length / 2).toLong()
            countEvolved(first, nTimes - 1) + countEvolved(second, nTimes - 1)
        } else {
            countEvolved(stone * 2024, nTimes - 1)
        }.also { memo[Pair(stone, nTimes)] = it }
    }
}

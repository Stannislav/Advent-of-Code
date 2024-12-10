package day10

import common.*
import java.io.File
import java.io.InputStream
import kotlin.math.sign

fun main() {
    val solution = Day10(File("input/10.txt").inputStream())
    println("Part 1: ${solution.part1()}")
    println("Part 2: ${solution.part2()}")
}

class Day10(stream: InputStream) {
    val map = parseInput(stream)
    val dim = computeDim(map)

    fun parseInput(stream: InputStream): Map<Vec, Int> {
        return stream
            .bufferedReader()
            .readLines()
            .flatMapIndexed { i, line -> line.mapIndexed {j, char -> Vec(i, j) to char.digitToInt() } }
            .toMap()
    }

    private fun computeDim(map: Map<Vec, Int>): Vec {
        return map.keys.maxWith { v1, v2 -> (v1 - v2).run { i + j } } + Vec(1, 1)
    }

    fun part1(): Int {
        return 0
    }

    fun part2(): Int {
        return 0
    }
}

package day01

import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun main() {
    val (left, right) = parseInput(File("input/01.txt").inputStream())
    println("Part 1: ${part1(left, right)}")
    println("Part 2: ${part2(left, right)}")
}

fun parseInput(stream: InputStream): Pair<List<Int>, List<Int>> {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    val pattern = Regex("(\\d+)\\s+(\\d+)")
    stream.bufferedReader().forEachLine {
        val match = pattern.matchEntire(it)
        val (l, r) = match!!.destructured
        left.add(l.toInt())
        right.add(r.toInt())
    }

    return Pair(left, right)
}


fun part1(left: List<Int>, right: List<Int>): Int {
    return left.sorted()
        .zip(right.sorted())
        .fold(0) { acc, (l, r) -> acc + abs(l - r) }
}

fun part2(left: List<Int>, right: List<Int>): Int {
    val leftCounts = count(left)
    val rightCounts = count(right)
    var result = 0
    for ((n, cnt) in leftCounts) {
        result += n * cnt * rightCounts.getOrDefault(n, 0)
    }
    return result
}

fun count(seq: Iterable<Int>): Map<Int, Int> {
    val counts = mutableMapOf<Int, Int>()
    seq.forEach { counts[it] = counts.getOrDefault(it, 0) + 1 }
    return counts
}

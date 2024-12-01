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
    val pattern = """(\d+)\s+(\d+)""".toRegex()
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
        .sumOf { (l, r) -> abs(l - r) }
}

fun part2(left: List<Int>, right: List<Int>): Int {
    val leftCounts = left.groupingBy { it }.eachCount()
    val rightCounts = right.groupingBy { it }.eachCount().withDefault { 0 }

    return leftCounts.map { (n, cnt) -> n * cnt * rightCounts.getValue(n) }.sum()
}

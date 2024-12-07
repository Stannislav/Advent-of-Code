package day07

import java.io.File
import java.io.InputStream



fun main() {
    val input = parseInput(File("input/07.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): Map<Long, List<Long>> {
    val input = mutableMapOf<Long, List<Long>>()
    stream.bufferedReader().forEachLine {
        val (key, values) = it.split(": ", limit = 2)
        input[key.toLong()] = values.split(" ").map { x -> x.toLong() }.toList()
    }
    return input
}

fun plus(x: Long, y: Long): Long = x + y
fun times(x: Long, y: Long): Long = x * y
fun cat(x: Long, y: Long): Long = (x.toString() + y.toString()).toLong()

fun part1(input: Map<Long, List<Long>>): Long {
    return calibrate(input, listOf(::plus, ::times))
}

fun part2(input: Map<Long, List<Long>>): Long {
    return calibrate(input, listOf(::plus, ::times, ::cat))
}

fun calibrate(input: Map<Long, List<Long>>, operators: List<(Long, Long) -> Long>): Long {
    fun test(numbers: List<Long>, current: Long, expected: Long): Boolean {
        if (numbers.isEmpty())
            return expected == current
        if (current > expected)
            return false
        return operators.any { test(numbers.drop(1), it(current, numbers.first()), expected) }
    }
    return input.filter { (k, v) -> test(v.drop(1), v.first(), k) }.keys.sum()
}

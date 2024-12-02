package day02

import java.io.File
import java.io.InputStream
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    val input = parseInput(File("input/02.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): List<List<Int>>  {
    val input = mutableListOf<List<Int>>()
    stream
        .bufferedReader()
        .forEachLine { line ->
            input.add(line.split(" ").map { it.toInt() }.toList())
        }
    return input
}

fun part1(reports: List<List<Int>>): Int {
    return reports.filter { isSafe(it) }.size
}

fun part2(reports: List<List<Int>>): Int {
    return reports.filter { isSafe(it) || canTolerate(it) }.size
}

fun isSafe(report: List<Int>): Boolean {
    val grad = report.zipWithNext().map {(a, b) -> b - a}
    val isInRange = grad.all { it.absoluteValue in 1..3 }
    val isMonotonic = grad.zipWithNext().all { (a, b) -> a.sign == b.sign }

    return isInRange && isMonotonic
}

fun canTolerate(report: List<Int>): Boolean {
    for (idxToDrop in report.indices) {
        if (isSafe(report.filterIndexed { idx, _ -> idx != idxToDrop })) {
            return true
        }
    }
    return false
}

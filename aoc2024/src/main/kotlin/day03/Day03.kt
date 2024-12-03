package day03

import java.io.File

fun main() {
    val input = File("input/03.txt").readText()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: String): Int = findAll(input).sumOf { it.first * it.second }
fun part2(input: String): Int = findAllConditional(input).sumOf { it.first * it.second }

fun findAll(input: String): List<Pair<Int,Int>> {
    return """mul\((\d+),(\d+)\)"""
        .toRegex()
        .findAll(input)
        .map {
            Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt())
        }.toList()
}

fun findAllConditional(input: String): List<Pair<Int,Int>> {
    var updatedInput = ""
    var idx = 0
    var enabled = true
    """(do\(\)|don't\(\))""".toRegex().findAll(input).forEach {
        if (enabled)
            updatedInput += input.substring(idx..<it.range.first)
        idx = it.range.last + 1
        when (it.value) {
            "do()" -> enabled = true
            "don't()" -> enabled = false
        }
    }
    if (enabled)
        updatedInput += input.substring(idx..<input.length)
    return findAll(updatedInput)
}

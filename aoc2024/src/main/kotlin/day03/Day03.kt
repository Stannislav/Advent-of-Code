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
    val pattern = """mul\((\d+),(\d+)\)""".toRegex()
    val result = mutableListOf<Pair<Int,Int>>()
    val matches = pattern.findAll(input)
    matches.forEach {
        result.add(Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt()))
    }
    return result
}

fun findAllConditional(input: String): List<Pair<Int,Int>> {
    val pattern = """(do\(\)|don't\(\))""".toRegex()
    val matches = pattern.findAll(input)
    var updatedInput = ""
    var idx = 0
    var enabled = true
    matches.forEach {
        if (enabled) {
            updatedInput += input.substring(idx..<it.range.first)
        }
        when (it.value) {
            "do()" -> enabled = true
            "don't()" -> enabled = false
        }
        idx = it.range.last + 1
    }
    if (enabled) {
        updatedInput += input.substring(idx..<input.length)
    }
    return findAll(updatedInput)
}

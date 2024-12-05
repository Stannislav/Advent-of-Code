package day05

import java.io.File
import java.io.InputStream

fun main() {
    val (rules, updates) = parseInput(File("input/05.txt").inputStream())
    println("Part 1: ${part1(rules, updates)}")
    println("Part 2: ${part2(rules, updates)}")
}

fun part1(rules: Set<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { isValid(it, rules) }.sumOf { it[it.size / 2] }
}

fun part2(rules: Set<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { !isValid(it, rules) }.map { order(it, rules) }.sumOf { it[it.size / 2] }
}

fun isValid(update: List<Int>, rules: Set<Pair<Int, Int>>): Boolean {
    for (i in 0 until update.size - 1) {
        if (!rules.contains(Pair(update[i], update[i + 1])))
            return false
    }
    return true
}

fun order(update: List<Int>, rules: Set<Pair<Int, Int>>): List<Int> {
    return update.sortedWith { a, b -> if (rules.contains(Pair(a, b))) -1 else 1 }
}

fun parseInput(stream: InputStream): Pair<Set<Pair<Int, Int>>, List<List<Int>>> {
    val (sec1, sec2) = stream.bufferedReader().readText().split("\n\n", limit = 2)

    val rules = sec1.trim().lines().map { Pair(it.substring(0, 2).toInt(), it.substring(3, 5).toInt()) }.toSet()
    val updates = sec2.trim().lines().map { line -> line.split(",").map { it.toInt() } }.toList()

    return Pair(rules, updates)
}

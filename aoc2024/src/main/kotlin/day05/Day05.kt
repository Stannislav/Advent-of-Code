package day05

import java.io.File
import java.io.InputStream

fun main() {
    val (rules, updates) = parseInput(File("input/05.txt").inputStream())
    println("Part 1: ${part1(rules, updates)}")
    println("Part 2: ${part2(rules, updates)}")
}

fun parseInput(stream: InputStream): Pair<Set<Pair<Int, Int>>, List<List<Int>>> {
    val (sec1, sec2) = stream.bufferedReader().readText().split("\n\n", limit = 2)

    val rules = sec1.trim().lines().map { Pair(it.substring(0, 2).toInt(), it.substring(3, 5).toInt()) }.toSet()
    val updates = sec2.trim().lines().map { line -> line.split(",").map { it.toInt() } }

    return Pair(rules, updates)
}

fun part1(rules: Set<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { isValid(it, rules) }.sumOf { it[it.size / 2] }
}

fun part2(rules: Set<Pair<Int, Int>>, updates: List<List<Int>>): Int {
    return updates.filter { !isValid(it, rules) }.map { order(it, rules) }.sumOf { it[it.size / 2] }
}

fun isValid(update: List<Int>, rules: Set<Pair<Int, Int>>): Boolean {
    return update.zipWithNext().all { pair -> rules.contains(pair) }
}

fun order(update: List<Int>, rules: Set<Pair<Int, Int>>): List<Int> {
    return update.sortedWith { a, b -> if (rules.contains(Pair(a, b))) -1 else 1 }
}

package day22

import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 22: Monkey Market ---")
    val input = parseInput(File("input/22.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): List<Long> {
    return stream.bufferedReader().readLines().map { it.toLong() }
}

fun nextSecret(n: Long): Long {
    var result = n xor (n * 64) % 16777216
    result = result xor (result / 32) % 16777216
    return result xor (result * 2048) % 16777216
}

fun getChangeToPrice(secretSequence: List<Long>): Map<Int, Int> {
    val prices = secretSequence.map { it % 10 }.map { it.toInt() }
    val diffs = prices.zip(prices.drop(1)).map { (p1, p2) -> p2 - p1 }.toIntArray()
    val changeToPrice = (0 until prices.size - 4).map { diffs.slice(it until it + 4).toTypedArray().contentHashCode() to prices[it + 4] }
    val uniqueChanges = changeToPrice.map { it.first }.toSet()
    return uniqueChanges.associate { changeToPrice.first { element -> element.first == it } }
}

fun part1(secrets: List<Long>): Long {
    return secrets.sumOf { generateSequence(it, ::nextSecret).take(2001).last() }
}

fun part2(secrets: List<Long>): Int {
    val changeToPrices = secrets
        .map { generateSequence(it, ::nextSecret).take(2001).toList() }
        .map(::getChangeToPrice)
    val allChanges = changeToPrices.flatMap { it.keys }.toSet()
    return allChanges.maxOf { change -> changeToPrices.sumOf { it[change] ?: 0 } }
}

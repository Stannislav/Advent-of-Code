package day10

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 10: Hoof It ---")
    val solution = Day10(File("input/10.txt").inputStream())
    println("Part 1: ${solution.part1()}")
    println("Part 2: ${solution.part2()}")
}

class Day10(stream: InputStream) {
    val map = parseInput(stream).withDefault { -1 }
    // `routes` is a map from starting points to a list of summit points reached
    // via distinct routes. List can contain identical summits if they were reached
    // via different routes.
    private val routes = solve()

    fun parseInput(stream: InputStream): Map<Vec, Int> {
        return stream
            .bufferedReader()
            .readLines()
            .flatMapIndexed { i, line -> line.mapIndexed {j, char -> Vec(i, j) to char.digitToInt() } }
            .toMap()
    }

    fun part1(): Int = routes.values.sumOf { it.toSet().size }
    fun part2(): Int = routes.values.sumOf { it.size }

    private fun solve(): Map<Vec, List<Vec>> {
        return map.filterValues { it == 0 }.keys.associateWith { walk(it) }
    }

    private fun walk(from: Vec): List<Vec> {
        val summitsReached = mutableListOf<Vec>()

        fun dfs(pos: Vec) {
            if (map.getValue(pos) == 9) {
                summitsReached.add(pos)
                return
            }
            sequenceOf(Vec(1, 0), Vec(0, 1), Vec(-1, 0), Vec(0, -1))
                .map { pos + it }
                .filter { map.getValue(it) == map.getValue(pos) + 1 }
                .forEach { dfs(it) }
        }

        dfs(from)

        return summitsReached
    }
}

package day10

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    val solution = Day10(File("input/10.txt").inputStream())
    println("Part 1: ${solution.part1()}")
    println("Part 2: ${solution.part2()}")
}

class Day10(stream: InputStream) {
    val map = parseInput(stream)
    val dim = computeDim(map)

    fun parseInput(stream: InputStream): Map<Vec, Int> {
        return stream
            .bufferedReader()
            .readLines()
            .flatMapIndexed { i, line -> line.mapIndexed {j, char -> Vec(i, j) to char.digitToInt() } }
            .toMap()
    }

    private fun computeDim(map: Map<Vec, Int>): Vec {
        return map.keys.maxWith { v1, v2 -> (v1 - v2).run { i + j } } + Vec(1, 1)
    }

    fun part1(): Int {
        val done = map.mapValues { false }.toMutableMap()
        val reachableSummits = map.mapValues { setOf<Vec>() }.toMutableMap()

        fun getReachableSummits(pos: Vec): Set<Vec> {
            if (!map.contains(pos))
                return setOf()
            if (done[pos]!!)
                return reachableSummits[pos]!!
            done[pos] = true
            reachableSummits[pos] = if (map[pos]!! == 9)
                setOf(pos)
            else
                 sequenceOf(Vec(1, 0), Vec(0, 1), Vec(-1, 0), Vec(0, -1))
                    .filter { map.getOrDefault(pos + it, 0) == map[pos]!! + 1 }
                    .flatMap { getReachableSummits(pos + it) }
                    .toSet()
            return reachableSummits[pos]!!
        }
        return map.filterValues { it == 0 }.keys.sumOf { getReachableSummits(it).size }
    }

    fun part2(): Int {
        return map.filterValues { it == 0 }.keys.sumOf { countRoutes(it) }
    }

    private fun countRoutes(from: Vec): Int {
        var reached = 0

        fun dfs(pos: Vec) {
            if (map[pos]!! == 9) {
                reached++
                return
            }
            sequenceOf(Vec(1, 0), Vec(0, 1), Vec(-1, 0), Vec(0, -1))
                .map { pos + it }
                .filter { map.getOrDefault(it, 0) == map[pos]!! + 1 }
                .forEach { dfs(it) }
        }

        dfs(from)
        return reached
    }
}

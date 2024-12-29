package day20

import common.*
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.abs

fun main() {
    println("--- Day 20: Race Condition ---")
    val day20 = Day20.fromStream(File("input/20.txt").inputStream())
    println("Part 1: ${day20.part1()}")
    println("Part 2: ${day20.part2()}")
}

class Day20(val map: Set<Vec>, val start: Vec, val end: Vec) {
    private val fromStart = distancesFrom(start)
    private val fromEnd = distancesFrom(end)
    val realDistance = fromStart[end] ?: error("End not reachable")

    companion object {
        fun fromStream(stream: InputStream): Day20 {
            val charMap = stream
                .bufferedReader()
                .lineSequence()
                .flatMapIndexed { i, line ->
                    line.mapIndexed { j, char -> Vec(i, j) to char }
                }.toMap()
            val start = charMap.filter { it.value == 'S' }.keys.first()
            val end = charMap.filter { it.value == 'E' }.keys.first()
            return Day20(charMap.filterValues { it != '#' }.keys.toSet(), start, end)
        }
    }

    private fun distancesFrom(start: Vec): Map<Vec, Int> {
        val q: Queue<Vec> = LinkedList()
        val distances = mutableMapOf(start to 0)
        q.add(start)
        while (q.isNotEmpty()) {
            val pos = q.remove()
            val dist = distances[pos] ?: error("$pos has no distance")
            sequenceOf(UP, DOWN, LEFT, RIGHT)
                .map { pos + it }
                .filter { map.contains(it) }
                .filterNot { distances.contains(it) }
                .forEach {
                    distances[it] = dist + 1
                    q.add(it)
                }
        }
        return distances
    }

    private fun cheatsFrom(pos: Vec, maxDist: Int) = iterator {
        (-maxDist..maxDist).forEach { i ->
            (-maxDist..maxDist)
                .map { j -> Pair(pos + Vec(i, j), abs(i) + abs(j)) }
                .filter { it.second <= maxDist }
                .filter { map.contains(it.first) }
                .forEach { yield(it) }
        }
    }

    fun cheatSavings(maxDist: Int): Map<Int, Int> {
        val counts = mutableMapOf<Int, Int>().withDefault { 0 }
        fromStart.entries.forEach { (pos, d1) ->
            cheatsFrom(pos, maxDist).forEach { (to, d2) ->
                fromEnd[to]?.let { d3 ->
                    val saved = realDistance - (d1 + d2 + d3)
                    if (saved > 0) {
                        counts[saved] = counts.getValue(saved) + 1
                    }
                }
            }
        }
        return counts
    }

    fun part1(): Int {
        return cheatSavings(2).filterKeys { it >= 100 }.values.sum()
    }

    fun part2(): Int {
        return cheatSavings(20).filterKeys { it >= 100 }.values.sum()
    }
}

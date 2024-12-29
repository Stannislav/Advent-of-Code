package day16

import common.*
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.streams.asSequence

fun main() {
    println("--- Day 16: Reindeer Maze ---")
    val maze = parseInput(File("input/16.txt").inputStream())
    println("Part 1: ${part1(maze)}")
    println("Part 2: ${part2(maze)}")
}

fun parseInput(stream: InputStream): Maze {
    val map = stream
        .bufferedReader()
        .lines()
        .asSequence()
        .flatMapIndexed { i, line -> line.mapIndexed { j, c -> Vec(i, j) to c } }
        .toMap()
        .toMutableMap()
    val start = map.filterValues { it == 'S' }.keys.first()
    val end = map.filterValues { it == 'E' }.keys.first()
    map[start] = '.'
    map[end] = '.'
    return Maze(map, start, end)
}

fun part1(maze: Maze): Int {
    return maze.score()
}

fun part2(maze: Maze): Int {
    return maze.traceAllPaths().size
}

class Maze(val map: Map<Vec, Char>, val start: Vec, val end: Vec) {
    private val scores = mutableMapOf(Pair(start, RIGHT) to 0).withDefault { Int.MAX_VALUE }

    init { traverse() }

    private fun traverse() {
        val q: Queue<Pair<Vec, Vec>> = LinkedList()
        q.add(Pair(start, RIGHT))

        while (q.isNotEmpty()) {
            val (pos, dir) = q.remove()
            val score = scores.getValue(Pair(pos, dir))
            if (pos == end)
                continue
            (0..3).forEach { nTurns ->
                val nextDir = dir.turn(nTurns)
                val nextScore = score + 1 + turnScore(nTurns)
                val nextPos = pos + nextDir
                if (map[nextPos] == '.' && scores.getValue(Pair(nextPos, nextDir)) > nextScore) {
                    q.add(Pair(nextPos, nextDir))
                    scores[Pair(nextPos, nextDir)] = nextScore
                }
            }
        }
    }

    fun score(): Int {
        return scores.filterKeys { it.first == end }.values.min()
    }

    private fun turnScore(nTurns: Int) = when(nTurns % 4) {
        0 -> 0
        1 -> 1000
        2 -> 2000
        3 -> 1000
        else -> error("Invalid nTurns % 4")
    }

    fun traceAllPaths(): Set<Vec> {
        val score = score()
        val seeds = scores.filter { it.key.first == end && it.value == score }.keys
        val q: Queue<Pair<Vec, Vec>> = LinkedList()
        val seen = mutableSetOf<Pair<Vec, Vec>>()
        q.addAll(seeds)
        seen.addAll(seeds)
        while (q.isNotEmpty()) {
            val (pos, dir) = q.remove()
            if (pos == start)
                continue
            (0..3).forEach { nTurns ->
                    val prevDir = dir.turn(nTurns)
                    val dScore = 1 + turnScore(nTurns)
                    val prevScore = scores[Pair(pos, dir)]?.let { it - dScore } ?: error("score not found for ${Pair(pos, dir)}")
                    val prevPos = pos - dir
                    if (scores[Pair(prevPos, prevDir)] == prevScore) {
                        q.add(Pair(prevPos, prevDir))
                        seen.add(Pair(prevPos, prevDir))
                    }
                }
        }
        return seen.map { it.first }.toSet()
    }
}

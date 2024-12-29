package day18

import common.*
import java.io.File
import java.io.InputStream
import java.util.*

fun main() {
    println("--- Day 18: RAM Run ---")
    val bytes = parseInput(File("input/18.txt").inputStream())
    println("Part 1: ${part1(bytes, Vec(70, 70), 1024)}")
    println("Part 2: ${part2(bytes, Vec(70, 70), 1024)}")
}

fun parseInput(stream: InputStream): List<Vec> {
    return stream
        .bufferedReader()
        .lineSequence()
        .map { it.split(",", limit = 2).map(String::toInt) }
        .map { (i, j) -> Vec(i, j) }
        .toList()
}

fun part1(allBytes: List<Vec>, target: Vec, max: Int): Int {
    return dijkstra(allBytes.subList(0, max), target)?.size ?: error("can't find $target")
}

fun part2(allBytes: List<Vec>, target: Vec, min: Int): String {
    // We know from path one that for min amount of bytes there is a path.
    val bytes = allBytes.subList(0, min).toMutableList()
    var path = dijkstra(bytes, target)?.toSet() ?: error("can't find minimal path")

    for (byte in allBytes.drop(min)) {
        bytes += byte
        // Only recompute the path if the next byte falls on the current path.
        if (path.contains(byte))
            path = dijkstra(bytes, target)?.toSet() ?: return byte.run { "${i},${j}" }
    }
    error("no solution found")
}

fun dijkstra(bytes: List<Vec>, target: Vec): List<Vec>? {
    val corrupted = bytes.toSet()
    val q = PriorityQueue<Pair<Vec, Int>>(compareBy { it.second }) // (pos, dist)
    q.add(Pair(Vec(0, 0), 0))
    val prev = mutableMapOf<Vec, Pair<Vec, Int>>() // pos -> (prevPos, prevDist)
    while (q.isNotEmpty()) {
        val (pos, dist) = q.remove()
        if (pos == target)
            break
        sequenceOf(LEFT, RIGHT, UP, DOWN)
            .map { pos + it }
            .filter { it.i >= 0 && it.j >= 0 && it.i <= target.i && it.j <= target.j }
            .filterNot { corrupted.contains(it) }
            .filter { prev[it]?.run { dist < second } ?: true }
            .forEach {
                prev[it] = Pair(pos, dist)
                q.add(Pair(it, dist + 1))
            }
    }

    // Reconstruct path
    val path = mutableListOf<Vec>()
    var pos = target
    while (pos != Vec(0, 0)) {
        path.add(pos)
        pos = prev[pos]?.first ?: return null
    }
    return path.reversed()
}

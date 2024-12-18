package day18

import common.*
import java.io.File
import java.io.InputStream
import java.util.*

fun main() {
    val bytes = parseInput(File("input/18.txt").inputStream())
    println("Part 1: ${part1(bytes, Vec(70, 70), 1024)}")
    println("Part 2: ${part2(bytes, Vec(70, 70), 1024)}")
}

fun parseInput(stream: InputStream): List<Vec> {
    return stream
        .bufferedReader()
        .readLines()
        .map { line ->
            val (i, j) = line.split(",", limit = 2).map { it.toInt() }
            Vec(i, j)
        }
        .toList()
}

fun part1(allBytes: List<Vec>, target: Vec, max: Int): Int {
    return getPath(allBytes.subList(0, max), target)?.size ?: error("Could not find $target")
}

fun part2(allBytes: List<Vec>, target: Vec, min: Int): String {
    val bytes = allBytes.subList(0, min).toMutableList()
    var path = getPath(bytes, target)?.toSet() ?: error("Can't find minimal path")
    for (byte in allBytes.drop(min)) {
        bytes += byte
        if (path.contains(byte))
            path = getPath(bytes, target)?.toSet() ?: return byte.run { "${i},${j}" }
    }
    error("No solution found")
}

fun getPath(bytes: List<Vec>, target: Vec): List<Vec>? {
    val corrupted = bytes.toSet()
    val dist = mutableMapOf(Vec(0, 0) to 0)
    val q: Queue<Vec> = LinkedList()
    val done = mutableSetOf<Vec>()
    q.add(Vec(0, 0))
    val prev = mutableMapOf<Vec, Vec>()
    while (q.isNotEmpty()) {
        val pos = q.remove()
        done.add(pos)
        val d = dist[pos]?.let { it + 1 } ?: error("distance missing for $pos")
        sequenceOf(LEFT, RIGHT, UP, DOWN)
            .map { pos + it }
            .filter { it.i >= 0 && it.j >= 0 && it.i <= target.i && it.j <= target.j }
            .filter { !corrupted.contains(it) && !done.contains(it) }
            .filter { dist[it]?.let { oldD -> oldD > d } ?: true }
            .forEach { dist[it] = d; prev[it] = pos; q.add(it) }
    }
    if (!dist.contains(target))
        return null
    val path = mutableListOf<Vec>()
    var pos = target
    while (pos != Vec(0, 0)) {
        path.add(pos)
        pos = prev[pos] ?: error("Path incomplete")
    }
    return path.reversed()
}

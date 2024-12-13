package day12

import common.*
import java.io.File
import java.io.InputStream
import java.util.LinkedList
import java.util.Queue
import kotlin.streams.asSequence

fun main() {
    val input = parseInput(File("input/12.txt").inputStream())
    println("Part 1: ${part1(input)}")
}

fun parseInput(stream: InputStream): Map<Vec, Char> {
    return stream
        .bufferedReader()
        .lines()
        .asSequence()
        .flatMapIndexed { i, line -> line.mapIndexed { j, c -> Vec(i, j) to c }}
        .toMap()
}

fun findRegionFor(pos: Vec, map: Map<Vec, Char>): Set<Vec> {
    val regionChar = map[pos] ?: error("$pos not in map")
    val region = mutableSetOf(pos)
    val q: Queue<Vec> = LinkedList<Vec>().apply { add(pos) }
    while (q.isNotEmpty()) {
        val v = q.remove()
        sequenceOf(UP, DOWN, LEFT, RIGHT)
            .map { v + it }
            .filter { !region.contains(it) && map.getOrDefault(it, '?') == regionChar }
            .forEach { q.add(it); region.add(it) }
    }
    return region
}

fun getRegions(map: Map<Vec, Char>): List<Set<Vec>> {
    val seen = mutableSetOf<Vec>()
    val regions = mutableListOf<Set<Vec>>()
    map.keys.forEach {
        if (!seen.contains(it)) {
            val region = findRegionFor(it, map)
            regions.add(region)
            seen.addAll(region)
        }
    }

    return regions
}

fun part1(input: Map<Vec, Char>): Int {
    return getRegions(input).sumOf { it.size * it.sumOf { pos -> perimeterScore(pos, input) }}
}

fun perimeterScore(pos: Vec, input: Map<Vec, Char>): Int {
    val map = input.withDefault { '?' }
    return sequenceOf(UP, DOWN, LEFT, RIGHT)
        .count { map.getValue(pos) != map.getValue(pos + it) }
}

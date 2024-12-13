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
    println("Part 2: ${part2(input)}")
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

fun part2(input: Map<Vec, Char>): Int {
    return getRegions(input).sumOf { it.size * getNumberOfSides(it) }
}

/**
 * Compute number of straight sides of an area.
 *
 * The number of sides of an area is equal to the number of corners. Example:
 *
 * ```text
 * +-+
 * |C|
 * | +-+
 * |C C|
 * +-+ |
 *   |C|
 *   +-+
 * ```
 *
 * The area above has 8 straight sides, and also 8 corners. This is always true
 * because straight sides and corners alternate.
 *
 * There are two types of corners - convex and concave corners.
 * Convex corners have two tiles from a different region on either side, concave corners have
 * tiles from the same region on either side, and a tile from a different region diagonally.
 *
 * Convex corner at top right of C:
 *
 * ```text
 *
 * d
 * -+
 * C|d
 * ```
 *
 * Concave corner at top right of C:
 *
 * ```text
 * c|D
 *  +-
 * C c
 * ```
 */
fun getNumberOfSides(region: Set<Vec>): Int {
    fun countCorners(pos: Vec): Int {
        val same = { v: Vec -> region.contains(v) }
        return sequenceOf(Pair(UP, LEFT), Pair(UP, RIGHT), Pair(DOWN, LEFT), Pair(DOWN, RIGHT))
            .count {
                val convex = !same(pos + it.first) && !same(pos + it.second)
                val concave = same(pos + it.first) && same(pos + it.second) && !same(pos + it.first + it.second)
                convex || concave
            }
    }
    return region.sumOf { countCorners(it) }
}

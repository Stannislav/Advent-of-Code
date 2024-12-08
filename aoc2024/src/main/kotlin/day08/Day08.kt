package day08

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    val (antennas, dim) = parseInput(File("input/08.txt").inputStream())
    println("Part 1: ${part1(antennas, dim)}")
    println("Part 2: ${part2(antennas, dim)}")
}

fun parseInput(stream: InputStream): Pair<Map<Char, List<Vec>>, Vec> {
    val lines = stream.bufferedReader().readLines()
    val dim = Vec(lines.size, lines[0].length)
    val antennas = mutableMapOf<Char, MutableList<Vec>>().withDefault { _ -> mutableListOf() }
    for (i in 0 until dim.i) {
        for (j in 0 until dim.j) {
            if (lines[i][j] != '.') {
                if (!antennas.contains(lines[i][j])) {
                    antennas[lines[i][j]] = mutableListOf()
                }
                antennas[lines[i][j]]!!.add(Vec(i, j))
            }
        }
    }
    return Pair(antennas, dim)
}

fun part1(antennas: Map<Char, List<Vec>>, dim: Vec): Int {
    return antennas.values
        .flatMap { pairs(it).asSequence() }
        .flatMap { (v1, v2) -> listOf(v1 + (v1 - v2), v2 + (v2 - v1))}
        .toSet()
        .count { it.i < dim.i && it.j < dim.j && it.i >= 0 && it.j >= 0 }
}

fun part2(antennas: Map<Char, List<Vec>>, dim: Vec): Int {
    val antinodes = mutableSetOf<Vec>()
    for ((_, pts) in antennas) {
        for ((v1, v2) in pairs(pts)) {
            makeAntinodes(v1, v2 - v1, dim).forEach { antinodes.add(it) }
            makeAntinodes(v2, v1 - v2, dim).forEach { antinodes.add(it) }
        }
    }
    return antinodes.count()
}

fun pairs(vecs: List<Vec>) = iterator {
    for (i in vecs.indices) {
        for (j in i + 1 until vecs.size) {
            yield(Pair(vecs[i], vecs[j]))
        }
    }
}

fun makeAntinodes(from: Vec, step: Vec, dim: Vec) = iterator {
    var next = from + step
    while(next.i >=0 && next.j >= 0 && next.i < dim.i && next.j < dim.j) {
        yield(next)
        next += step
    }
}

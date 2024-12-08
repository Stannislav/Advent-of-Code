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
    return generateAntinodes(antennas, dim, ::simpleAntinodes)
}

fun part2(antennas: Map<Char, List<Vec>>, dim: Vec): Int {
    return generateAntinodes(antennas, dim, ::allAntinodes)
}

fun generateAntinodes(antennas: Map<Char, List<Vec>>, dim: Vec, generator: (Vec, Vec, Vec) -> Sequence<Vec>): Int {
    return antennas.values
        .flatMap { pairs(it) }
        .flatMap { (v1, v2) -> generator(v1, v2, dim) }
        .toSet()
        .count()
}

fun pairs(vecs: List<Vec>) = sequence {
    for (i in vecs.indices) {
        for (j in i + 1 until vecs.size) {
            yield(Pair(vecs[i], vecs[j]))
        }
    }
}

fun inBounds(vec: Vec, dim: Vec): Boolean = vec.i >=0 && vec.j >= 0 && vec.i < dim.i && vec.j < dim.j

fun simpleAntinodes(v1: Vec, v2: Vec, dim: Vec): Sequence<Vec> {
    return sequenceOf(v1 + (v1 - v2), v2 + (v2 - v1)).filter { inBounds(it, dim) }
}

fun allAntinodes(v1: Vec, v2: Vec, dim: Vec): Sequence<Vec> {
    fun generate(from: Vec, step: Vec) = sequence {
        var next = from + step
        while(inBounds(next, dim)) {
            yield(next)
            next += step
        }
    }
    return generate(v1, v2 - v1) + generate(v2, v1 - v2)
}

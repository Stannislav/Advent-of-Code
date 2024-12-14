package day14

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    val robots = parseInput(File("input/14.txt").inputStream())
    val lim = Vec(101, 103)
    println("Part 1: ${part1(robots, lim)}")
    val easterEggTime = part2(robots, lim)
    println("Part 2: $easterEggTime")
    draw(robots.map { it.move(easterEggTime, lim)}, Vec(35, 30), Vec(66, 64))
}

fun parseInput(stream: InputStream): List<Robot> {
    return stream.bufferedReader().lineSequence().map(Robot::fromString).toList()
}

fun part1(robots: List<Robot>, lim: Vec): Int {
    require(lim.i % 2 == 1 && lim.j % 2 == 1)
    return robots
        .map { it.move(100, lim)}
        .filter { it.pos.i != lim.i / 2 && it.pos.j != lim.j / 2 }
        .groupBy { Pair(it.pos.i < lim.i / 2, it.pos.j < lim.j / 2) }
        .values
        .map { it.count() }
        .fold(1) { acc, value -> acc * value }
}

fun part2(robots: List<Robot>, lim: Vec): Int {
    return generateSequence(0, Int::inc).first { time ->
        robots.map { it.move(time, lim) }.map { it.pos }.toSet().size == robots.size
    }
}

fun draw(robots: List<Robot>, from: Vec, to: Vec) {
    val positions = robots.map { it.pos }.toSet()

    for (j in from.j until to.j) {
        val line = (from.i until to.i).joinToString("") { if (positions.contains(Vec(it, j))) "##" else "  " }
        println(line)
    }
}

data class Robot(val pos: Vec, val vel: Vec) {
    companion object {
        fun fromString(line: String): Robot {
            return "-?\\d+".toRegex()
                .findAll(line)
                .map { it.value.toInt() }
                .toList()
                .let { Robot(Vec(it[0], it[1]), Vec(it[2], it[3])) }
        }
    }

    fun move(steps: Int, lim: Vec): Robot {
        return copy(pos = (pos + vel * steps).mod(lim))
    }
}

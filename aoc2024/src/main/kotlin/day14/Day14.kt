package day14

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 14: Restroom Redoubt ---")
    val robots = parseInput(File("input/14.txt").inputStream())
    val room = Room(robots, Vec(101, 103))
    println("Part 1: ${part1(room)}")
    val easterEggTime = room.findEasterEggTime()
    println("Part 2: $easterEggTime")
    room.evolve(easterEggTime).draw(Vec(35, 30), Vec(66, 64))
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

    fun move(steps: Int, lim: Vec): Robot = copy(pos = (pos + vel * steps).mod(lim))
}

data class Room(private val robots: List<Robot>, private val lim: Vec) {
    init { require(lim.i % 2 == 1 && lim.j % 2 == 1) }

    fun evolve(time: Int): Room = copy(robots = robots.map { it.move(time, lim)})

    fun countByQuadrant(): List<Int> {
        return robots
            .filter { it.pos.i != lim.i / 2 && it.pos.j != lim.j / 2 }
            .groupingBy { Pair(it.pos.i < lim.i / 2, it.pos.j < lim.j / 2) }
            .eachCount()
            .values
            .toList()
    }

    /**
     * Find the first time when all robots are at distinct positions.
     */
    fun findEasterEggTime(): Int {
        return generateSequence(0, Int::inc).first { time ->
            robots.map { it.move(time, lim) }.map { it.pos }.toSet().size == robots.size
        }
    }

    fun draw(from: Vec = Vec(0, 0), to: Vec = lim) {
        val positions = robots.map { it.pos }.toSet()
        val pixel = { pos: Vec -> if (positions.contains(pos)) "##" else "  " }
        for (j in from.j until to.j) {
            val line = (from.i until to.i).joinToString("") { pixel(Vec(it, j)) }
            println(line)
        }
    }
}

fun parseInput(stream: InputStream): List<Robot> {
    return stream.bufferedReader().lineSequence().map(Robot::fromString).toList()
}

fun part1(room: Room): Int {
    return room.evolve(100).countByQuadrant().fold(1) { acc, value -> acc * value }
}

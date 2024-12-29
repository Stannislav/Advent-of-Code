package day15

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    println("--- Day 15: Warehouse Woes ---")
    val file = File("input/15.txt")
    println("Part 1: ${part1(parseInput(file.inputStream()))}")
    println("Part 2: ${part2(parseInput(file.inputStream(), double = true))}")
}

data class Warehouse(val map: MutableMap<Vec, Char>, val cmd: List<Vec>) {
    fun run(): Warehouse {
        var pos = map.filterValues { it == '@' }.keys.first()
        cmd.forEach {
            if (canMove(pos, it))
                pos = move(pos, it)
        }
        return this
    }

    private fun canMove(pos: Vec, dir: Vec): Boolean {
        val newPos = pos + dir
        return map[newPos].let {
            when(it) {
                '#' -> false
                'O' -> canMove(newPos, dir)
                '[' -> when (dir) {
                    RIGHT -> canMove(newPos + RIGHT, dir)
                    LEFT -> canMove(newPos, dir)
                    else -> canMove(newPos, dir) && canMove(newPos + RIGHT, dir)
                }
                ']' -> when (dir) {
                    RIGHT -> canMove(newPos, dir)
                    LEFT -> canMove(newPos + LEFT, dir)
                    else -> canMove(newPos, dir) && canMove(newPos + LEFT, dir)
                }
                '.' -> true
                else -> error("Unknown tile: $it")
            }
        }
    }

    private fun move(pos: Vec, dir: Vec): Vec {
        val newPos = pos + dir
        when (map[newPos]) {
            'O' -> move(newPos, dir)
            '[' -> if (dir == RIGHT) {
                move(newPos + RIGHT, dir)
                move(newPos, dir)
            } else {
                move(newPos, dir)
                move(newPos + RIGHT, dir)
            }
            ']' -> if (dir == LEFT) {
                move(newPos + LEFT, dir)
                move(newPos, dir)
            } else {
                move(newPos, dir)
                move(newPos + LEFT, dir)
            }
        }
        map[newPos] = map[pos]!!
        map[pos] = '.'
        return newPos
    }

    fun scoreBy(char: Char): Int {
        return map.filterValues { it == char }.keys.sumOf { 100 * it.i + it.j }
    }
}

fun parseInput(stream: InputStream, double: Boolean = false): Warehouse {
    val (first, second) = stream.bufferedReader().readText().split("\n\n", limit = 2)
    val map = first
        .lines()
        .map { if (double) double(it) else it }
        .flatMapIndexed { i, line -> line.mapIndexed { j, c -> Vec(i, j) to c } }
        .toMap()
    val cmd = second.replace("\n", "").map {
        when(it) {'^' -> UP; 'v' -> DOWN; '<' -> LEFT; '>' -> RIGHT; else -> error("Unknown command: $it") }
    }

    return Warehouse(map.toMutableMap(), cmd)
}

fun double(line: String): String {
    return line.map {
        when (it) {
            'O' -> "[]"
            '@' -> "@."
            else -> "$it$it"
        }
    }.joinToString("")
}

fun part1(warehouse: Warehouse): Int {
    return warehouse.run().scoreBy('O')
}

fun part2(warehouse: Warehouse): Int {
    return warehouse.run().scoreBy('[')
}

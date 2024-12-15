package day15

import common.*
import java.io.File
import java.io.InputStream

fun main() {
    val file = File("input/15.txt")
    println("Part 1: ${part1(parseInput(file.inputStream()))}")
    println("Part 2: ${part2(parseInput(file.inputStream(), double = true))}")
}

data class Warehouse(val map: Map<Vec, Char>, val cmd: List<Vec>)

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

    return Warehouse(map, cmd)
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
    val map = warehouse.map.toMutableMap()
    var pos = map.filterValues { it == '@' }.keys.first()
    warehouse.cmd.forEach {
        if (canMove(pos, it, map))
            pos = move(pos, it, map)
    }
    return scoreBy('O', map)
}

fun part2(warehouse: Warehouse): Int {
    val map = warehouse.map.toMutableMap()
    var pos = map.filterValues { it == '@' }.keys.first()
    warehouse.cmd.forEach {
//        show(map)
//        println("Next command: ${when(it) {LEFT -> '<'; RIGHT -> '>'; UP -> '^'; DOWN -> 'v'; else -> error("?")}}")
        if (canMove(pos, it, map))
            pos = move(pos, it, map)
    }
//    show(map)
    return scoreBy('[', map)
}

fun canMove(pos: Vec, dir: Vec, map: Map<Vec, Char>): Boolean {
    val newPos = pos + dir
    return map[newPos].let {
        when(it) {
            '#' -> false
            'O' -> canMove(newPos, dir, map)
            '[' -> when (dir) {
                RIGHT -> canMove(newPos + RIGHT, dir, map)
                LEFT -> canMove(newPos, dir, map)
                else -> canMove(newPos, dir, map) && canMove(newPos + RIGHT, dir, map)
            }
            ']' -> when (dir) {
                RIGHT -> canMove(newPos, dir, map)
                LEFT -> canMove(newPos + LEFT, dir, map)
                else -> canMove(newPos, dir, map) && canMove(newPos + LEFT, dir, map)
            }
            '.' -> true
            else -> error("Unknown tile: $it")
        }
    }
}

fun move(pos: Vec, dir: Vec, map: MutableMap<Vec, Char>): Vec {
    val newPos = pos + dir
    when (map[newPos]) {
        'O' -> move(newPos, dir, map)
        '[' -> if (dir == RIGHT) {
            move(newPos + RIGHT, dir, map)
            move(newPos, dir, map)
        } else {
            move(newPos, dir, map)
            move(newPos + RIGHT, dir, map)
        }
        ']' -> if (dir == LEFT) {
            move(newPos + LEFT, dir, map)
            move(newPos, dir, map)
        } else {
            move(newPos, dir, map)
            move(newPos + LEFT, dir, map)
        }
    }
    map[newPos] = map[pos]!!
    map[pos] = '.'
    return newPos
}


fun show(map: Map<Vec, Char>) {
    val lim = map.keys.maxBy { it.i + it.j } + Vec(1, 1)
    for (i in 0 until lim.i) {
        println((0 until lim.j).map { map[Vec(i, it)] }.joinToString(""))
    }
}

fun scoreBy(char: Char, map: Map<Vec, Char>): Int {
    return map.filterValues { it == char }.keys.sumOf { 100 * it.i + it.j }
}

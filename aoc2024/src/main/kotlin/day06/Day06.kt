package day06

import java.io.File
import java.io.InputStream

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second

fun main() {
    val (grid, start, dir) = parseInput(File("input/06.txt").inputStream())
    println("Part 1: ${part1(grid, start, dir)}")
    println("Part 2: ${part2(grid, start, dir)}")
}

fun parseInput(stream: InputStream): Triple<Array<BooleanArray>, Vec, Vec> {
    var grid = arrayOf<BooleanArray>()
    var start = Vec(0, 0)
    var dir = Vec(0, 0)

    var i = 0
    stream.bufferedReader().forEachLine {
        grid += it.map { c -> c != '#'}.toTypedArray().toBooleanArray()
        it.forEachIndexed { j, c -> if (c != '#' && c != '.') {
                start = Vec(i, j)
                dir = when(c) {
                    'v' -> Vec(1, 0)
                    '>' -> Vec(0, 1)
                    '^' -> Vec(-1, 0)
                    '<' -> Vec(0, -1)
                    else -> throw IllegalStateException("Unknown character: $c")
                }
            }
        }
        i += 1
    }

    return Triple(grid, start, dir)
}

fun part1(grid: Array<BooleanArray>, start: Vec, startDir: Vec): Int {
    val seen = mutableSetOf<Vec>()
    var pos = start
    var dir = startDir
    while (inBounds(pos, grid)) {
        seen.add(pos)
        while (blocked(step(pos, dir), grid))
            dir = turn(dir)
        pos = step(pos, dir)
    }
    return seen.size
}

fun part2(grid: Array<BooleanArray>, start: Vec, startDir: Vec): Int {
    var loopCount = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] && Vec(i, j) != start) {
                grid[i][j] = false
                if (isLoop(grid, start, startDir))
                    loopCount++
                grid[i][j] = true
            }
        }
    }
    return loopCount
}

fun isLoop(grid: Array<BooleanArray>, start: Vec, startDir: Vec): Boolean {
    val seen = mutableSetOf<Vec>()
    val seenWithDir = mutableSetOf<Pair<Vec, Vec>>()
    var pos = start
    var dir = startDir
    while (inBounds(pos, grid)) {
        seen.add(pos)
        seenWithDir.add(Pair(pos, dir))
        while (blocked(step(pos, dir), grid))
            dir = turn(dir)
        pos = step(pos, dir)
        if (seenWithDir.contains(Pair(pos, dir)))
            return true
    }
    return false
}

fun inBounds(pos: Vec, grid: Array<BooleanArray>): Boolean {
    return pos.i >= 0 && pos.j >= 0 && pos.i < grid.size && pos.j < grid[0].size
}

fun blocked(pos: Vec, grid: Array<BooleanArray>): Boolean {
    return inBounds(pos, grid) && !grid[pos.i][pos.j]
}

fun step(pos: Vec, dir: Vec): Vec {
    return Vec(pos.i + dir.i, pos.j + dir.j)
}

fun turn(dir: Vec): Vec {
    return when(dir) {
        Vec(1, 0) -> Vec(0, -1)
        Vec(0, -1) -> Vec(-1, 0)
        Vec(-1, 0) -> Vec(0, 1)
        Vec(0, 1) -> Vec(1, 0)
        else -> throw IllegalArgumentException("Invalid direction: $dir")
    }
}

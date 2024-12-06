package day06

import java.io.File
import java.io.InputStream

fun main() {
    val (grid, start, dir) = parseInput(File("input/06.txt").inputStream())
    print("Part 1: ${part1(grid, start, dir)}")
}

fun parseInput(stream: InputStream): Triple<Array<BooleanArray>, Pair<Int, Int>, Pair<Int, Int>> {
    var grid = arrayOf<BooleanArray>()
    var start = Pair(0, 0)
    var dir = Pair(0, 0)

    var i = 0
    stream.bufferedReader().forEachLine {
        grid += it.map { c -> c != '#'}.toTypedArray().toBooleanArray()
        it.forEachIndexed { j, c -> if (c != '#' && c != '.') {
                start = Pair(i, j)
                dir = when(c) {
                    'v' -> Pair(1, 0)
                    '>' -> Pair(0, 1)
                    '^' -> Pair(-1, 0)
                    '<' -> Pair(0, -1)
                    else -> throw IllegalStateException("Unknown character: $c")
                }
            }
        }
        i += 1
    }

    return Triple(grid, start, dir)
}

fun part1(grid: Array<BooleanArray>, start: Pair<Int, Int>, startDir: Pair<Int, Int>): Int {
    val seen = mutableSetOf<Pair<Int, Int>>()
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

fun inBounds(pos: Pair<Int, Int>, grid: Array<BooleanArray>): Boolean {
    return pos.first >= 0 && pos.second >= 0 && pos.first < grid.size && pos.second < grid[0].size
}

fun blocked(pos: Pair<Int, Int>, grid: Array<BooleanArray>): Boolean {
    return inBounds(pos, grid) && !grid[pos.first][pos.second]
}

fun step(pos: Pair<Int, Int>, dir: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(pos.first + dir.first, pos.second + dir.second)
}

fun turn(dir: Pair<Int, Int>): Pair<Int, Int> {
    return when(dir) {
        Pair(1, 0) -> Pair(0, -1)
        Pair(0, -1) -> Pair(-1, 0)
        Pair(-1, 0) -> Pair(0, 1)
        Pair(0, 1) -> Pair(1, 0)
        else -> throw IllegalArgumentException("Invalid direction: $dir")
    }
}

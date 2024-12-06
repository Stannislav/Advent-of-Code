package day06

import java.io.File
import java.io.InputStream

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second

fun main() {
    val (grid, start, dir) = parseInput(File("input/06.txt").inputStream())
    val seen = part1(grid, start, dir)
    println("Part 1: ${seen.size}")
    // Only points reachable from the given start point, all of which we found in part 1,
    // should be considered as potential obstacle points for part 2.
    println("Part 2: ${part2(grid, start, dir, seen)}")
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

fun part1(grid: Array<BooleanArray>, start: Vec, startDir: Vec): Set<Vec> {
    val seen = mutableSetOf<Vec>()
    var pos = start
    var dir = startDir
    while (inBounds(pos, grid)) {
        seen.add(pos)
        while (blocked(step(pos, dir), grid))
            dir = turn(dir)
        pos = step(pos, dir)
    }
    return seen
}

fun part2(grid: Array<BooleanArray>, start: Vec, startDir: Vec, candidates: Collection<Vec>): Int {
    var loopCount = 0
    // Cache contains straight routes to the next obstacle. Using this optimization
    // we can jump directly to the next obstacle instead of doing single steps.
    // We only cache routes without the new obstacles added in this part.
    val cache = mutableMapOf<Pair<Vec, Vec>, Vec>()
    for ((i, j) in candidates) {
        if (Vec(i, j) != start) {
            grid[i][j] = false
            if (isLoop(grid, start, startDir, i, j, cache))
                loopCount++
            grid[i][j] = true
        }
    }
    return loopCount
}

fun isLoop(grid: Array<BooleanArray>, start: Vec, startDir: Vec, i: Int, j: Int, cache: MutableMap<Pair<Vec, Vec>, Vec>): Boolean {
    val seen = mutableSetOf<Pair<Vec, Vec>>()
    var pos = start
    var dir = startDir
    val cacheStarts = mutableListOf<Vec>()
    while (inBounds(pos, grid)) {
        if (seen.contains(Pair(pos, dir)))
            return true
        seen.add(Pair(pos, dir))
        if (blocked(step(pos, dir), grid)) {
            cacheStarts.forEach { cache[Pair(it, dir)] = pos }
            cacheStarts.clear()
            while (blocked(step(pos, dir), grid))
                dir = turn(dir)
        } else {
            // Cache is for the grid without the new obstacles. So, if we're on a trajectory that
            // would hit the new obstacle, then we can't use the cache.
            val skipCache = (pos.i == i && (j - pos.j) * dir.j > 0) || (pos.j == j && (i - pos.i) * dir.i > 0)
            if (!skipCache && cache.contains(Pair(pos, dir))) {
                pos = cache[Pair(pos, dir)]!!
            } else {
                cacheStarts.add(pos)
                pos = step(pos, dir)
            }
        }
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

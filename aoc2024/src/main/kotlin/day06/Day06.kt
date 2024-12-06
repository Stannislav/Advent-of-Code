package day06

import java.io.File
import java.io.InputStream

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second
operator fun Vec.plus(other: Vec) = Vec(this.i + other.i, this.j + other.j)
operator fun Vec.minus(other: Vec) = Vec(this.i - other.i, this.j - other.j)

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
        i++
    }

    return Triple(grid, start, dir)
}

fun part1(grid: Array<BooleanArray>, start: Vec, startDir: Vec): Set<Vec> {
    val seen = mutableSetOf<Vec>()
    var pos = start
    var dir = startDir
    while (inBounds(pos, grid)) {
        seen.add(pos)
        while (blocked(pos + dir, grid))
            dir = turn(dir)
        pos += dir
    }
    return seen
}

fun part2(grid: Array<BooleanArray>, start: Vec, startDir: Vec, candidates: Set<Vec>): Int {
    var loopCount = 0
    // Cache contains straight routes to the next obstacle. Using this optimization
    // we can jump directly to the next obstacle instead of doing single steps.
    // We only cache routes without the new obstacles added in this part.
    val jumpMap = computeJumpMap(grid)
    for ((i, j) in candidates - start) {
        grid[i][j] = false
        if (isLoop(grid, start, startDir, i, j, jumpMap))
            loopCount++
        grid[i][j] = true
    }
    return loopCount
}

fun computeJumpMap(grid: Array<BooleanArray>): Map<Pair<Vec, Vec>, Vec> {
    // Idea thanks to Reddit: https://www.reddit.com/r/adventofcode/comments/1h7tovg/comment/m0pj7m5
    val jumpMap = mutableMapOf<Pair<Vec, Vec>, Vec>()
    val iMax = grid.size - 1
    val jMax = grid[0].size - 1

    fun addJumps(target: Vec, dir: Vec) {
        var pos = target - dir
        while (inBounds(pos, grid) && grid[pos.i][pos.j]) {
            jumpMap[Pair(pos, dir)] = target
            pos -= dir
        }
    }

    //
    for (i in grid.indices) {
        if (grid[i][0])
            addJumps(Vec(i, -1), Vec(0, -1))
        if (grid[i][jMax])
            addJumps(Vec(i, jMax + 1), Vec(0, 1))
    }
    for (j in grid[0].indices) {
        if (grid[0][j])
            addJumps(Vec(-1, j), Vec(-1, 0))
        if (grid[iMax][j])
            addJumps(Vec(iMax + 1, j), Vec(1, 0))
    }
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (!grid[i][j]) {
                for (dir in listOf(Vec(1, 0), Vec(0, 1), Vec(-1, 0), Vec(0, -1))) {
                    val target = Vec(i, j) - dir
                    if (inBounds(target, grid) && grid[target.i][target.j])
                        addJumps(target, dir)
                }
            }
        }
    }

    return jumpMap
}

fun isLoop(grid: Array<BooleanArray>, start: Vec, startDir: Vec, i: Int, j: Int, jumpMap: Map<Pair<Vec, Vec>, Vec>): Boolean {
    val seen = mutableSetOf<Pair<Vec, Vec>>()
    var pos = start
    var dir = startDir
    while (inBounds(pos, grid)) {
        if (seen.contains(Pair(pos, dir)))
            return true
        seen.add(Pair(pos, dir))
        if (blocked(pos + dir, grid)) {
            while (blocked(pos + dir, grid))
                dir = turn(dir)
        } else {
            // JumpMap is for the grid without the new obstacles. So, if we're on a trajectory that
            // would hit the new obstacle, then we can't use the jumpMap.
            val skipJump = (pos.i == i && (j - pos.j) * dir.j > 0) || (pos.j == j && (i - pos.i) * dir.i > 0)
            if (!skipJump && jumpMap.contains(Pair(pos, dir))) {
                pos = jumpMap[Pair(pos, dir)]!!
            } else {
                pos += dir
            }
        }
    }
    return false
}

fun inBounds(pos: Vec, grid: Array<BooleanArray>): Boolean {
    return pos.i >= 0 && pos.j >= 0 && pos.i < grid.size && pos.j < grid[0].size
}

fun blocked(pos: Vec, grid: Array<BooleanArray>): Boolean = inBounds(pos, grid) && !grid[pos.i][pos.j]

fun turn(dir: Vec): Vec = when(dir) {
    Vec(1, 0) -> Vec(0, -1)
    Vec(0, -1) -> Vec(-1, 0)
    Vec(-1, 0) -> Vec(0, 1)
    Vec(0, 1) -> Vec(1, 0)
    else -> throw IllegalArgumentException("Invalid direction: $dir")
}

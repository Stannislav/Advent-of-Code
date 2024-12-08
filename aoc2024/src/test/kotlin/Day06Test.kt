import common.Vec
import day06.*
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class Day06Test {
    @Test
    fun testParseInput() {
        val (grid, start, dir) = parseInput(streamInput("06.txt"))
        val expectedGrid = arrayOf(
            booleanArrayOf(true, true, true, true, false, true, true, true, true, true),
            booleanArrayOf(true, true, true, true, true, true, true, true, true, false),
            booleanArrayOf(true, true, true, true, true, true, true, true, true, true),
            booleanArrayOf(true, true, false, true, true, true, true, true, true, true),
            booleanArrayOf(true, true, true, true, true, true, true, false, true, true),
            booleanArrayOf(true, true, true, true, true, true, true, true, true, true),
            booleanArrayOf(true, false, true, true, true, true, true, true, true, true),
            booleanArrayOf(true, true, true, true, true, true, true, true, false, true),
            booleanArrayOf(false, true, true, true, true, true, true, true, true, true),
            booleanArrayOf(true, true, true, true, true, true, false, true, true, true),
        )
        expectedGrid.zip(grid).forEach { assertContentEquals(it.first, it.second) }
        assertEquals(Pair(6, 4), start)
        assertEquals(Pair(-1, 0), dir)
    }

    @Test
    fun testPart1() {
        val (grid, start, dir) = parseInput(streamInput("06.txt"))
        assertEquals(41, part1(grid, start, dir).size)
    }

    @Test
    fun testPart2() {
        val (grid, start, dir) = parseInput(streamInput("06.txt"))
        val candidates = part1(grid, start, dir)
        assertEquals(6, part2(grid, start, dir, candidates))
    }

    @Test
    fun funTestLoop() {
        val (grid, start, dir) = parseInput(streamInput("06.txt"))
        val cache = mutableMapOf<Pair<Vec, Vec>, Vec>()
        val i = 9
        val j = 7
        grid[i][j] = false
        assertTrue(isLoop(grid, start, dir, i, j, cache))
        assertTrue(isLoop(grid, start, dir, i, j, cache))
    }
}

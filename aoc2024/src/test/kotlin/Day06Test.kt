import day06.parseInput
import day06.part1
import day06.part2
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertContentEquals

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
        assertEquals(41, part1(grid, start, dir))
    }

    @Test
    fun testPart2() {
        val (grid, start, dir) = parseInput(streamInput("06.txt"))
        assertEquals(6, part2(grid, start, dir))
    }
}

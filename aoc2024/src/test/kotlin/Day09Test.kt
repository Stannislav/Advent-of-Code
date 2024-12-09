import day09.parseInput
import day09.part1
import day09.part2
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Day09Test {
    @Test
    fun testParseInput() {
        val input = parseInput(streamInput("09.txt"))
        val expected = arrayOf(
            0, 0, -1, -1, -1, 1, 1, 1, -1, -1, -1, 2, -1, -1,
            -1, 3, 3, 3, -1, 4, 4, -1, 5, 5, 5, 5, -1, 6, 6,
            6, 6, -1, 7, 7, 7, -1, 8, 8, 8, 8, 9, 9,
        )
        assertContentEquals(expected, input)
    }

    @Test
    fun testPart1() {
        val input = parseInput(streamInput("09.txt"))
        assertEquals(1928, part1(input))
    }

    @Test
    fun testPart2() {
        val input = parseInput(streamInput("09.txt"))
        assertEquals(2858, part2(input))
    }

    @Test
    fun testDebug() {
        part2(arrayOf(0, -1, -1, -1, -1, 1, 1, 2, 2, 3, 3))
    }
}

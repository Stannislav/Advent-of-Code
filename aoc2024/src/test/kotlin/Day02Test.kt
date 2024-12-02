import day02.parseInput
import day02.part1
import day02.part2
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun testParseInput() {
        val input = parseInput(streamInput("02.txt"))
        val expected = listOf(
            listOf(7, 6, 4, 2, 1),
            listOf(1, 2, 7, 8, 9),
            listOf(9, 7, 6, 2, 1),
            listOf(1, 3, 2, 4, 5),
            listOf(8, 6, 4, 4, 1),
            listOf(1, 3, 6, 7, 9),
        )
        assertEquals(expected, input)
    }

    @Test
    fun testPart1() {
        val input = parseInput(streamInput("02.txt"))
        assertEquals(2, part1(input))
    }

    @Test
    fun testPart2() {
        val input = parseInput(streamInput("02.txt"))
        assertEquals(4, part2(input))
    }
}

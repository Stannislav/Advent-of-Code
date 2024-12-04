import day04.part1
import day04.part2
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Day04Test {
    @Test
    fun testParseInput() {
        val input = day04.parseInput(streamInput("04.txt"))
        val expected = arrayOf(
            charArrayOf('M', 'M', 'M', 'S', 'X', 'X', 'M', 'A', 'S', 'M'),
            charArrayOf('M', 'S', 'A', 'M', 'X', 'M', 'S', 'M', 'S', 'A'),
            charArrayOf('A', 'M', 'X', 'S', 'X', 'M', 'A', 'A', 'M', 'M'),
            charArrayOf('M', 'S', 'A', 'M', 'A', 'S', 'M', 'S', 'M', 'X'),
            charArrayOf('X', 'M', 'A', 'S', 'A', 'M', 'X', 'A', 'M', 'M'),
            charArrayOf('X', 'X', 'A', 'M', 'M', 'X', 'X', 'A', 'M', 'A'),
            charArrayOf('S', 'M', 'S', 'M', 'S', 'A', 'S', 'X', 'S', 'S'),
            charArrayOf('S', 'A', 'X', 'A', 'M', 'A', 'S', 'A', 'A', 'A'),
            charArrayOf('M', 'A', 'M', 'M', 'M', 'X', 'M', 'M', 'M', 'M'),
            charArrayOf('M', 'X', 'M', 'X', 'A', 'X', 'M', 'A', 'S', 'X'),
        )
        for ((expectedLine, inputLine) in expected.zip(input)) {
            assertContentEquals(expectedLine, inputLine)
        }
    }

    @Test
    fun testPart1() {
        val input = day04.parseInput(streamInput("04.txt"))
        assertEquals(18, part1(input))
    }

    @Test
    fun testPart2() {
        val input = day04.parseInput(streamInput("04.txt"))
        assertEquals(9, part2(input))
    }
}

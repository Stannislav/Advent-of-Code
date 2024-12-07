import day07.parseInput
import day07.part1
import day07.part2
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    @Test
    fun testParseInput() {
        val input = parseInput(streamInput("07.txt"))
        val expected = mapOf<Long, List<Long>>(
            190L to listOf(10, 19),
            3267L to listOf(81, 40, 27),
            83L to listOf(17, 5),
            156L to listOf(15, 6),
            7290L to listOf(6, 8, 6, 15),
            161011L to listOf(16, 10, 13),
            192L to listOf(17, 8, 14),
            21037L to listOf(9, 7, 18, 13),
            292L to listOf(11, 6, 16, 20),
        )
        assertEquals(expected, input)
    }

    @Test
    fun testPart1() {
        val input = parseInput(streamInput("07.txt"))
        assertEquals(3749, part1(input))
    }

    @Test
    fun testPart2() {
        val input = parseInput(streamInput("07.txt"))
        assertEquals(11387, part2(input))
    }
}

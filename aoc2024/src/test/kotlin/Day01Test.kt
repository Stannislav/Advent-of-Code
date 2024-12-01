import day01.parseInput
import day01.part1
import day01.part2
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class Day01Test {
    @Test
    fun testParseInput() {
        val (left, right) = parseInput(streamInput("01.txt"))
        assertEquals(left, listOf(3, 4, 2, 1, 3, 3))
        assertEquals(right, listOf(4, 3, 5, 3, 9, 3))
    }

    @Test
    fun testPart1() {
        val (left, right) = parseInput(streamInput("01.txt"))
        assertEquals(part1(left, right), 11)
    }

    @Test
    fun testPart2() {
        val (left, right) = parseInput(streamInput("01.txt"))
        assertEquals(part2(left, right), 31)
    }
}

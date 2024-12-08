import day08.parseInput
import common.*
import day08.part1
import day08.part2
import kotlin.test.assertEquals
import kotlin.test.Test

class Day08Test {
    @Test
    fun testParseInput() {
        val (antennas, dim) = parseInput(streamInput("08.txt"))
        val expectedAntennas = mapOf(
            '0' to listOf(Vec(1, 8), Vec(2, 5), Vec(3, 7), Vec(4, 4)),
            'A' to listOf(Vec(5, 6), Vec(8, 8), Vec(9, 9))
        )
        assertEquals(Vec(12, 12), dim)
        assertEquals(expectedAntennas, antennas)
    }

    @Test
    fun testPart1() {
        val (antennas, dim) = parseInput(streamInput("08.txt"))
        assertEquals(14, part1(antennas, dim))
    }

    @Test
    fun testPart2() {
        val (antennas, dim) = parseInput(streamInput("08.txt"))
        assertEquals(34, part2(antennas, dim))
    }
}

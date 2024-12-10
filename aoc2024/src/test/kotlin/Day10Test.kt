import common.*
import day10.Day10
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    @Test
    fun testParseInput() {
        val solution = Day10(streamInput("10.txt"))
        val expectedMap = mapOf(
            Vec(0, 0) to 8, Vec(0, 1) to 9, Vec(0, 2) to 0, Vec(0, 3) to 1, Vec(0, 4) to 0, Vec(0, 5) to 1, Vec(0, 6) to 2, Vec(0, 7) to 3,
            Vec(1, 0) to 7, Vec(1, 1) to 8, Vec(1, 2) to 1, Vec(1, 3) to 2, Vec(1, 4) to 1, Vec(1, 5) to 8, Vec(1, 6) to 7, Vec(1, 7) to 4,
            Vec(2, 0) to 8, Vec(2, 1) to 7, Vec(2, 2) to 4, Vec(2, 3) to 3, Vec(2, 4) to 0, Vec(2, 5) to 9, Vec(2, 6) to 6, Vec(2, 7) to 5,
            Vec(3, 0) to 9, Vec(3, 1) to 6, Vec(3, 2) to 5, Vec(3, 3) to 4, Vec(3, 4) to 9, Vec(3, 5) to 8, Vec(3, 6) to 7, Vec(3, 7) to 4,
            Vec(4, 0) to 4, Vec(4, 1) to 5, Vec(4, 2) to 6, Vec(4, 3) to 7, Vec(4, 4) to 8, Vec(4, 5) to 9, Vec(4, 6) to 0, Vec(4, 7) to 3,
            Vec(5, 0) to 3, Vec(5, 1) to 2, Vec(5, 2) to 0, Vec(5, 3) to 1, Vec(5, 4) to 9, Vec(5, 5) to 0, Vec(5, 6) to 1, Vec(5, 7) to 2,
            Vec(6, 0) to 0, Vec(6, 1) to 1, Vec(6, 2) to 3, Vec(6, 3) to 2, Vec(6, 4) to 9, Vec(6, 5) to 8, Vec(6, 6) to 0, Vec(6, 7) to 1,
            Vec(7, 0) to 1, Vec(7, 1) to 0, Vec(7, 2) to 4, Vec(7, 3) to 5, Vec(7, 4) to 6, Vec(7, 5) to 7, Vec(7, 6) to 3, Vec(7, 7) to 2,
        )
        assertEquals(expectedMap, solution.map)
        assertEquals(Vec(8, 8), solution.dim)
    }

    @Test
    fun testPart1() {
        val solution = Day10(streamInput("10.txt"))
        assertEquals(36, solution.part1())
    }

    @Test
    fun testPart2() {
        val solution = Day10(streamInput("10.txt"))
        assertEquals(81, solution.part2())
    }
}

import common.*
import day15.Warehouse
import day15.parseInput
import day15.double
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day15Test {
    companion object {
        @JvmStatic
        fun part1() = listOf(
            Arguments.of("small", 2028),
            Arguments.of("big", 10092)
        )
        @JvmStatic
        fun doubling() = listOf(
            Arguments.of("big", """
                ####################
                ##....[]....[]..[]##
                ##............[]..##
                ##..[][]....[]..[]##
                ##....[]@.....[]..##
                ##[]##....[]......##
                ##[]....[]....[]..##
                ##..[][]..[]..[][]##
                ##........[]......##
                ####################
            """.trimIndent()),
            Arguments.of("small-2", """
                ##############
                ##......##..##
                ##..........##
                ##....[][]@.##
                ##....[]....##
                ##..........##
                ##############
            """.trimIndent())
        )
    }

    @Test
    fun `Input parsing works`() {
        val parsed = parseInput(streamInput("15-small.txt"))
        val expectedMap = mapOf(
            Vec(0, 0) to '#', Vec(0, 1) to '#', Vec(0, 2) to '#', Vec(0, 3) to '#', Vec(0, 4) to '#', Vec(0, 5) to '#', Vec(0, 6) to '#', Vec(0, 7) to '#',
            Vec(1, 0) to '#', Vec(1, 1) to '.', Vec(1, 2) to '.', Vec(1, 3) to 'O', Vec(1, 4) to '.', Vec(1, 5) to 'O', Vec(1, 6) to '.', Vec(1, 7) to '#',
            Vec(2, 0) to '#', Vec(2, 1) to '#', Vec(2, 2) to '@', Vec(2, 3) to '.', Vec(2, 4) to 'O', Vec(2, 5) to '.', Vec(2, 6) to '.', Vec(2, 7) to '#',
            Vec(3, 0) to '#', Vec(3, 1) to '.', Vec(3, 2) to '.', Vec(3, 3) to '.', Vec(3, 4) to 'O', Vec(3, 5) to '.', Vec(3, 6) to '.', Vec(3, 7) to '#',
            Vec(4, 0) to '#', Vec(4, 1) to '.', Vec(4, 2) to '#', Vec(4, 3) to '.', Vec(4, 4) to 'O', Vec(4, 5) to '.', Vec(4, 6) to '.', Vec(4, 7) to '#',
            Vec(5, 0) to '#', Vec(5, 1) to '.', Vec(5, 2) to '.', Vec(5, 3) to '.', Vec(5, 4) to 'O', Vec(5, 5) to '.', Vec(5, 6) to '.', Vec(5, 7) to '#',
            Vec(6, 0) to '#', Vec(6, 1) to '.', Vec(6, 2) to '.', Vec(6, 3) to '.', Vec(6, 4) to '.', Vec(6, 5) to '.', Vec(6, 6) to '.', Vec(6, 7) to '#',
            Vec(7, 0) to '#', Vec(7, 1) to '#', Vec(7, 2) to '#', Vec(7, 3) to '#', Vec(7, 4) to '#', Vec(7, 5) to '#', Vec(7, 6) to '#', Vec(7, 7) to '#',
        )
        val expectedCmd = listOf(LEFT, UP, UP, RIGHT, RIGHT, RIGHT, DOWN, DOWN, LEFT, DOWN, RIGHT, RIGHT, DOWN, LEFT, LEFT)
        assertEquals(Warehouse(expectedMap, expectedCmd), parsed)
    }

    @ParameterizedTest(name = "Doubling for {0} is correct")
    @MethodSource("doubling")
    fun `Doubling works`(name: String, expected: String) {
        val map = streamInput("15-$name.txt")
            .bufferedReader()
            .readText()
            .split("\n\n", limit = 2)
            .first()
            .lines()
            .joinToString("\n", transform = ::double)
        assertEquals(expected, map)
    }

    @ParameterizedTest(name = "Input {0} should give {1}")
    @MethodSource("part1")
    fun `Part 1 Examples work`(name: String, expected: Int) {
        assertEquals(expected, day15.part1(parseInput(streamInput("15-$name.txt"))))
    }

    @Test
    fun `Part 2 Example works`() {
        assertEquals(9021, day15.part2(parseInput(streamInput("15-big.txt"), double = true)))
    }
}

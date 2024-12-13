import common.Vec
import day12.parseInput
import day12.part1
import day12.part2
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    companion object {
        @JvmStatic
        fun part1() = listOf(
            Arguments.of("1", 140),
            Arguments.of("2", 772),
            Arguments.of("3", 1930),
        )

        @JvmStatic
        fun part2() = listOf(
            Arguments.of("1", 80),
            Arguments.of("2", 436),
            Arguments.of("E", 236),
            Arguments.of("AB", 368),
            Arguments.of("3", 1206),
        )
    }

    @Test
    fun `Input parsing works`() {
        val expected = mapOf(
            Vec(0, 0) to 'A', Vec(0, 1) to 'A',Vec(0, 2) to 'A',Vec(0, 3) to 'A',
            Vec(1, 0) to 'B', Vec(1, 1) to 'B',Vec(1, 2) to 'C',Vec(1, 3) to 'D',
            Vec(2, 0) to 'B', Vec(2, 1) to 'B',Vec(2, 2) to 'C',Vec(2, 3) to 'C',
            Vec(3, 0) to 'E', Vec(3, 1) to 'E',Vec(3, 2) to 'E',Vec(3, 3) to 'C',
        )
        assertEquals(expected, parseInput(streamInput("12-1.txt")))
    }

    @ParameterizedTest(name = "Input {0} should give {2}")
    @MethodSource("part1")
    fun `Part 1 examples work`(name: String, expected: Int) {
        assertEquals(expected, part1(parseInput(streamInput("12-$name.txt"))))
    }


    @ParameterizedTest(name = "Input {0} should give {2}")
    @MethodSource("part2")
    fun `Part 2 examples work`(name: String, expected: Int) {
        assertEquals(expected, part2(parseInput(streamInput("12-$name.txt"))))
    }
}

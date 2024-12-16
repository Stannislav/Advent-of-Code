import common.*
import day16.parseInput
import day16.part1
import day16.part2
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class Day16Test {
    companion object {
        @JvmStatic
        fun parsing() = listOf(
            Arguments.of("small", Vec(13, 1), Vec(1, 13), """
                ###############
                #.......#.....#
                #.#.###.#.###.#
                #.....#.#...#.#
                #.###.#####.#.#
                #.#.#.......#.#
                #.#.#####.###.#
                #...........#.#
                ###.#.#####.#.#
                #...#.....#.#.#
                #.#.#.###.#.#.#
                #.....#...#.#.#
                #.###.#.#.#.#.#
                #...#.....#...#
                ###############
            """.trimIndent()
            ),
            Arguments.of("big", Vec(15, 1), Vec(1, 15), """
                #################
                #...#...#...#...#
                #.#.#.#.#.#.#.#.#
                #.#.#.#...#...#.#
                #.#.#.#.###.#.#.#
                #...#.#.#.....#.#
                #.#.#.#.#.#####.#
                #.#...#.#.#.....#
                #.#.#####.#.###.#
                #.#.#.......#...#
                #.#.###.#####.###
                #.#.#...#.....#.#
                #.#.#.#####.###.#
                #.#.#.........#.#
                #.#.#.#########.#
                #.#.............#
                #################
            """.trimIndent())
        )
        @JvmStatic
        fun part1examples() = listOf(
            Arguments.of("small", 7036),
            Arguments.of("big", 11048)
        )
        @JvmStatic
        fun part2examples() = listOf(
            Arguments.of("small", 45),
            Arguments.of("big", 64)
        )
    }
    @ParameterizedTest(name = "Parsing of {0} works")
    @MethodSource("parsing")
    fun `Input parsing works`(name: String, expectedStart: Vec, expectedEnd: Vec, expectedRendered: String) {
        val maze = parseInput(streamInput("16-$name.txt"))
        assertEquals(expectedStart, maze.start)
        assertEquals(expectedEnd, maze.end)
        val lim = maze.map.keys.maxBy { it.i + it.j } + Vec(1, 1)
        val rendered = (0 until lim.i).joinToString("\n") { i ->
            (0 until lim.j).map { j -> maze.map[Vec(i, j)] }.joinToString("")
        }
        assertEquals(expectedRendered, rendered)
    }

    @ParameterizedTest(name = "Example {0} works")
    @MethodSource("part1examples")
    fun `Part 1 examples work`(name: String, expectedScore: Int) {
        val maze = parseInput(streamInput("16-$name.txt"))
        assertEquals(expectedScore, part1(maze))
    }

    @ParameterizedTest(name = "Example {0} works")
    @MethodSource("part2examples")
    fun `Part 2 examples work`(name: String, expectedScore: Int) {
        val maze = parseInput(streamInput("16-$name.txt"))
        assertEquals(expectedScore, part2(maze))
    }
}

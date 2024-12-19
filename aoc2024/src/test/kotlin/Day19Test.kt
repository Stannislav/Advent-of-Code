import day19.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun `Input parsing works`() {
        val (towels, patterns) = parseInput(streamInput("19.txt"))
        val expectedTowels = listOf("r", "wr", "b", "g", "bwu", "rb", "gb", "br")
        val expectedPatterns = listOf(
            "brwrr",
            "bggr",
            "gbbr",
            "rrbgbr",
            "ubwu",
            "bwurrg",
            "brgr",
            "bbrgwb"
        )
        assertEquals(expectedTowels, towels)
        assertEquals(expectedPatterns, patterns)
    }

    @Test
    fun `Part 1 example works`() {
        val (towels, patterns) = parseInput(streamInput("19.txt"))
        assertEquals(6, part1(towels, patterns))
    }

    @Test
    fun `Part 1 patterns work individually`() {
        val (towels, patterns) = parseInput(streamInput("19.txt"))
        val expected = listOf(true, true, true, true, false, true, true, false)
        assertEquals(expected, patterns.map { arrangements(it, towels) != 0L })
    }

    @Test
    fun `Part 2 example works`() {
        val (towels, patterns) = parseInput(streamInput("19.txt"))
        assertEquals(16, part2(towels, patterns))
    }

    @Test
    fun `Part 2 patterns work individually`() {
        val (towels, patterns) = parseInput(streamInput("19.txt"))
        val expected = listOf<Long>(2, 1, 4, 6, 0, 1, 2, 0)
        assertEquals(expected, patterns.map { arrangements(it, towels) })
    }
}

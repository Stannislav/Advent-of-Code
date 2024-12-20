import day19.Day19
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun `Input parsing works`() {
        val day19 = Day19.fromStream(streamInput("19.txt"))
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
        assertEquals(expectedTowels, day19.towels)
        assertEquals(expectedPatterns, day19.patterns)
    }

    @Test
    fun `Part 1 example works`() {
        val day19 = Day19.fromStream(streamInput("19.txt"))
        assertEquals(6, day19.part1())
    }

    @Test
    fun `Part 1 patterns work individually`() {
        val day19 = Day19.fromStream(streamInput("19.txt"))
        val expected = listOf(true, true, true, true, false, true, true, false)
        assertEquals(expected, day19.countAll().map { it != 0L })
    }

    @Test
    fun `Part 2 example works`() {
        val day19 = Day19.fromStream(streamInput("19.txt"))
        assertEquals(16, day19.part2())
    }

    @Test
    fun `Part 2 patterns work individually`() {
        val day19 = Day19.fromStream(streamInput("19.txt"))
        val expected = listOf<Long>(2, 1, 4, 6, 0, 1, 2, 0)
        assertEquals(expected, day19.countAll())
    }
}

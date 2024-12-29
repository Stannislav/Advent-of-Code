import day21.parseInput
import day21.part1
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun `Input parsing works`() {
        val input = parseInput(streamInput("21.txt"))
        val expected = listOf("029A", "980A", "179A", "456A", "379A")
        assertEquals(expected, input)
    }

    @Test
    fun `Part 1 example works`() {
        val input = parseInput(streamInput("21.txt"))
        assertEquals(126384, part1(input))
    }
}

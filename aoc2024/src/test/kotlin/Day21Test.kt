import day21.parseInput
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun `Input parsing works`() {
        val input = parseInput(streamInput("21.txt"))
        val expected = listOf("029A", "980A", "179A", "456A", "379A")
        assertEquals(expected, input)
    }
}

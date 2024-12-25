import day25.parseInput
import day25.part1
import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {
    @Test
    fun `Input parsing works`() {
        val (locks, keys) = parseInput(streamInput("25.txt"))
        val expectedLocks = setOf(
            listOf(0, 5, 3, 4, 3),
            listOf(1, 2, 0, 5, 3),
        )
        val expectedKeys = setOf(
            listOf(5, 0, 2, 1, 3),
            listOf(4, 3, 4, 0, 2),
            listOf(3, 0, 2, 0, 1),
        )
        assertEquals(expectedLocks, locks)
        assertEquals(expectedKeys, keys)
    }

    @Test
    fun `Part 1 example works`() {
        val (locks, keys) = parseInput(streamInput("25.txt"))
        assertEquals(3, part1(locks, keys))
    }
}

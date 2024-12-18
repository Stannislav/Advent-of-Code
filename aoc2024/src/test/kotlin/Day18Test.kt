import common.Vec
import day18.parseInput
import day18.part1
import day18.part2
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun `Input parsing works`() {
        val bytes = parseInput(streamInput("18.txt"))
        val expected = listOf(
            Vec(5, 4), Vec(4, 2), Vec(4, 5), Vec(3, 0), Vec(2, 1),
            Vec(6, 3), Vec(2, 4), Vec(1, 5), Vec(0, 6), Vec(3, 3),
            Vec(2, 6), Vec(5, 1), Vec(1, 2), Vec(5, 5), Vec(2, 5),
            Vec(6, 5), Vec(1, 4), Vec(0, 4), Vec(6, 4), Vec(1, 1),
            Vec(6, 1), Vec(1, 0), Vec(0, 5), Vec(1, 6), Vec(2, 0)
        )
        assertEquals(expected, bytes)
    }

    @Test
    fun `Part 1 example works`() {
        val bytes = parseInput(streamInput("18.txt"))
        assertEquals(22, part1(bytes, Vec(6, 6), 12))
    }

    @Test
    fun `Part 2 example works`() {
        val bytes = parseInput(streamInput("18.txt"))
        assertEquals("6,1", part2(bytes, Vec(6, 6), 12))
    }
}

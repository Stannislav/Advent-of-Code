import common.Vec
import day14.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    @Test
    fun `Input parsing works`() {
        val parsed = parseInput(streamInput("14.txt"))
        val expected = listOf(
            Robot(Vec(0, 4), Vec(3, -3)),
            Robot(Vec(6, 3), Vec(-1, -3)),
            Robot(Vec(10, 3), Vec(-1, 2)),
            Robot(Vec(2, 0), Vec(2, -1)),
            Robot(Vec(0, 0), Vec(1, 3)),
            Robot(Vec(3, 0), Vec(-2, -2)),
            Robot(Vec(7, 6), Vec(-1, -3)),
            Robot(Vec(3, 0), Vec(-1, -2)),
            Robot(Vec(9, 3), Vec(2, 3)),
            Robot(Vec(7, 3), Vec(-1, 2)),
            Robot(Vec(2, 4), Vec(2, -3)),
            Robot(Vec(9, 5), Vec(-3, -3)),
        )
        assertEquals(expected, parsed)
    }

    @Test
    fun `Part 1 example works`() {
        val robots = parseInput(streamInput("14.txt"))
        assertEquals(12, part1(Room(robots, Vec(11, 7))))
    }
}

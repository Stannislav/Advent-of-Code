import day13.Machine
import day13.parseInput
import day13.part1
import kotlin.test.*

class Day13Test {
    @Test
    fun `Input parsing works`() {
        val parsed = parseInput(streamInput("13.txt"))
        val expected = listOf(
            Machine(94, 34, 22, 67, 8400, 5400),
            Machine(26, 66, 67, 21, 12748, 12176),
            Machine(17, 86, 84, 37, 7870, 6450),
            Machine(69, 23, 27, 71, 18641, 10279),
        )
        assertEquals(expected, parsed)
    }

    @Test
    fun `Part 1 example works`() {
        val machines = parseInput(streamInput("13.txt"))
        assertEquals(480, part1(machines))
    }

    @Test
    fun `Part 2 example works`() {
        val machines = parseInput(streamInput("13.txt")).map { it.enhance() }
        assertNull(machines[0].getCost())
        assertTrue(machines[1].getCost()!! > 100)
        assertNull(machines[2].getCost())
        assertTrue(machines[3].getCost()!! > 100)
    }
}

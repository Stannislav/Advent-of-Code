import day17.Computer
import day17.parseInput
import day17.part1
import day17.part2
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    @Test
    fun `Input parsing works`() {
        val parsed = parseInput(streamInput("17-1.txt"))
        val expected = Computer(729, 0, 0, listOf(0, 1, 5, 4, 3, 0))
        assertEquals(expected, parsed)
    }

    @Test
    fun `Part 1 example works`() {
        val computer = parseInput(streamInput("17-1.txt"))
        assertEquals("4,6,3,5,6,3,5,2,1,0", part1(computer))
    }

    @Test
    fun `Part 2 example works`() {
        val computer = parseInput(streamInput("17-2.txt"))
        assertEquals(117440, part2(computer))
    }

    @Test
    fun `If register C contains 9, the program 2,6 would set register B to 1`() {
        val computer = Computer(0, 0, 9, listOf(2, 6))
        computer.run()
        assertEquals(1, computer.getB())
    }

    @Test
    fun `If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2`() {
        val computer = Computer(10, 0, 0, listOf(5, 0, 5, 1, 5, 4))
        computer.run()
        assertEquals(listOf<Long>(0, 1, 2), computer.readout())
    }

    @Test
    fun `If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A`() {
        val computer = Computer(2024, 0, 0, listOf(0, 1, 5, 4, 3, 0))
        computer.run()
        assertEquals(listOf<Long>(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0), computer.readout())
        assertEquals(0, computer.getA())
    }

    @Test
    fun `If register B contains 29, the program 1,7 would set register B to 26`() {
        val computer = Computer(0, 29, 0, listOf(1, 7))
        computer.run()
        assertEquals(26, computer.getB())
    }

    @Test
    fun `If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354`() {
        val computer = Computer(0, 2024, 43690, listOf(4, 0))
        computer.run()
        assertEquals(44354, computer.getB())
    }
}

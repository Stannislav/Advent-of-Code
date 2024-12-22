import day22.nextSecret
import day22.parseInput
import day22.part1
import day22.part2
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {
    @Test
    fun `Secret generation works`() {
        val result = generateSequence(123L, ::nextSecret).take(11).toList()
        val expected = listOf<Long>(
            123,
            15887950,
            16495136,
            527345,
            704524,
            1553684,
            12683156,
            11100544,
            12249484,
            7753432,
            5908254
        )
        assertEquals(expected, result)
    }

    @Test
    fun `Input parsing works`() {
        val input = parseInput(streamInput("22-1.txt"))
        assertEquals(listOf<Long>(1, 10, 100, 2024), input)
    }

    @Test
    fun `Part 1 example works`() {
        val input = parseInput(streamInput("22-1.txt"))
        assertEquals(37327623, part1(input))
    }

    @Test
    fun `Part 2 example works`() {
        val input = parseInput(streamInput("22-2.txt"))
        assertEquals(23, part2(input))
    }
}

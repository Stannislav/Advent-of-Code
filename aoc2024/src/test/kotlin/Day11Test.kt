import day11.Day11
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test
    fun testParseInput() {
        val solution = Day11(streamInput("11.txt"))
        assertEquals(listOf<Long>(1, 2024, 1, 0, 9, 9, 2021976), solution.input)
    }

    @Test
    fun testBlink() {
        assertEquals(7, Day11("0 1 10 99 999".byteInputStream()).blink(1))
        assertEquals(22, Day11("125 17".byteInputStream()).blink(6))
        assertEquals(55312, Day11("125 17".byteInputStream()).blink(25))
    }
}

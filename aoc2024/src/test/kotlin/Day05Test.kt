import day05.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {
    @Test
    fun testIsValid() {
        val (rules, updates) = parseInput(streamInput("05.txt"))
        assertEquals(
            listOf(true, true, true, false, false, false),
            updates.map {isValid(it, rules) }
        )
    }

    @Test
    fun testOrder() {
        val (rules, updates) = parseInput(streamInput("05.txt"))
        assertEquals(listOf(97, 75, 47, 61, 53), order(updates[3], rules))
        assertEquals(listOf(61, 29, 13), order(updates[4], rules))
        assertEquals(listOf(97, 75, 47, 29, 13), order(updates[5], rules))
    }

    @Test
    fun testPart1() {
        val (rules, updates) = parseInput(streamInput("05.txt"))
        assertEquals(143, part1(rules, updates))
    }

    @Test
    fun testPart2() {
        val (rules, updates) = parseInput(streamInput("05.txt"))
        assertEquals(123, part2(rules, updates))
    }
}

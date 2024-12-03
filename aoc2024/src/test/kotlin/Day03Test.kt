import day03.findAll
import day03.findAllConditional
import day03.part1
import day03.part2
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals



class Day03Test {
    private val input1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    private val input2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    @Test
    fun testFindAll() {
        assertEquals(listOf(Pair(2, 4), Pair(5, 5), Pair(11, 8), Pair(8, 5)), findAll(input1))
    }

    @Test
    fun testFindAllConditional() {
        assertEquals(listOf(Pair(2, 4), Pair(8, 5)), findAllConditional(input2))
    }

    @Test
    fun testPart1() {
        assertEquals(161, part1(input1))
    }

    @Test
    fun testPart2() {
        assertEquals(48, part2(input2))
    }
}

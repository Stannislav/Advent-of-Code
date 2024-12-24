import day24.Device
import day24.Gate
import day24.part1
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {
    @Test
    fun `Input parsing works`() {
        val device = Device.fromStream(streamInput("24-1.txt"))
        val expectedInputValues = mapOf(
            "x00" to 1, "x01" to 1, "x02" to 1,
            "y00" to 0, "y01" to 1, "y02" to 0)
        val expectedGates = listOf(
            Gate(Int::and, "x00", "y00", "z00"),
            Gate(Int::xor, "x01", "y01", "z01"),
            Gate(Int::or, "x02", "y02", "z02")
        )
        assertEquals(Device(expectedInputValues, expectedGates), device)
    }

    companion object {
        @JvmStatic
        fun part1() = listOf(
            Arguments.of("1", 4L),
            Arguments.of("2", 2024L)
        )
    }

    @ParameterizedTest(name = "Input {0}")
    @MethodSource("part1")
    fun `Part 1 examples work`(name: String, expected: Long) {
        val device = Device.fromStream(streamInput("24-$name.txt"))
        assertEquals(expected, part1(device))
    }
}

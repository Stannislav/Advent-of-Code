import common.Vec
import day12.parseInput
import day12.part1
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    private val input1 = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent()
    private val input2 = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent()
    private val input3 = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()

    @Test
    fun `Should parse input correctly`() {
        val expected = mapOf(
            Vec(0, 0) to 'A', Vec(0, 1) to 'A',Vec(0, 2) to 'A',Vec(0, 3) to 'A',
            Vec(1, 0) to 'B', Vec(1, 1) to 'B',Vec(1, 2) to 'C',Vec(1, 3) to 'D',
            Vec(2, 0) to 'B', Vec(2, 1) to 'B',Vec(2, 2) to 'C',Vec(2, 3) to 'C',
            Vec(3, 0) to 'E', Vec(3, 1) to 'E',Vec(3, 2) to 'E',Vec(3, 3) to 'C',
        )
        assertEquals(expected, parseInput(input1.byteInputStream()))
    }

    @Test
    fun testPart1() {
//        assertEquals(140, part1(parseInput(input1.byteInputStream())))
        assertEquals(772, part1(parseInput(input2.byteInputStream())))
        assertEquals(1930, part1(parseInput(input3.byteInputStream())))
    }
}

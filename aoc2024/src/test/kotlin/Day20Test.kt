import common.Vec
import common.i
import common.j
import common.plus
import day20.Day20
import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test {
    @Test
    fun `Input parsing works`() {
        val day20 = Day20.fromStream(streamInput("20.txt"))
        val expectedMap = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #.#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###...#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent()
        val lim = Vec(day20.map.maxOf { it.i }, day20.map.maxOf { it.j }) + Vec(1, 1)
        val map = (0..lim.i).joinToString("\n") { i ->
            (0..lim.j).map { j ->
                if (day20.map.contains(Vec(i, j))) '.' else '#'
            }.joinToString("")
        }
        assertEquals(expectedMap, map)
        assertEquals(Vec(3, 1), day20.start)
        assertEquals(Vec(7, 5), day20.end)

    }

    @Test
    fun `Total real distance is correct`() {
        val day20 = Day20.fromStream(streamInput("20.txt"))
        assertEquals(84, day20.realDistance)
    }

    @Test
    fun `Part 1 example works`() {
        val day20 = Day20.fromStream(streamInput("20.txt"))

        val withCheats = day20.cheatSavings(2)
        val expectedWithCheats = mapOf(
            2 to 14,
            4 to 14,
            6 to 2,
            8 to 4,
            10 to 2,
            12 to 3,
            20 to 1,
            36 to 1,
            38 to 1,
            40 to 1,
            64 to 1
        ).toSortedMap()
        assertEquals(expectedWithCheats, withCheats)
    }

    @Test
    fun `Part 2 example works`() {
        val day20 = Day20.fromStream(streamInput("20.txt"))

        val withCheats = day20.cheatSavings(20).filterKeys { it >= 50 }
        val expectedWithCheats = mapOf(
            50 to 32,
            52 to 31,
            54 to 29,
            56 to 39,
            58 to 25,
            60 to 23,
            62 to 20,
            64 to 19,
            66 to 12,
            68 to 14,
            70 to 12,
            72 to 22,
            74 to 4,
            76 to 3,
        ).toSortedMap()
        assertEquals(expectedWithCheats, withCheats)
    }
}

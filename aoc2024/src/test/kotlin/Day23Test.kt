import day23.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun `Input parsing works`() {
        val network = Network.fromStream(streamInput("23.txt"))
        val expectedLinks = setOf(
            setOf("kh", "tc"),
            setOf("qp", "kh"),
            setOf("de", "cg"),
            setOf("ka", "co"),
            setOf("yn", "aq"),
            setOf("qp", "ub"),
            setOf("cg", "tb"),
            setOf("vc", "aq"),
            setOf("tb", "ka"),
            setOf("wh", "tc"),
            setOf("yn", "cg"),
            setOf("kh", "ub"),
            setOf("ta", "co"),
            setOf("de", "co"),
            setOf("tc", "td"),
            setOf("tb", "wq"),
            setOf("wh", "td"),
            setOf("ta", "ka"),
            setOf("td", "qp"),
            setOf("aq", "cg"),
            setOf("wq", "ub"),
            setOf("ub", "vc"),
            setOf("de", "ta"),
            setOf("wq", "aq"),
            setOf("wq", "vc"),
            setOf("wh", "yn"),
            setOf("ka", "de"),
            setOf("kh", "ta"),
            setOf("co", "tc"),
            setOf("wh", "qp"),
            setOf("tb", "vc"),
            setOf("td", "yn"),
        )
        val expectedTargets = mapOf(
            "aq" to setOf("yn", "vc", "cg", "wq"),
            "cg" to setOf("de", "tb", "yn", "aq"),
            "co" to setOf("ka", "ta", "de", "tc"),
            "de" to setOf("cg", "co", "ta", "ka"),
            "ka" to setOf("co", "tb", "ta", "de"),
            "kh" to setOf("tc", "qp", "ub", "ta"),
            "qp" to setOf("kh", "ub", "td", "wh"),
            "ta" to setOf("co", "ka", "de", "kh"),
            "tb" to setOf("cg", "ka", "wq", "vc"),
            "tc" to setOf("kh", "wh", "td", "co"),
            "td" to setOf("tc", "wh", "qp", "yn"),
            "ub" to setOf("qp", "kh", "wq", "vc"),
            "vc" to setOf("aq", "ub", "wq", "tb"),
            "wh" to setOf("tc", "td", "yn", "qp"),
            "wq" to setOf("tb", "ub", "aq", "vc"),
            "yn" to setOf("aq", "cg", "wh", "td"),
        )
        assertEquals(expectedLinks, network.links)
        assertEquals(network.targets, expectedTargets)
    }

    @Test
    fun `Clique growing works`() {
        val network = Network.fromStream(streamInput("23.txt"))
        val expectedTriples = setOf(
            setOf("aq","cg","yn"),
            setOf("aq","vc","wq"),
            setOf("co","de","ka"),
            setOf("co","de","ta"),
            setOf("co","ka","ta"),
            setOf("de","ka","ta"),
            setOf("kh","qp","ub"),
            setOf("qp","td","wh"),
            setOf("tb","vc","wq"),
            setOf("tc","td","wh"),
            setOf("td","wh","yn"),
            setOf("ub","vc","wq"),
        )
        assertEquals(expectedTriples, network.grow(network.links))
    }

    @Test
    fun `Part 1 example works`() {
        val network = Network.fromStream(streamInput("23.txt"))
        assertEquals(7, part1(network))
    }

    @Test
    fun `Part 2 example works`() {
        val network = Network.fromStream(streamInput("23.txt"))
        assertEquals("co,de,ka,ta", part2(network))
    }
}

package day23

import java.io.File
import java.io.InputStream

fun main() {
    val network = Network.fromStream(File("input/23.txt").inputStream())
    println("Part 1: ${part1(network)}")
    println("Part 2: ${part2(network)}")
}

class Network private constructor(val links: Set<Set<String>>, val targets: Map<String, Set<String>>) {
    companion object {
        fun fromStream(stream: InputStream): Network {
            val pairs = stream.bufferedReader().lineSequence().map {
                val (c1, c2) = it.split("-", limit = 2)
                Pair(c1, c2)
            }.toList()

            val links = pairs.map { setOf(it.first, it.second) }.toSet()
            val targets = mutableMapOf<String, MutableSet<String>>()
            pairs.forEach { (c1, c2) ->
                targets.getOrPut(c1) { mutableSetOf() }.add(c2)
                targets.getOrPut(c2) { mutableSetOf() }.add(c1)
            }

            return Network(links, targets)
        }
    }

    /**
     * Grow given computer cliques by one additional computer.
     *
     * Cliques that can't grow are dropped and only the set of grown cliques is returned.
     *
     * To grow a clique consider all member computers and the sets of their connections.
     * The intersection of all connection sets gives the set of target computers in common.
     * Each of these computers, if not already in the clique, can be used to grow the clique by one.
     */
    fun grow(cliques: Set<Set<String>>): Set<Set<String>> {
        return cliques.flatMap { clique ->
            clique
                .mapNotNull { targets[it] }
                .reduce { acc, targets -> acc intersect targets }
                .run { this - clique }
                .map { clique + it }
        }.toSet()
    }
}

fun part1(network: Network): Int {
    return network
        .grow(network.links)
        .count { triple -> triple.any { it.startsWith("t") }}
}

fun part2(network: Network): String {
    var cliques = network.links
    while (cliques.size > 1)
        cliques = network.grow(cliques)
    check(cliques.size == 1)
    return cliques.first().sorted().joinToString(",")
}

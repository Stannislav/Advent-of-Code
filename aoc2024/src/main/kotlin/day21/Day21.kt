package day21

import common.*
import java.io.File
import java.io.InputStream
import java.util.*

fun main() {
    println("--- Day 21: Keypad Conundrum ---")
    val input = parseInput(File("input/21.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): List<String> = stream.bufferedReader().readLines()
fun part1(input: List<String>): Long = solve(input, 2)
fun part2(input: List<String>): Long = solve(input, 25)

fun solve(input: List<String>, nKeypads: Int): Long {
    val arrowKeypad = Keypad.fromString(" ^A\n<v>")
    val numericKeypad = Keypad.fromString("789\n456\n123\n 0A")
    val robot = Robot(numericKeypad, (1..nKeypads).fold(Human as Agent) { agent, _ -> Robot(arrowKeypad, agent)})

    return input.sumOf { line -> line.substring(0, 3).toInt() * line.sumOf(robot::pressButton) }
}

class Keypad private constructor(private val positions: Map<Char, Vec>) {
    companion object {
        fun fromString(layout: String): Keypad {
            val positions = layout
                .lineSequence()
                .flatMapIndexed { i, line -> line.mapIndexed { j, char -> char to Vec(i, j) } }
                .toMap()
                .filterKeys { it != ' ' }
            return Keypad(positions)
        }
    }

    private val pathCache = mutableMapOf<Pair<Char, Char>, List<List<Char>>>()

    fun getAllPaths(from: Char, to: Char): List<List<Char>> = pathCache.getOrPut(Pair(from, to)) {
        // Flood map with BFS.
        val fromPos = positions[from] ?: error("position of from=$from is unknown")
        val toPos = positions[to] ?: error("position of to=$to is unknown")
        val validPositions = positions.values.toSet()
        val distances = mutableMapOf(fromPos to 0)
        val q: Queue<Vec> = LinkedList()
        q.add(fromPos)
        while (q.isNotEmpty()) {
            val pos = q.remove()
            sequenceOf(UP, DOWN, LEFT, RIGHT)
                .map { pos + it }
                .filter { validPositions.contains(it) }
                .filterNot { distances.contains(it) }
                .forEach { newPos ->
                    distances[newPos] = distances[pos]?.let { it + 1 } ?: error("unknown distance for $pos")
                    q.add(newPos)
                }

        }

        // Given node distances computed via BFS above, build all possible paths.
        fun buildPaths(currentPos: Vec): List<List<Vec>> {
            if (currentPos == toPos)
                return listOf(listOf(currentPos))
            val currentDist = distances[currentPos] ?: error("unknown distance for $currentPos")
            return sequenceOf(UP, DOWN, LEFT, RIGHT)
                .map { currentPos + it }
                .filter { distances[it]?.let { dist -> dist == currentDist + 1 } ?: false }
                .flatMap { buildPaths(it).map { path -> listOf(currentPos) + path } }
                .toList()
        }

        // Map path positions to direction characters.
        buildPaths(fromPos)
            .map { path ->
                path.zip(path.drop(1)).map {
                    when(it.second - it.first) {
                        UP -> '^'
                        DOWN -> 'v'
                        LEFT -> '<'
                        RIGHT -> '>'
                        else -> error("unknown direction $it")
                    }
                }
            }
    }
}

sealed class Agent {
    abstract fun pressButton(button: Char): Long
}

data object Human : Agent() {
    override fun pressButton(button: Char): Long  = 1
}

class Robot(private val targetKeypad: Keypad, private val controller: Agent) : Agent() {
    private var position = 'A'
    private val cache = mutableMapOf<Pair<Char, Char>, Long>()

    override fun pressButton(button: Char): Long = cache.getOrPut(Pair(position, button)) {
        targetKeypad.getAllPaths(position, button)
            .map { it + 'A' }
            .minOf { it.sumOf { button -> controller.pressButton(button) } }
    }
    .also { position = button }
}

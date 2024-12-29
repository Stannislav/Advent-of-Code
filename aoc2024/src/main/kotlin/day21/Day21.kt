package day21

import common.*
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.abs

fun main() {
    val input = parseInput(File("input/21.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int = solve(input, 2)
fun part2(input: List<String>): Int = solve(input, 25)


fun solve(input: List<String>, nKeypads: Int): Int {
    val arrowKeypad = Keypad.fromString(" ^A\n<v>")
    val numericKeypad = Keypad.fromString("789\n456\n123\n 0A")
    val robot = Robot(numericKeypad, (1..nKeypads).fold(Human as Agent) { agent, _ -> Robot(arrowKeypad, agent)})

    return input.sumOf { line -> line.substring(0, 3).toInt() * line.sumOf(robot::pressButton) }
}

fun parseInput(stream: InputStream): List<String> {
    return stream.bufferedReader().readLines()
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
        // flood map with dfs
        val fromPos = positions[from] ?: error("position of from=$from is unknown")
        val toPos = positions[to] ?: error("position of to=$to is unknown")
        val valid = positions.values.toSet()
        val distances = mutableMapOf(fromPos to 0)
        val q: Queue<Vec> = LinkedList()
        q.add(fromPos)
        while (q.isNotEmpty()) {
            val pos = q.remove()
            sequenceOf(UP, DOWN, LEFT, RIGHT)
                .map { pos + it }
                .filter { valid.contains(it) }
                .filterNot { distances.contains(it) }
                .forEach { newPos ->
                    distances[newPos] = distances[pos]?.let { it + 1 } ?: error("unknown distance for $pos")
                    q.add(newPos)
                }

        }

        fun buildPaths(currentPos: Vec): List<List<Vec>> {
            if (currentPos == toPos) {
                return listOf(listOf(currentPos))
            }
            return sequenceOf(UP, DOWN, LEFT, RIGHT)
                .map { currentPos + it }
                .filter { valid.contains(it) }
                .filter { distances[it]!! == distances[currentPos]!! + 1 }
                .flatMap { buildPaths(it).map { path -> listOf(currentPos) + path } }
                .toList()
        }

        return buildPaths(fromPos)
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
    abstract fun pressButton(button: Char): Int
    abstract fun reset()
    abstract fun printLog()
    abstract fun typeSequence(seq: String): Int
}

data object Human : Agent() {
    private var log = ""

    override fun pressButton(button: Char): Int {
        log += button
        return 1
    }
    override fun reset() {
        log = ""
    }
    override fun printLog() {
        println(log)
        log = ""
    }
    override fun typeSequence(seq: String): Int {
        log += seq
        return seq.length
    }
}

class Robot(private val targetKeypad: Keypad, private val controller: Agent) : Agent() {
    private var position = 'A'
    private val cache = mutableMapOf<Pair<Char, Char>, Int>()
    private var log = ""

    override fun pressButton(button: Char): Int = cache.getOrPut(Pair(position, button)) {
        log += button
        val count = targetKeypad.getAllPaths(position, button)
            .map { it + 'A' }
            .minOf { it.sumOf { c -> controller.pressButton(c) } }
        position = button
        return count
    }

    override fun reset() {
        position = 'A'
        log = ""
        controller.reset()
    }

    override fun printLog() {
        controller.printLog()
        println(log)
        log = ""
    }

    override fun typeSequence(seq: String): Int {
        return 0
    }
}

package day21

import common.*
import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun main() {
    val input = parseInput(File("input/21.txt").inputStream())
    val arrowKeypad = Keypad.fromString(" ^A\n<v>")
    println(arrowKeypad.path('A', '>'))
    println(arrowKeypad.path('A', '<'))

    val numericKeypad = Keypad.fromString("789\n456\n123\n 0A")
    println(numericKeypad.path('4', '9'))
    println(numericKeypad.path('A', '8'))
    println(numericKeypad.path('8', 'A'))

    println("numeric + human")
    val robot = Robot(numericKeypad)
    println(robot.pressButton('0'))
    println(robot.pressButton('2'))
    println(robot.pressButton('9'))
    println(robot.pressButton('A'))

    println("arrow + human")
    val robot2 = Robot(arrowKeypad)
    println(robot2.pressButton('<'))
    println(robot2.pressButton('^'))
    println(robot2.pressButton('^'))
    println(robot2.pressButton('^'))
    println(robot2.pressButton('>'))
    println(robot2.pressButton('v'))
    println(robot2.pressButton('v'))
    println(robot2.pressButton('v'))

    println("puzzle")
    part1(input)
}

fun part1(input: List<String>): Int {
    val arrowKeypad = Keypad.fromString(" ^A\n<v>")
    val numericKeypad = Keypad.fromString("789\n456\n123\n 0A")
    val robot = Robot(numericKeypad, Robot(arrowKeypad, Robot(arrowKeypad)))

    robot.pressButton('0')
//    robot.pressButton('2')
//    robot.pressButton('9')
//    robot.pressButton('A')
    robot.printLog()

//    println(robot.pressButton('3'))
//    println(robot.pressButton('7'))
//    println(robot.pressButton('9'))
//    println(robot.pressButton('A'))
    return 0
//    input.forEach { line ->
//        println(line.sumOf { c -> robot.pressButton(c) })
//        robot.reset()
//
//    }
//    return input.sumOf { it.sumOf { c -> robot.pressButton(c) } * it.removeSuffix("A").toInt() }
}

fun parseInput(stream: InputStream): List<String> {
    return stream.bufferedReader().readLines()
}

class Keypad private constructor(private val paths: Map<Pair<Char, Char>, List<Char>>) {
    companion object {
        fun fromString(layout: String): Keypad {
            val positions = layout
                .lineSequence()
                .flatMapIndexed { i, line -> line.mapIndexed { j, char -> char to Vec(i, j) } }
                .toMap()
                .filterKeys { it != ' ' }
            val paths = positions.entries
                .flatMap { (from, fromPos) ->
                    positions.map { (to, toPos) -> Pair(from, to) to toPos - fromPos }
                }
                .toMap()
                .mapValues { distToPath(it.value) }

            return Keypad(paths)
        }

        private fun distToPath(dist: Vec): List<Char> {
            return (0 until abs(dist.i)).map { if (dist.i > 0) 'v' else '^' }.toList() +
                    (0 until abs(dist.j)).map { if (dist.j > 0) '>' else '<' }.toList()
        }
    }
    fun path(from: Char, to: Char): List<Char> {
        return paths[Pair(from, to)] ?: error("keypad: can't go from $from to $to")
    }
}

sealed class Agent {
    abstract fun pressButton(button: Char): Int
    abstract fun reset()
    abstract fun printLog()
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
}

class Robot(private val targetKeypad: Keypad, private val controller: Agent = Human) : Agent() {
    private var position = 'A'
    private val cache = mutableMapOf<Pair<Char, Char>, Int>()
    private var log = ""

    override fun pressButton(button: Char): Int {
        log += button
        val count = targetKeypad.path(position, button).sumOf { controller.pressButton(it) } + controller.pressButton('A')
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

    fun typeSequence(seq: String) {

    }
}

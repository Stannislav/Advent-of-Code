package day21

import java.io.File
import java.io.InputStream

fun main() {
    val input = parseInput(File("input/21.txt").inputStream())
}

fun parseInput(stream: InputStream): List<String> {
    return stream.bufferedReader().readLines()
}

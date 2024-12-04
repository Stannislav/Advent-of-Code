package day04

import java.io.File
import java.io.InputStream

fun main() {
    val input = parseInput(File("input/04.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): Array<CharArray> {
    var result= arrayOf<CharArray>()
    stream.bufferedReader().forEachLine {
        result += it.toCharArray()
    }
    return result
}

fun part1(input: Array<CharArray>): Int {
    var count = 0

    // horizontal
    for (i in input.indices) {
        for (j in 0 until input[i].size - 3) {
            count += isXmas(input[i][j], input[i][j + 1], input[i][j + 2], input[i][j + 3])
        }
    }

    // vertical
    for (i in 0 until input.size - 3) {
        for (j in 0 until input[0].size) {
            count += isXmas(input[i][j], input[i + 1][j], input[i + 2][j], input[i + 3][j])
        }
    }

    // down-right
    for (i in 0 until input.size - 3) {
        for (j in 0 until input[0].size - 3) {
            count += isXmas(input[i][j], input[i + 1][j + 1], input[i + 2][j + 2], input[i + 3][j + 3])
        }
    }

    // up-right
    for (i in input.size-1 downTo 3) {
        for (j in 0 until input[0].size - 3) {
            count += isXmas(input[i][j], input[i - 1][j + 1], input[i - 2][j + 2], input[i - 3][j + 3])
        }
    }

    return count
}

fun part2(input: Array<CharArray>): Int {
    var count = 0
    for (i in 0 until input.size - 2) {
        for (j in 0 until input[0].size - 2) {
            count += isXmas(input, i, j)
        }
    }
    return count
}

fun isXmas(c1: Char, c2: Char, c3: Char, c4: Char): Int {
    return if ((c1 == 'X' && c2 == 'M' && c3 == 'A' && c4 == 'S') || (c1 == 'S' && c2 == 'A' && c3 == 'M' && c4 == 'X')) 1 else 0
}

fun isXmas(input: Array<CharArray>, i: Int, j: Int): Int {
    if (input[i + 1][j + 1] != 'A')
        return 0
    val edges = String(charArrayOf(input[i][j], input[i][j + 2], input[i + 2][j + 2], input[i + 2][j]))
    if (edges == "MMSS" || edges == "SMMS" || edges == "SSMM" || edges == "MSSM")
        return 1
    return 0
}

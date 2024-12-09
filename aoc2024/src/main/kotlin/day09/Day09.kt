package day09

import java.io.File
import java.io.InputStream

fun main() {
    val input = parseInput(File("input/09.txt").inputStream())
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(stream: InputStream): Array<Int> {
    val digits = stream
        .bufferedReader()
        .readText()
        .replace("\n", "")
        .map { it.digitToInt() }

    var result = arrayOf<Int>()
    var isSpace = false
    var currentIndex = 0
    for (digit in digits) {
        result += Array(digit) { if (isSpace) -1 else currentIndex }
        if (!isSpace)
            currentIndex++
        isSpace = !isSpace
    }
    return result
}

fun checksum(arr: Array<Int>): Long = arr.mapIndexed { idx, n -> idx * n.toLong() }.sum()

fun part1(input: Array<Int>): Long {
    val rearranged = Array(input.size) { 0 }
    var i = 0
    var j = input.size - 1
    while (i <= j) {
        rearranged[i] = if (input[i] == -1) input[j--] else input[i]
        while (input[j] == -1) j--
        i++
    }
    return checksum(rearranged)
}

fun part2(input: Array<Int>): Long {
    val rearranged = Array(input.size) { 0 }
    val spaces = Array(input.size) { idx -> input[idx] == -1 }
    var digit = input.max()

    var ptr = input.size - 1
    while (ptr > 0) {
        // Find the last index of the current digit block.
        while (ptr >= 0 && (input[ptr] == -1 || input[ptr] > digit)) ptr--
        // Move past block and determine its length.
        var blockLen = 0
        while (ptr >= 0 && input[ptr] == digit) {
            blockLen++
            ptr--
        }
        // Find the new position of the current block. Can be equal to the origin position.
        val newPos = findNewPos(spaces, ptr + 1, blockLen)
        for (idx in newPos until newPos + blockLen) {
            rearranged[idx] = digit
            spaces[idx] = false
        }
        digit--
    }
    println("${input.sumOf { if (it == -1) 0 else it }} ${rearranged.sum()}")
//    println(rearranged.joinToString(""))

    return checksum(rearranged)
}

fun findNewPos(spaces: Array<Boolean>, originalPos: Int, blockLen: Int): Int {
    var currentPos = 0
    while (currentPos + blockLen <= originalPos) {
        val (spacePos, spaceLen) = nextSpace(spaces, currentPos)
        if (blockLen <= spaceLen)
            return spacePos
        currentPos = spacePos + spaceLen
    }
    return originalPos
}

fun nextSpace(spaces: Array<Boolean>, from: Int): Pair<Int, Int> {
    var start = from
    while (start < spaces.size && !spaces[start])
        start++
    var end = start
    while (end < spaces.size && spaces[end])
        end++
    return Pair(start, end - start)
}

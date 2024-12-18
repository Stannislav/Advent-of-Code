package day17

import java.io.File
import java.io.InputStream

data class Computer(val a: Long, val b: Long, val c: Long, val program: List<Long>) {
    private var ptr = 0
    private var regA = a
    private var regB = b
    private var regC = c
    private val out = mutableListOf<Long>()

    val getA = { regA }
    val getB = { regB }

    fun run(): Computer {
        while (ptr < program.size) { step() }
        return this
    }

    private fun step() {
        val op = program[ptr++]
        val n = program[ptr++]
        when (op) {
            0L -> regA = regA shr combo(n).toInt()
            1L -> regB = regB xor n
            2L -> regB = combo(n) and 0b111
            3L -> if (regA > 0) ptr = combo(n).toInt()
            4L -> regB = regB xor regC
            5L -> out.add(combo(n) and 0b111)
            6L -> regB = regA shr combo(n).toInt()
            7L -> regC = regA shr combo(n).toInt()
            else -> error("Unknown op: $op")
        }
    }

    private fun combo(n: Long) = if (n < 4) n else when(n) {
        4L -> regA
        5L -> regB
        6L -> regC
        else -> error("Unknown operand: $n")
    }

    fun readout(): List<Long> = out.toList()

    /**
     * In the initial value of register A with which the computer output is equal to its program.
     *
     * Manual parsing of the input showed that the program does the following:
     *
     * ```
     * for (A = A_initial; A != 0, A = A >> 3) {
     * 	  y = (A % 8) xor 1
     * 	  out(y xor 4 xor (A >> y) % 8)
     * }
     * ```
     *
     * Once can see that each iteration depends on at most 10 first bits (1 << 10 = 1024) of A:
     * y consumes the first 3 bits of A, its value is at most 7, therefore (A >> y) % 8 doesn't reach
     * beyond the 10th bit.
     *
     * So, an output number depends on the first 10 bits of A, and at each iteration A is shifted by 3 bits.
     * Therefore, subsequent output numbers have an overlap of 7 bits which they depend on:
     *
     * ```
     * ##########
     *    ##########
     *       ##########
     *          ##########
     *             ...
     * ```
     *
     * So, what we need to do is for each desired output number to find the input 10-bit number which
     * is consistent in the overlap with the rest of the sequence.
     *
     * For each of the 8 possible output numbers we find all 10-bit values of A which lead to this
     * output. Starting from the end of the desired sequence we recursively filter out those 10-bit
     * values, which have a compatible overlap.
     *
     * At the end we need to overlap all 10-bit values into a single bit sequence and convert it
     * to a decimal number.
     */
    fun findReplicatingA(): Long {
        // For each of the possible outputs 0-7 collect the values of A which give this output.
        val outputToA = mutableMapOf<Long, List<Long>>().withDefault { listOf() }
        (0L until 1024L).forEach {
            val x = this.copy(a = it).run().readout().first()
            outputToA[x] = outputToA.getValue(x) + it
        }

        /**
         * Starting from the tail of the desired sequence, recursively find all 10-bit parts.
         *
         * @param ptr the current position in the desired output sequence.
         * @param next the sequence of parts already generated.
         */
        fun findParts(ptr: Int = program.size - 1, next: List<Long> = listOf()): List<Long>? {
            if (ptr == -1) return next

            val candidates = outputToA.getValue(program[ptr])
            for (candidate in candidates) {
                if (next.size >= 1 && (candidate shr 3 != next[0] and 0b1111111))
                    continue
                if (next.size > 2 && (candidate shr 6 != next[1] and 0b1111))
                    continue
                if (next.size > 3 && (candidate shr 9 != next[2] and 0b1))
                    continue
                findParts(ptr - 1, listOf(candidate) + next)?.let { return it }
            }
            return null
        }

        val parts = findParts() ?: error("No solution found")

        // Overlap all 10-bit numbers into one.
        var a = 0L
        for ((i, num) in parts.withIndex())
            a = a or (num shl (3 * i))
        return a
    }
}

fun main() {
    val computer = parseInput(File("input/17.txt").inputStream())
    println("Part 1: ${part1(computer)}")
    println("Part 2: ${part2(computer)}")
}

fun parseInput(stream: InputStream): Computer {
    val pattern = """
        Register A: (\d+)
        Register B: (\d+)
        Register C: (\d+)

        Program: ([\d,]+)
    """.trimIndent().toRegex()
    val values = pattern.matchEntire(stream.bufferedReader().readText().trim())?.groupValues ?: error("Can't match input")
    return Computer(values[1].toLong(), values[2].toLong(), values[3].toLong(), values[4].split(",").map { it.toLong() })
}

fun part1(computer: Computer): String {
    return computer.run().readout().joinToString(",")
}

fun part2(computer: Computer): Long {
    return computer.findReplicatingA()
}

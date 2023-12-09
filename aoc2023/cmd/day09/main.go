package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/09.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	var seqs [][]int
	for _, line := range strings.Split(input, "\n") {
		var seq []int
		for _, xStr := range strings.Split(line, " ") {
			x, _ := strconv.Atoi(xStr)
			seq = append(seq, x)
		}
		seqs = append(seqs, seq)
	}

	// Solve
	part1 := 0
	part2 := 0
	for _, seq := range seqs {
		prev, next := extend(seq)
		part1 += next
		part2 += prev
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}

func extend(seq []int) (prev, next int) {
	if allZero(seq) {
		return 0, 0
	} else {
		var diffs []int
		for i := 0; i < len(seq)-1; i++ {
			diffs = append(diffs, seq[i+1]-seq[i])
		}
		dPrev, dNext := extend(diffs)
		return seq[0] - dPrev, seq[len(seq)-1] + dNext
	}
}

func allZero(seq []int) bool {
	for _, x := range seq {
		if x != 0 {
			return false
		}
	}
	return true
}

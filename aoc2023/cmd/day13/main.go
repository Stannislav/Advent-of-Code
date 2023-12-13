package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/13.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse Input
	var blocks [][]string
	for _, block := range strings.Split(input, "\n\n") {
		blocks = append(blocks, strings.Split(block, "\n"))
	}

	// Part 1
	part1 := 0
	part2 := 0
	for _, block := range blocks {
		part1 += colSymmetry(block, 0) + 100*rowSymmetry(block, 0)
		part2 += colSymmetry(block, 1) + 100*rowSymmetry(block, 1)
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 1:", part2)
}

func colSymmetry(block []string, wantSmudges int) int {

	checkCol := func(col int) bool {
		// col = idx to the row right of the symmetry line
		smudges := 0
		for row := 0; row < len(block); row++ {
			maxDCol := min(col, len(block[0])-col)
			for dCol := 0; dCol < maxDCol; dCol++ {
				if block[row][col+dCol] != block[row][col-1-dCol] {
					smudges += 1
				}
				if smudges > wantSmudges {
					return false
				}
			}
		}
		return smudges == wantSmudges
	}

	for col := 1; col < len(block[0]); col++ {
		if checkCol(col) {
			return col
		}
	}
	return 0
}

func rowSymmetry(block []string, wantSmudges int) int {

	checkRow := func(row int) bool {
		// row = idx to the row below the symmetry line
		smudges := 0
		for col := 0; col < len(block[0]); col++ {
			maxDRow := min(row, len(block)-row)
			for dRow := 0; dRow < maxDRow; dRow++ {
				if block[row+dRow][col] != block[row-1-dRow][col] {
					smudges += 1
				}
				if smudges > wantSmudges {
					return false
				}
			}
		}
		return smudges == wantSmudges
	}

	for row := 1; row < len(block); row++ {
		if checkRow(row) {
			return row
		}
	}

	return 0
}

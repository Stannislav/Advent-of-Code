package main

import (
	"fmt"
	"image"
	"os"
	"regexp"
	"strconv"
	"strings"
	"unicode"
)

// Credit: https://github.com/mnml/aoc/blob/main/2023/03/1.go
func main() {
	input, _ := os.ReadFile("input/03.txt")
	lines := strings.Fields(string(input))

	// Map of points to part symbols
	parts := make(map[image.Point]rune)
	for x, line := range lines {
		for y, char := range line {
			if char != '.' && !unicode.IsDigit(char) {
				parts[image.Point{X: x, Y: y}] = char
			}
		}
	}

	reNumber := regexp.MustCompile(`\d+`)
	partNumbers := make(map[image.Point][]int)
	part1 := 0
	for x, line := range lines {
		for _, bounds := range reNumber.FindAllStringIndex(line, -1) {
			// Instead of finding neighbours of parts, find neighbours of part numbers!
			// This makes part number assignment easier since parts are always single characters.
			neighbours := []image.Point{{X: x, Y: bounds[0] - 1}, {X: x, Y: bounds[1]}}
			for y := bounds[0] - 1; y <= bounds[1]; y++ {
				neighbours = append(neighbours, image.Point{X: x - 1, Y: y}, image.Point{X: x + 1, Y: y})
			}

			// If a neighbour is a part, add the part number to the part.
			partNumber, _ := strconv.Atoi(line[bounds[0]:bounds[1]])
			for _, point := range neighbours {
				if _, ok := parts[point]; ok {
					partNumbers[point] = append(partNumbers[point], partNumber)
					part1 += partNumber
				}
			}
		}
	}

	part2 := 0
	for point, part := range parts {
		if part == '*' && len(partNumbers[point]) == 2 {
			part2 += partNumbers[point][0] * partNumbers[point][1]
		}
	}

	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"slices"
	"strconv"
	"strings"
)

type Point struct {
	i, j int
}

type Part struct {
	symbol      rune
	coord       Point
	partNumbers []int
}

func main() {
	input, _ := os.ReadFile("input/03.txt")

	var engineMap []string
	for _, line := range strings.Fields(string(input)) {
		engineMap = append(engineMap, line)
	}
	bounds := Point{len(engineMap), len(engineMap[0])}

	// Find all numbers
	reNum := regexp.MustCompile(`\d+`)
	var numbers []int
	coordToNumberPtr := make(map[Point]int)
	nNumbers := 0
	for i, line := range engineMap {
		for _, pt := range reNum.FindAllStringIndex(line, -1) {
			number, _ := strconv.Atoi(line[pt[0]:pt[1]])
			numbers = append(numbers, number)
			for j := pt[0]; j < pt[1]; j++ {
				coordToNumberPtr[Point{i, j}] = nNumbers
			}
			nNumbers += 1
		}
	}

	// Find all parts
	notPart := []rune(".0123456789")
	var parts []Part
	for i, line := range engineMap {
		for j, char := range line {
			if !slices.Contains(notPart, char) {
				part := Part{char, Point{i, j}, nil}
				var seen []int // [numPtr]
				for _, neighbour := range neighbourCoords(part.coord, bounds) {
					numPtr, ok := coordToNumberPtr[neighbour]
					if ok && !slices.Contains(seen, numPtr) {
						part.partNumbers = append(part.partNumbers, numbers[numPtr])
						seen = append(seen, numPtr)
					}
				}
				parts = append(parts, part)
			}
		}
	}

	partNumberSum := 0
	gearRatioSum := 0
	for _, part := range parts {
		// Part 1
		for _, partNumber := range part.partNumbers {
			partNumberSum += partNumber
		}
		// Part 2
		if part.symbol == '*' && len(part.partNumbers) == 2 {
			gearRatioSum += part.partNumbers[0] * part.partNumbers[1]
		}
	}
	fmt.Println("Part 1:", partNumberSum)
	fmt.Println("Part 2:", gearRatioSum)
}

func neighbourCoords(pt Point, bounds Point) (neighbours []Point) {
	for di := -1; di <= 1; di++ {
		for dj := -1; dj <= 1; dj++ {
			if pt.i+di >= 0 && pt.i+di < bounds.i && pt.j+dj >= 0 && pt.j+dj < bounds.j {
				neighbours = append(neighbours, Point{pt.i + di, pt.j + dj})
			}
		}
	}
	return
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"slices"
	"strconv"
	"strings"
)

type Engine struct {
	schematic  []string
	maxI, maxJ int
}

type Point struct {
	i, j int
}

type Part struct {
	symbol      rune
	coord       Point
	partNumbers []int
}

func main() {
	engine := parseFile("input/03.txt")

	// Find all numbers
	var numbers []int
	pointToNumberIdx := make(map[Point]int) // Given a point, which number is it? Numbers are not unique.
	reNumber := regexp.MustCompile(`\d+`)
	currentIdx := 0
	for i, line := range engine.schematic {
		for _, bounds := range reNumber.FindAllStringIndex(line, -1) {
			number, _ := strconv.Atoi(line[bounds[0]:bounds[1]])
			numbers = append(numbers, number)
			for j := bounds[0]; j < bounds[1]; j++ {
				pointToNumberIdx[Point{i, j}] = currentIdx
			}
			currentIdx++
		}
	}

	// Find all parts
	notPart := []rune(".0123456789")
	var parts []Part
	for i, line := range engine.schematic {
		for j, char := range line {
			if !slices.Contains(notPart, char) {
				parts = append(parts, Part{char, Point{i, j}, nil})
			}
		}
	}

	// Attach part numbers to parts
	for i := range parts {
		var seen []int // list of part number indices
		for point := range engine.neighbours(parts[i].coord) {
			numberIdx, ok := pointToNumberIdx[point]
			if ok && !slices.Contains(seen, numberIdx) {
				parts[i].partNumbers = append(parts[i].partNumbers, numbers[numberIdx])
				seen = append(seen, numberIdx)
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

func parseFile(fileName string) (engine Engine) {
	content, _ := os.ReadFile(fileName)
	for _, line := range strings.Fields(string(content)) {
		engine.schematic = append(engine.schematic, line)
	}
	engine.maxI = len(engine.schematic)
	engine.maxJ = len(engine.schematic[0])
	return
}

func (engine Engine) neighbours(point Point) chan Point {
	neighbours := make(chan Point)

	go func() {
		for di := -1; di <= 1; di++ {
			for dj := -1; dj <= 1; dj++ {
				neighbour := Point{point.i + di, point.j + dj}
				if engine.inBounds(neighbour) && neighbour != point {
					neighbours <- neighbour
				}
			}
		}
		close(neighbours)
	}()

	return neighbours
}

func (engine Engine) inBounds(pt Point) bool {
	return pt.i >= 0 && pt.i < engine.maxI && pt.j >= 0 && pt.j < engine.maxJ
}

package main

import (
	"fmt"
	"image"
	"os"
	"strings"
)

type Map struct {
	Grid  map[image.Point]rune
	Start image.Point
	End   image.Point
	Dim   image.Rectangle
}

var slopeDirections = map[rune]image.Point{'^': image.Pt(-1, 0), '>': image.Pt(0, 1), 'v': image.Pt(1, 0), '<': image.Pt(0, -1)}
var deltas = []image.Point{image.Pt(-1, 0), image.Pt(0, 1), image.Pt(1, 0), image.Pt(0, -1)}

func main() {
	m := ParseInput("input/23.txt")

	fmt.Printf("Part 1: %d\n", Part1(m))
	fmt.Printf("Part 2: %d\n", 0)
}

func ParseInput(filename string) *Map {
	inputBytes, err := os.ReadFile(filename)
	if err != nil {
		panic(fmt.Sprintf("file %s not found", filename))
	}

	grid := make(map[image.Point]rune)
	for row, line := range strings.Fields(string(inputBytes)) {
		for col, r := range line {
			grid[image.Point{row, col}] = r
		}
	}

	dim := image.Rectangle{}
	for pt := range grid {
		dim = dim.Union(image.Rectangle{image.Pt(0, 0), pt})
	}

	return &Map{grid, dim.Min.Add(image.Pt(0, 1)), dim.Max.Sub(image.Pt(0, 1)), dim}
}

func Part1(m *Map) int {
	return m.LongestPath(map[image.Point]int{m.Start: 0}, m.Start)
}

func (m *Map) LongestPath(currentPath map[image.Point]int, start image.Point) int {
	if start.Eq(m.End) {
		return currentPath[start]
	}

	distances := []int{}
	for _, next := range m.next(start) {
		if _, seen := currentPath[next]; seen {
			continue
		}
		currentPath[next] = currentPath[start] + 1
		distances = append(distances, m.LongestPath(currentPath, next))
		delete(currentPath, next)
	}

	result := 0
	for _, d := range distances {
		if d > result {
			result = d
		}
	}

	return result
}

func (m *Map) next(pt image.Point) []image.Point {
	if delta, ok := slopeDirections[m.Grid[pt]]; ok {
		return []image.Point{pt.Add(delta)}
	}

	result := []image.Point{}
	for _, delta := range deltas {
		next := pt.Add(delta)
		if r, ok := m.Grid[next]; ok && r != '#' {
			result = append(result, next)
		}
	}
	return result
}

package main

import (
	"fmt"
	"image"
	"os"
	"strings"
)

func main() {
	fmt.Printf("Part 1: %d\n", 0)
	fmt.Printf("Part 2: %d\n", 0)
}

func ParseInput(filename string) map[image.Point]rune {
	inputBytes, _ := os.ReadFile(filename)

	m := make(map[image.Point]rune)
	for row, line := range strings.Fields(string(inputBytes)) {
		for col, r := range line {
			m[image.Point{row, col}] = r
		}
	}

	return m
}

package main

import (
	"fmt"
	"image"
	"os"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/11.txt")
	input := string(inputBytes)

	// Parse input
	var sky [][]rune
	for _, line := range strings.Fields(input) {
		sky = append(sky, []rune(line))
	}

	// Find emtpy rows
	var emptyRows []int
	for i, row := range sky {
		empty := true
		for _, char := range row {
			if char == '#' {
				empty = false
				break
			}
		}
		if empty {
			emptyRows = append(emptyRows, i)
		}
	}

	// Find empty columns
	var emptyCols []int
	for j := 0; j < len(sky[0]); j++ {
		empty := true
		for _, row := range sky {
			if row[j] == '#' {
				empty = false
				break
			}
		}
		if empty {
			emptyCols = append(emptyCols, j)
		}
	}

	// Find all galaxies
	var galaxies []image.Point
	for x, row := range sky {
		for y, char := range row {
			if char == '#' {
				galaxies = append(galaxies, image.Pt(x, y))
			}
		}
	}

	// Form pairs of galaxies
	var pairs []image.Rectangle
	for i := 0; i < len(galaxies); i++ {
		for j := i + 1; j < len(galaxies); j++ {
			pairs = append(pairs, image.Rectangle{Min: galaxies[i], Max: galaxies[j]}.Canon())
		}
	}

	solve := func(expansionFactor int) (total int) {
		for _, pair := range pairs {
			total += pair.Dx() + pair.Dy()
			for _, row := range emptyRows {
				if pair.Min.X < row && row < pair.Max.X {
					total += expansionFactor - 1
				}
			}
			for _, col := range emptyCols {
				if pair.Min.Y < col && col < pair.Max.Y {
					total += expansionFactor - 1
				}
			}
		}
		return
	}

	fmt.Println("Part 1:", solve(2))
	fmt.Println("Part 1:", solve(1_000_000))
}

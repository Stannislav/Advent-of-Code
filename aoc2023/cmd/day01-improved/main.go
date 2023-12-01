package main

import (
	"fmt"
	"os"
	"regexp"
	"slices"
	"strings"
)

// Credit: https://github.com/mnml/aoc/blob/main/2023/01/1.go
func main() {
	input, _ := os.ReadFile("input/01.txt")
	// Nice find: use `strings.Fields` instead of `strings.Split` to split lines.
	// All whitespaces are automatically trimmed.
	lines := strings.Fields(string(input))

	// Typical idea: use an array instead of a map and use element indices to
	// calculate values (see `index % 9 + 1` in `solve`).
	digits1 := []string{"1", "2", "3", "4", "5", "6", "7", "8", "9"}
	digits2 := []string{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}
	fmt.Println("Part 1:", solve(lines, digits1))
	fmt.Println("Part 2:", solve(lines, append(digits1, digits2...)))
}

func solve(lines []string, digits []string) (total int) {
	total = 0
	first := regexp.MustCompile("(" + strings.Join(digits, "|") + ")")
	last := regexp.MustCompile(".*" + first.String())

	for _, line := range lines {
		firstDigit := slices.Index(digits, first.FindStringSubmatch(line)[1])
		lastDigit := slices.Index(digits, last.FindStringSubmatch(line)[1])
		total += 10*(firstDigit%9+1) + (lastDigit%9 + 1)
	}
	return
}

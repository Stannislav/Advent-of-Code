package main

import (
	"fmt"
	"maps"
	"os"
	"strings"
)

func main() {
	data, _ := os.ReadFile("input/01.txt")
	lines := strings.Split(strings.Trim(string(data), "\n"), "\n")

	// Part 1
	wordToDigit := map[string]int{
		"1": 1,
		"2": 2,
		"3": 3,
		"4": 4,
		"5": 5,
		"6": 6,
		"7": 7,
		"8": 8,
		"9": 9,
	}
	fmt.Println("Part 1:", calibrationSum(lines, wordToDigit))

	// Part 2
	maps.Copy(wordToDigit, map[string]int{
		"one":   1,
		"two":   2,
		"three": 3,
		"four":  4,
		"five":  5,
		"six":   6,
		"seven": 7,
		"eight": 8,
		"nine":  9,
	})
	fmt.Println("Part 2:", calibrationSum(lines, wordToDigit))
}

func calibrationSum(lines []string, wordToDigit map[string]int) (total int) {
	total = 0
	for _, line := range lines {
		first := findFirst(line, wordToDigit)
		last := findLast(line, wordToDigit)
		total += first*10 + last
	}
	return
}

func findFirst(line string, wordToDigit map[string]int) (result int) {
	result = -1
	lowestIdx := len(line)
	for word := range wordToDigit {
		idx := strings.Index(line, word)
		if idx >= 0 && idx < lowestIdx {
			lowestIdx = idx
			result = wordToDigit[word]
		}
	}
	return
}

func findLast(line string, wordToDigit map[string]int) (result int) {
	result = -1
	highestIndex := -1
	for word := range wordToDigit {
		idx := strings.LastIndex(line, word)
		if idx >= 0 && idx > highestIndex {
			highestIndex = idx
			result = wordToDigit[word]
		}
	}
	return
}

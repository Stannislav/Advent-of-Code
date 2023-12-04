package main

import (
	"fmt"
	"os"
	"regexp"
	"slices"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("input/04.txt")
	lines := strings.Split(strings.Trim(string(input), "\n"), "\n")

	reLine := regexp.MustCompile(`Card\s+\d+: (.+) \| (.+)`)
	reNumber := regexp.MustCompile(`\d+`)

	nCards := make([]int, len(lines))
	for i := range nCards {
		nCards[i] = 1
	}

	part1 := 0
	part2 := 0
	for i, line := range lines {
		match := reLine.FindStringSubmatch(line)
		var have []int
		var winning []int
		for _, xStr := range reNumber.FindAllString(match[1], -1) {
			x, _ := strconv.Atoi(xStr)
			have = append(have, x)
		}
		for _, xStr := range reNumber.FindAllString(match[2], -1) {
			x, _ := strconv.Atoi(xStr)
			winning = append(winning, x)
		}

		common := nCommon(have, winning)
		// Part 1
		if common > 0 {
			part1 += 1 << (common - 1)
		}
		// Part 2
		for j := i + 1; j < common+i+1 && j < len(nCards); j++ {
			nCards[j] += nCards[i]
		}
		part2 += nCards[i]
	}

	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)

}

func nCommon(set1 []int, set2 []int) int {
	var intersection []int
	for _, x := range set1 {
		if slices.Contains(set2, x) {
			intersection = append(intersection, x)
		}
	}
	return len(intersection)
}

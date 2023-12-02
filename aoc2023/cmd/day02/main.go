package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("input/02.txt")
	lines := strings.Split(strings.Trim(string(input), "\n"), "\n")

	reGame := regexp.MustCompile(`Game \d+: (.*)`)
	maxCubes := []int{12, 13, 14}
	idSum := 0
	totalPower := 0
	for id, line := range lines {
		isPossible := 1
		minCubes := []int{0, 0, 0}
		game := reGame.FindStringSubmatch(line)[1]
		for _, hand := range strings.Split(game, "; ") {
			cubesShown := parseHand(hand)
			for i, nCubes := range cubesShown {
				// for part 1
				if nCubes > maxCubes[i] {
					isPossible = 0
				}
				// for part 2
				minCubes[i] = max(minCubes[i], nCubes)
			}
		}
		totalPower += minCubes[0] * minCubes[1] * minCubes[2]
		idSum += (id + 1) * isPossible
	}
	fmt.Println("Part 1:", idSum)
	fmt.Println("Part 2:", totalPower)
}

// parseHand parse a hand of the form `"3 red, 5 green, 1 blue"` and
// extract the cube count as an array `(3, 5, 1)`.
func parseHand(hand string) (rbg []int) {
	rbg = []int{0, 0, 0}
	for i, color := range []string{"red", "green", "blue"} {
		match := regexp.MustCompile(`(\d+) ` + color).FindStringSubmatch(hand)
		if match != nil {
			rbg[i], _ = strconv.Atoi(match[1])
		}
	}
	return
}

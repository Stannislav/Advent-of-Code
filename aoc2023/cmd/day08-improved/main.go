package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

// Credit:
// * https://github.com/mnml/aoc/blob/main/2023/08/1.go
// * https://github.com/akashdeepnandi/advent-of-code/blob/main/day8/main.go
func main() {
	inputBytes, _ := os.ReadFile("input/08.txt")
	input := string(inputBytes)

	// Parse input
	nav := []rune(input[:strings.Index(input, "\n")])
	net := make(map[string]map[rune]string) // <node> -> <direction> -> <node>
	re := regexp.MustCompile(`(.*) = \((.*), (.*)\)`)
	for _, match := range re.FindAllStringSubmatch(input, -1) {
		net[match[1]] = map[rune]string{'L': match[2], 'R': match[3]}
	}

	// Define as closure to access net and nav.
	// Each cycle only has one valid end node, so we can just walk until we find it.
	walk := func(start string) (steps int) {
		for node := start; node[2] != 'Z'; steps++ {
			node = net[node][nav[steps%len(nav)]]
		}
		return
	}

	fmt.Println("Part 1:", walk("AAA"))
	part2 := 1
	for node, _ := range net {
		if node[2] == 'A' {
			part2 = lcm(part2, walk(node))
		}
	}
	fmt.Println("Part 2:", part2)
}

func lcm(a, b int) int {
	gcd, r := a, b
	for r != 0 {
		gcd, r = r, gcd%r
	}

	return a * b / gcd
}

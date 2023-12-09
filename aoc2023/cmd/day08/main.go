package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/08.txt")

	// Parse input
	parts := strings.Split(strings.TrimSpace(string(inputBytes)), "\n\n")
	dirs := []rune(parts[0])
	nodes := make(map[string]Node)
	re := regexp.MustCompile(`(\w\w\w) = \((\w\w\w), (\w\w\w)\)`)
	for _, line := range strings.Split(parts[1], "\n") {
		match := re.FindStringSubmatch(line)
		nodes[match[1]] = Node{match[2], match[3]}
	}

	// Part 1
	fmt.Println("Part 1:", travel(nodes, dirs, "AAA"))

	// Part 2
	var steps []int
	for name, _ := range nodes {
		if name[2] == 'A' {
			steps = append(steps, travel(nodes, dirs, name))
		}
	}
	fmt.Println("Part 2:", LCM(steps[0], steps[1], steps[2:]...))
}

type Node struct {
	left  string
	right string
}

func (n Node) next(dir rune) string {
	if dir == 'L' {
		return n.left
	} else {
		return n.right
	}
}

func travel(nodes map[string]Node, dirs []rune, start string) (steps int) {
	for name := start; name[2] != 'Z'; steps++ {
		name = nodes[name].next(dirs[steps%len(dirs)])
	}
	return
}

func GCD(a, b int) int {
	for b != 0 {
		t := b
		b = a % b
		a = t
	}
	return a
}

// LCM find the Least Common Multiple (LCM) via GCD
// Credit: https://siongui.github.io/2017/06/03/go-find-lcm-by-gcd/
func LCM(a, b int, integers ...int) int {
	result := a * b / GCD(a, b)

	for i := 0; i < len(integers); i++ {
		result = LCM(result, integers[i])
	}

	return result
}

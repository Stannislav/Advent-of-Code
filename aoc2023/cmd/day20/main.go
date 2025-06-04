package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

func main() {
	modules, connections := parseInput("input/20.txt")
	println(len(modules), "modules found")
	println(len(connections), "connections found")
	fmt.Printf("Modules:\n%v\n", modules)
	fmt.Printf("Connections:\n%v\n", connections)
}

func parseInput(filename string) (map[string]Module, map[string][]string) {
	inputBytes, err := os.ReadFile(filename)
	if err != nil {
		panic(err)
	}
	input := string(inputBytes)
	lines := strings.Split(strings.TrimSpace(input), "\n")
	reLine := regexp.MustCompile(`(.*) -> (.*)`)
	modules := make(map[string]Module)
	connections := make(map[string][]string)

	for _, line := range lines {
		match := reLine.FindStringSubmatch(line)
		label := match[1]
		var name string
		var module Module
		targets := strings.Split(match[2], ", ")
		switch label[0] {
		case '%':
			name = label[1:]
			module = &FlipFlop{}
		case '&':
			name = label[1:]
			module = NewConjunction(targets)
		case 'b':
			name = label
			module = &Broadcast{}
		default:
			panic("Unknown module type: " + label)
		}
		modules[name] = module
		connections[name] = targets
	}

	return modules, connections
}

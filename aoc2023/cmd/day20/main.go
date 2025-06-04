package main

import (
	"container/list"
	"fmt"
	"os"
	"regexp"
	"slices"
	"strings"
)

func main() {
	modules, connections := parseInput("input/20.txt")
	part1Result := part1(modules, connections)
	fmt.Printf("Part 1: %d\n", part1Result)

	modules, connections = parseInput("input/20.txt")
	part2Result := part2(modules, connections)
	fmt.Printf("Part 2: %d\n", part2Result)
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
			module = NewConjunction()
		case 'b':
			name = label
			module = &Broadcast{}
		default:
			panic("Unknown module type: " + label)
		}
		modules[name] = module
		connections[name] = targets

	}
	// Set inputs for all conjunction modules
	conjunctions := []string{}
	for name, mod := range modules {
		if _, ok := mod.(*Conjunction); ok {
			conjunctions = append(conjunctions, name)
		}
	}
	for origin, targets := range connections {
		for _, target := range targets {
			if _, ok := modules[target]; ok {
				if slices.Contains(conjunctions, target) {
					modules[target].(*Conjunction).LastInputs[origin] = false
				}
			}
		}
	}

	return modules, connections
}

type Signal struct {
	from  string
	value bool
	to    string
}

func (s Signal) String() string {
	var value string
	if s.value {
		value = "-high->"
	} else {
		value = "-low->"
	}
	return fmt.Sprintf("%s %s %s", s.from, value, s.to)
}

func part1(modules map[string]Module, connections map[string][]string) int {
	counts := map[bool]int{true: 0, false: 0}
	for i := 0; i < 1000; i++ {
		signals := pressButton(modules, connections)
		for _, signal := range signals {
			counts[signal.value]++
		}
	}

	return counts[true] * counts[false]
}

func pressButton(modules map[string]Module, connections map[string][]string) []Signal {
	signals := []Signal{}

	q := list.New()
	q.PushBack(Signal{"button", false, "broadcaster"})
	for q.Len() > 0 {
		item := q.Remove(q.Front())
		inSignal := item.(Signal)
		// fmt.Println(inSignal)
		signals = append(signals, inSignal)
		module := modules[inSignal.to]
		if module != nil {
			signal, send := module.Receive(inSignal.from, inSignal.value)
			if send {
				for _, target := range connections[inSignal.to] {
					q.PushBack(Signal{inSignal.to, signal, target})
				}
			}
		}
	}

	return signals
}

func part2(modules map[string]Module, connections map[string][]string) int {
	return 0
}

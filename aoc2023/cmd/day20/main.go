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
	part1Result := Part1(modules, connections)
	fmt.Printf("Part 1: %d\n", part1Result)

	modules, connections = parseInput("input/20.txt")
	part2Result := Part2(modules, connections)
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

func Part1(modules map[string]Module, connections map[string][]string) int {
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

// Part2 calculates the number of button presses needed for the rx module to recieve a low signal.
func Part2(modules map[string]Module, connections map[string][]string) int {
	rxInputs := findInputsOf("rx", connections)
	if len(rxInputs) != 1 {
		panic("Expected exactly one input to rx, found: " + strings.Join(rxInputs, ", "))
	}
	ensureModuleIsConjunction(rxInputs[0], modules)

	parents := findInputsOf(rxInputs[0], connections)
	for _, input := range parents {
		ensureModuleIsConjunction(input, modules)
	}
	grandparents := []string{}
	for _, mod := range parents {
		parents := findInputsOf(mod, connections)
		if len(parents) != 1 {
			panic("Expected exactly one input to " + mod + ", found: " + strings.Join(parents, ", "))
		}
		ensureModuleIsConjunction(parents[0], modules)
		grandparents = append(grandparents, parents[0])
	}

	modToCycle := make(map[string]int)
	for _, mod := range grandparents {
		modToCycle[mod] = -1 // -1 means not yet set
	}

	buttonPressCounts := 0
	for done := false; !done; {
		signals := pressButton(modules, connections)
		buttonPressCounts++

		// Check if any of the cycles have been found.
		for _, signal := range signals {
			for mod := range modToCycle {
				if signal.from == mod && !signal.value {
					modToCycle[mod] = buttonPressCounts
				}
			}
		}

		// Have all cycles been found?
		done = true
		for _, cycle := range modToCycle {
			if cycle == -1 {
				done = false
				break
			}
		}
	}

	cycles := []int{}
	for _, cycle := range modToCycle {
		cycles = append(cycles, cycle)
	}
	return LCM(cycles[0], cycles[1], cycles[2:]...)
}

func findInputsOf(moduleName string, connections map[string][]string) []string {
	inputs := []string{}
	for from, to := range connections {
		if slices.Contains(to, moduleName) {
			inputs = append(inputs, from)
		}
	}
	return inputs
}

func ensureModuleIsConjunction(moduleName string, modules map[string]Module) {
	if _, ok := modules[moduleName].(*Conjunction); !ok {
		panic("Expected " + moduleName + " to be a Conjunction module")
	}
}

// greatest common divisor (GCD) via Euclidean algorithm
func GCD(a, b int) int {
	for b != 0 {
		t := b
		b = a % b
		a = t
	}
	return a
}

// find Least Common Multiple (LCM) via GCD
func LCM(a, b int, integers ...int) int {
	result := a * b / GCD(a, b)

	for i := 0; i < len(integers); i++ {
		result = LCM(result, integers[i])
	}

	return result
}

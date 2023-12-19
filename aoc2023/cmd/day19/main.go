package main

import (
	"fmt"
	"maps"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type Rule struct {
	cat, op rune
	x       int
	target  string
}

type Flow []Rule

type Part map[rune]int

type PartInterval struct {
	lo, hi Part
}

func main() {
	inputBytes, _ := os.ReadFile("input/19.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	blocks := strings.Split(input, "\n\n")
	flows := make(map[string]Flow)
	var parts []Part
	reFlow := regexp.MustCompile(`(\w+)\{(.*)}`)
	rePart := regexp.MustCompile(`([xmas])=(\d+)`)
	reRule := regexp.MustCompile(`([xmas])([<>])(\d+):(\w+)`)
	for _, line := range strings.Split(blocks[0], "\n") {
		match := reFlow.FindStringSubmatch(line)
		key := match[1]
		var value []Rule
		rules := strings.Split(match[2], ",")
		for i := 0; i < len(rules)-1; i += 1 {
			m := reRule.FindStringSubmatch(rules[i])
			x, _ := strconv.Atoi(m[3])
			value = append(value, Rule{rune(m[1][0]), rune(m[2][0]), x, m[4]})
		}
		value = append(value, Rule{'-', '-', 0, rules[len(rules)-1]})
		flows[key] = value
	}
	for _, line := range strings.Split(blocks[1], "\n") {
		part := make(Part)
		for _, match := range rePart.FindAllStringSubmatch(line, -1) {
			key := rune(match[1][0])
			value, _ := strconv.Atoi(match[2])
			part[key] = value
		}
		parts = append(parts, part)
	}

	// Part 1
	checkAccept := func(p Part) bool {
		name := "in"
		for name != "R" && name != "A" {
			name = flows[name].process(p)
		}
		return name == "A"
	}

	var accepted []Part
	for _, part := range parts {
		if checkAccept(part) {
			accepted = append(accepted, part)
		}
	}
	part1 := 0
	for _, p := range accepted {
		part1 += p.getRating()
	}
	fmt.Println("Part 1:", part1)

	// Part 2
	start := PartInterval{
		Part{'x': 1, 'm': 1, 'a': 1, 's': 1},
		Part{'x': 4001, 'm': 4001, 'a': 4001, 's': 4001},
	}
	part2 := 0
	for _, pi := range findAcceptedIntervals(start, "in", flows) {
		// All intervals are disjoint by construction, so we can just sum them
		part2 += pi.getSize()
	}
	fmt.Println("Part 2:", part2)
}

func findAcceptedIntervals(pi PartInterval, flowName string, flows map[string]Flow) (accepted []PartInterval) {
	for _, rule := range flows[flowName] {
		pass, fail := pi.splitBy(rule)

		// If condition is satisfied, we branch out to the flow that the rule points to.
		if rule.target == "A" {
			accepted = append(accepted, pass)
		} else if rule.target != "R" {
			accepted = append(accepted, findAcceptedIntervals(pass, rule.target, flows)...)
		}

		// If condition isn't satisfied, we keep checking the rest of the rules.
		pi = fail
	}

	return accepted
}

func (f Flow) process(p Part) string {
	for _, rule := range f {
		if p.satisfies(rule) {
			return rule.target
		}
	}
	panic("No matching rule")
}

func (p Part) satisfies(r Rule) bool {
	if r.op == '-' {
		return true
	} else {
		switch r.op {
		case '<':
			return p[r.cat] < r.x
		case '>':
			return p[r.cat] > r.x
		}
		panic("Unknown operator: " + string(r.op))
	}
}

func (p Part) getRating() (rating int) {
	for _, value := range p {
		rating += value
	}
	return
}

func (pi PartInterval) splitBy(r Rule) (pass, fail PartInterval) {
	if r.op == '-' {
		return pi.clone(), PartInterval{make(Part), make(Part)}
	}
	if pi.contradicts(r) {
		return PartInterval{make(Part), make(Part)}, pi.clone()
	}
	pass = pi.clone()
	fail = pi.clone()
	switch r.op {
	case '<':
		pass.hi[r.cat] = r.x
		fail.lo[r.cat] = r.x
	case '>':
		pass.lo[r.cat] = r.x + 1
		fail.hi[r.cat] = r.x + 1
	}
	return
}

func (pi PartInterval) contradicts(r Rule) bool {
	if r.op == '<' && pi.lo[r.cat] >= r.x {
		return true
	}
	if r.op == '>' && pi.hi[r.cat] <= r.x+1 {
		return true
	}

	return false
}

func (pi PartInterval) getSize() int {
	total := 1
	for _, key := range "xmas" {
		total *= pi.hi[key] - pi.lo[key]
	}
	return total
}

func (pi PartInterval) clone() PartInterval {
	return PartInterval{maps.Clone(pi.lo), maps.Clone(pi.hi)}
}

package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/12.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	var records [][]rune
	var groups [][]int
	for _, line := range strings.Split(input, "\n") {
		parts := strings.Split(line, " ")
		records = append(records, []rune(parts[0]))
		var group []int
		for _, xStr := range strings.Split(parts[1], ",") {
			x, _ := strconv.Atoi(xStr)
			group = append(group, x)
		}
		groups = append(groups, group)
	}

	// Part 1
	part1 := 0
	for i := 0; i < len(records); i++ {
		n := nSolutions(records[i], groups[i])
		//fmt.Println(string(records[i]), groups[i], n)
		part1 += n
	}
	fmt.Println("Part 1:", part1)
	//i := 2
	//fmt.Println(string(records[i]), groups[i], nSolutions(records[i], groups[i]))
}

func nSolutions(record []rune, groups []int) int {
	// TODO
	//fmt.Println(string(record), groups)
	if len(record) == 0 {
		if len(groups) == 0 {
			return 1
		} else {
			return 0
		}
	}
	if len(groups) == 0 {
		if has(record, '#') {
			return 0
		} else {
			return 1
		}
	}
	if sum(groups)+len(groups)-1 > len(record) {
		return 0
	}
	switch record[0] {
	case '#':
		for _, char := range record[:groups[0]] {
			if char == '.' {
				return 0
			}
		}
		newRecord := record[groups[0]:]
		if len(newRecord) > 0 {
			if newRecord[0] == '#' {
				return 0
			} else {
				return nSolutions(newRecord[1:], groups[1:])
			}
		}
		return nSolutions(newRecord, groups[1:])
	case '?':
		record1 := append(record[:0:0], record...) // copy record
		record2 := append(record[:0:0], record...)
		record1[0] = '#'
		record2[0] = '.'
		return nSolutions(record1, groups) + nSolutions(record2, groups)
	case '.':
		return nSolutions(record[1:], groups)
	}
	panic("Should be unreachable")
}

func sum(arr []int) (result int) {
	for _, x := range arr {
		result += x
	}
	return result
}

func has(arr []rune, x rune) bool {
	for _, y := range arr {
		if x == y {
			return true
		}
	}
	return false
}

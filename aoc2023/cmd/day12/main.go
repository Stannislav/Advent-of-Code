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
	//part1 := 0
	//for i := 0; i < len(records); i++ {
	//	part1 += nSolutions(records[i], groups[i])
	//}
	//fmt.Println("Part 1:", part1)
	//
	// Part 2
	for i := 0; i < len(records); i++ {
		nCopies := 5
		updatedRecord := records[i]
		updatedGroup := groups[i]
		for j := 0; j < nCopies-1; j++ {
			updatedRecord = append(updatedRecord, '?')
			updatedRecord = append(updatedRecord, records[i]...)
			updatedGroup = append(updatedGroup, groups[i]...)
		}
		records[i] = updatedRecord
		groups[i] = updatedGroup
	}
	part2 := 0
	for i := 0; i < len(records); i++ {
		fmt.Println("Record", i+1, "of ", len(records), "...")
		//n := splitSolve(records[i], groups[i])
		n := nSolutions(records[i], groups[i], []rune{})
		fmt.Println(string(records[i]), groups[i], n)
		part2 += n
	}
	fmt.Println("Part 2:", part2)
	// record #68 !!!

	//i := 2
	//fmt.Println("Given", string(records[i]), groups[i])
	//fmt.Println(string(records[i]), groups[i], splitSolve(records[i], groups[i]))

	//p := []string{"??", "??", "?##", "?", "??", "??", "?##"}
	//g := []int{1, 1, 3, 1, 1, 3}
	////g := []int{1, 1, 3}
	////p := []string{"??", "##", "#?"}
	//for s := range split(g, len(p)) {
	//	fmt.Println(s)
	//}
}

func split(groups []int, parts int) chan [][]int {

	splits := make(chan [][]int)
	do := func() {
		for ids := range take(parts-1, 0, len(groups)) {
			ids = append([]int{0}, ids...)
			ids = append(ids, len(groups))
			var intervals [][]int
			for i := 0; i < len(ids)-1; i++ {
				intervals = append(intervals, groups[ids[i]:ids[i+1]])
			}
			splits <- intervals
		}
		close(splits)
	}
	go do()
	return splits
}

func take(k, start, end int) chan []int {
	result := make(chan []int)

	do := func() {
		if k == 0 {
			result <- []int{}
		} else {
			for i := start; i <= end; i++ {
				for next := range take(k-1, i, end) {
					result <- append([]int{i}, next...)
				}

			}
		}
		close(result)
	}
	go do()

	return result
}

func splitSolve(record []rune, groups []int) int {
	partsWithEmpty := strings.Split(string(record), ".")
	var parts []string
	for _, part := range partsWithEmpty {
		if len(part) > 0 {
			parts = append(parts, part)
		}
	}
	fmt.Println("Parts:", parts)

	count := 0
	for dist := range split(groups, len(parts)) {
		total := 1
		for i, g := range dist {
			if sum(g)+len(g)-1 > len(parts[i]) {
				total = 0
				break
			}
			total *= nSolutions([]rune(parts[i]), g, []rune{})
		}
		count += total
	}
	return count
}

func nSolutions(record []rune, groups []int, current []rune) int {
	// TODO
	//fmt.Println(string(record), groups)
	if len(record) == 0 {
		if len(groups) == 0 {
			show(current)
			return 1
		} else {
			return 0
		}
	}
	if len(groups) == 0 {
		if has(record, '#') {
			return 0
		} else {
			show(append(current, record...))
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
				return nSolutions(newRecord[1:], groups[1:], append(append(current, record[:groups[0]]...), '.'))
			}
		}
		return nSolutions(newRecord, groups[1:], append(current, record[:groups[0]]...))
	case '?':
		record1 := append(record[:0:0], record...) // copy record
		record2 := append(record[:0:0], record...)
		record1[0] = '#'
		record2[0] = '.'
		return nSolutions(record1, groups, current) + nSolutions(record2, groups, current)
	case '.':
		return nSolutions(record[1:], groups, append(current, record[0]))
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

func show(solution []rune) {
	return
	fmt.Println("Found", string(solution))
}

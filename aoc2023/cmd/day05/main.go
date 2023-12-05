package main

import (
	"fmt"
	"os"
	"regexp"
	"sort"
	"strconv"
	"strings"
)

type Map struct {
	sources      []int // Must be sorted
	destinations []int
	lengths      []int
}

func main() {
	input, _ := os.ReadFile("input/05.txt")
	blocks := strings.Split(strings.Trim(string(input), "\n"), "\n\n")

	reNumber := regexp.MustCompile(`\d+`)
	var seeds []int
	var maps []Map

	// Parse seeds
	for _, xStr := range reNumber.FindAllString(blocks[0], -1) {
		x, _ := strconv.Atoi(xStr)
		seeds = append(seeds, x)
	}

	// Parse maps
	for _, block := range blocks[1:] {
		var triples [][]int
		for _, line := range strings.Split(block, "\n")[1:] {
			triple := make([]int, 3)
			for i, xStr := range reNumber.FindAllString(line, -1) {
				triple[i], _ = strconv.Atoi(xStr)
			}
			triples = append(triples, triple)
		}
		// Sort triples by source interval start
		sort.Slice(triples, func(i, j int) bool {
			return triples[i][1] < triples[j][1]
		})

		// Save map
		m := Map{[]int{}, []int{}, []int{}}
		for _, triple := range triples {
			m.sources = append(m.sources, triple[1])
			m.destinations = append(m.destinations, triple[0])
			m.lengths = append(m.lengths, triple[2])
		}
		maps = append(maps, m)
	}

	// Part 1
	var mappedSeeds []int
	for _, seed := range seeds {
		for _, m := range maps {
			seed = m.mapOne(seed)
		}
		mappedSeeds = append(mappedSeeds, seed)
	}
	sort.Ints(mappedSeeds)
	fmt.Println("Part 1:", mappedSeeds[0])

	// Part 2
	var intervals [][]int
	for i := 0; i < len(seeds)/2; i++ {
		intervals = append(intervals, []int{seeds[2*i], seeds[2*i+1]})
	}
	for _, m := range maps {
		var mappedIntervals [][]int
		for _, interval := range intervals {
			mappedIntervals = append(mappedIntervals, m.mapInterval(interval)...)
		}
		intervals = mappedIntervals
	}
	sort.Slice(intervals, func(i, j int) bool {
		return intervals[i][0] < intervals[j][0]
	})
	fmt.Println("Part 2:", intervals[0][0])
}

func (m Map) mapOne(point int) int {
	for i := 0; i < len(m.sources); i++ {
		if point >= m.sources[i] && point < m.sources[i]+m.lengths[i] {
			return point + m.destinations[i] - m.sources[i]
		}
	}
	return point
}

func (m Map) mapInterval(interval []int) [][]int {
	start := interval[0]
	length := interval[1]
	var mapped [][]int

	for i := 0; i < len(m.sources) && length > 0; i++ {
		if start < m.sources[i] { // start outside any interval: map to itself
			mappedLength := min(m.sources[i]-start, length)
			mapped = append(mapped, []int{start, mappedLength})
			start += mappedLength
			length -= mappedLength
		}
		if length == 0 {
			break
		}
		if start >= m.sources[i] && start < m.sources[i]+m.lengths[i] {
			mappedLength := min(m.sources[i]+m.lengths[i]-start, length)
			mapped = append(mapped, []int{m.destinations[i] + start - m.sources[i], mappedLength})
			start += mappedLength
			length -= mappedLength
		}
	}
	// Map the remaining tail to itself
	if length > 0 {
		mapped = append(mapped, []int{start, length})
	}

	return mapped
}

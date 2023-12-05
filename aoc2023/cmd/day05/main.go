package main

import (
	"fmt"
	"os"
	"regexp"
	"sort"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("input/05.txt")
	blocks := strings.Split(strings.Trim(string(input), "\n"), "\n\n")

	reNumber := regexp.MustCompile(`\d+`)
	var seeds []int
	var maps [][][]int // [nMap][nLine][destStart, sourceStart, length]

	// Parse seeds
	for _, xStr := range reNumber.FindAllString(blocks[0], -1) {
		x, _ := strconv.Atoi(xStr)
		seeds = append(seeds, x)
	}

	// Parse maps
	var mapNames []string
	for _, block := range blocks[1:] {
		var nMap [][]int
		for i, line := range strings.Split(block, "\n") {
			if i == 0 {
				mapNames = append(mapNames, line[:len(line)-4])
				continue
			}
			var nLine []int
			for _, xStr := range reNumber.FindAllString(line, -1) {
				x, _ := strconv.Atoi(xStr)
				nLine = append(nLine, x)
			}
			nMap = append(nMap, nLine)
		}
		// Sort map by source interval start
		sort.Slice(nMap, func(i, j int) bool {
			return nMap[i][1] < nMap[j][1]
		})
		maps = append(maps, nMap)
	}

	// Part 1
	var mappedSeeds []int
	for _, seed := range seeds {
		mappedSeeds = append(mappedSeeds, mapOne(seed, maps))
	}
	sort.Ints(mappedSeeds)
	fmt.Println("Part 1:", mappedSeeds[0])

	// Part 2
	var intervals [][]int
	for i := 0; i < len(seeds)/2; i++ {
		intervals = append(intervals, []int{seeds[2*i], seeds[2*i+1]})
	}

	for _, rangeMap := range maps {
		var newIntervals [][]int
		for _, interval := range intervals {
			newIntervals = append(newIntervals, mapInterval(interval, rangeMap)...)
		}
		intervals = newIntervals
		sort.Slice(intervals, func(i, j int) bool {
			return intervals[i][0] < intervals[j][0]
		})
	}

	sort.Slice(intervals, func(i, j int) bool {
		return intervals[i][0] < intervals[j][0]
	})
	fmt.Println("Part 2:", intervals[0][0])
}

func mapOne(point int, maps [][][]int) int {
	applyMap := func(point int, rangeMap [][]int) int {
		for _, line := range rangeMap {
			if point >= line[1] && point < line[1]+line[2] {
				return point + line[0] - line[1]
			}
		}
		return point
	}

	for _, rangeMap := range maps {
		point = applyMap(point, rangeMap)
	}
	return point
}

func mapInterval(interval []int, rangeMap [][]int) [][]int {
	start := interval[0]
	length := interval[1]
	var mapped [][]int

	for _, line := range rangeMap {
		if start < line[1] { // start outside any interval: map to itself
			diff := line[1] - start
			if length <= diff { // interval outside any mapping

				mapped = append(mapped, []int{start, length})
				length = 0
				break
			}
			// mapping part of the interval
			mapped = append(mapped, []int{start, diff})
			start += diff
			length -= diff
		}
		if start >= line[1] && start < line[1]+line[2] {
			diff := line[1] + line[2] - start
			if length <= diff {
				mapped = append(mapped, []int{start + line[0] - line[1], length})
				length = 0
				break
			}
			mapped = append(mapped, []int{start + line[0] - line[1], diff})
			start += diff
			length -= diff
		}
	}
	// Map the remaining tail to itself
	if length > 0 {
		mapped = append(mapped, []int{start, length})
	}

	return mapped
}

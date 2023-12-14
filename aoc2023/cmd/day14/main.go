package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/14.txt")
	input := string(inputBytes)

	platform := parse(input)
	tiltNorth(platform)
	fmt.Println("Part 1:", getLoad(platform))

	platform = parse(input)
	head, cycleLen := findCycle(platform)

	platform = parse(input)
	tail := (1_000_000_000 - head) % cycleLen
	for i := 0; i < head+tail; i++ {
		runCycle(platform)
	}
	fmt.Println("Part 2:", getLoad(platform))
}

func parse(input string) [][]rune {
	var platform [][]rune
	for _, line := range strings.Fields(input) {
		platform = append(platform, []rune(line))
	}
	return platform
}

func findCycle(platform [][]rune) (head, cycleLen int) {
	strHash := func(platform [][]rune) string {
		var sb strings.Builder
		for _, row := range platform {
			sb.WriteString(string(row))
		}
		return sb.String()
	}

	seen := map[string]int{strHash(platform): 0}
	i := 0
	for {
		runCycle(platform)
		i++
		currentHash := strHash(platform)
		prev, ok := seen[currentHash]
		if ok {
			return prev, i - prev
		} else {
			seen[strHash(platform)] = i
		}
	}
}

func runCycle(platform [][]rune) {
	tiltNorth(platform)
	tiltWest(platform)
	tiltSouth(platform)
	tiltEast(platform)
}

func tiltNorth(platform [][]rune) {
	changed := true
	for changed {
		changed = false
		for i := 1; i < len(platform); i++ {
			for j := 0; j < len(platform[i]); j++ {
				if platform[i][j] == 'O' && platform[i-1][j] == '.' {
					platform[i][j] = '.'
					platform[i-1][j] = 'O'
					changed = true
				}
			}
		}
	}
}

func tiltSouth(platform [][]rune) {
	changed := true
	for changed {
		changed = false
		for i := len(platform) - 2; i >= 0; i-- {
			for j := 0; j < len(platform[i]); j++ {
				if platform[i][j] == 'O' && platform[i+1][j] == '.' {
					platform[i][j] = '.'
					platform[i+1][j] = 'O'
					changed = true
				}
			}
		}
	}
}

func tiltWest(platform [][]rune) {
	changed := true
	for changed {
		changed = false
		for j := 1; j < len(platform[0]); j++ {
			for i := 0; i < len(platform); i++ {
				if platform[i][j] == 'O' && platform[i][j-1] == '.' {
					platform[i][j] = '.'
					platform[i][j-1] = 'O'
					changed = true
				}
			}
		}
	}
}

func tiltEast(platform [][]rune) {
	changed := true
	for changed {
		changed = false
		for j := len(platform[0]) - 2; j >= 0; j-- {
			for i := 0; i < len(platform); i++ {
				if platform[i][j] == 'O' && platform[i][j+1] == '.' {
					platform[i][j] = '.'
					platform[i][j+1] = 'O'
					changed = true
				}
			}
		}
	}
}

func getLoad(platform [][]rune) int {
	total := 0
	for i := 0; i < len(platform); i++ {
		for j := 0; j < len(platform[i]); j++ {
			if platform[i][j] == 'O' {
				total += len(platform) - i
			}
		}
	}
	return total
}

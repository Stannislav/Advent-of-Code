package main

import (
	"fmt"
	"os"
	"slices"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/10.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	maze := make(map[complex128]rune)
	var start complex128
	for i, line := range strings.Split(input, "\n") {
		for j, char := range line {
			pos := complex(float64(i), float64(j))
			maze[pos] = char
			if char == 'S' {
				start = pos
			}
		}
	}

	// Turn rules <tile> -> <from> -> <to>
	nextDir := map[rune]map[complex128]complex128{
		'|': {-1: -1, 1: 1},
		'-': {1i: 1i, -1i: -1i},
		'L': {1: 1i, -1i: -1},
		'J': {1: -1i, 1i: -1},
		'7': {-1: -1i, 1i: 1},
		'F': {-1: 1i, -1i: 1},
	}

	// Identify which tile S is on and replace it with the correct tile.
	// Where can we go from S? The directions identify uniquely the tiles.
	var startDirs []complex128
	for _, dir := range []complex128{1, 1i, -1, -1i} {
		// Does the adjacent tile have the given incoming direction?
		if _, ok := nextDir[maze[start+dir]][dir]; ok {
			startDirs = append(startDirs, dir)
		}
	}
	// Which tile's directions does S match with?
	for tile, dirs := range nextDir {
		found := true
		for _, dir := range dirs {
			if !slices.Contains(startDirs, dir) {
				found = false
				break
			}
		}
		if found {
			maze[start] = tile
			break
		}
	}

	// Walk the path and collect information for the solution.
	pos := start
	dir := startDirs[0]
	dist := 0 // for part 1

	/* For part 2, we assign each tile a kind:
	 * 0 = on path
	 * 1 = left of the path
	 * -1 = right of the path
	 * If the loop winds left, then all tiles of kind "1" will be inside the loop,
	 * and all tiles of kind "-1" will be outside the loop. For a right-winding loop
	 * the opposite is true.
	 * In the for-loop below we only mark tiles on the path and the adjacent tiles.
	 * Later, we'll mark the missing tiles by propagating the kind from the adjacent tiles.
	 */
	kinds := make(map[complex128]int)
	left := 1i
	right := -1i
	// In order to determine the winding direction (=spin), we need to know the sum of all
	// turns. Since the path is a loop, the imaginary part of the sum will be either 4i or -4i.
	// By dividing the imaginary part by 4i, we get the spin.
	turnSum := 0.0i
	for {
		kinds[pos] = 0
		pos += dir
		newDir := nextDir[maze[pos]][dir]
		turnSum += newDir / dir
		if _, ok := kinds[pos+dir*left]; !ok {
			kinds[pos+dir*left] = 1
		}
		if _, ok := kinds[pos+newDir*left]; !ok {
			kinds[pos+newDir*left] = 1
		}
		if _, ok := kinds[pos+dir*right]; !ok {
			kinds[pos+dir*right] = -1
		}
		if _, ok := kinds[pos+newDir*right]; !ok {
			kinds[pos+newDir*right] = -1
		}

		dir = newDir
		dist++
		if pos == start {
			break
		}
	}
	fmt.Println("Part 1:", dist/2)

	// Fill in the missing tile kinds.
	// Small detail: we must not try and fill the missing outside tiles, because
	// they are not enclosed by the loop and will fill to infinity since we
	// don't check for the maze boundaries.
	var queue []complex128
	spin := int(imag(turnSum) / 4) // i = 1/4 turn
	for p, kind := range kinds {
		if kind == spin {
			queue = append(queue, p)
		}
	}
	for len(queue) > 0 {
		p := queue[0]
		queue = queue[1:]
		for _, d := range []complex128{1, 1i, -1, -1i} {
			if _, ok := kinds[p+d]; !ok {
				kinds[p+d] = kinds[p]
				queue = append(queue, p+d)
			}
		}
	}

	part2 := 0
	for _, kind := range kinds {
		if kind == spin {
			part2++
		}
	}
	fmt.Println("Part 2:", part2)
}

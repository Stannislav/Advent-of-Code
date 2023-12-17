package main

import (
	"fmt"
	"image"
	"math"
	"os"
	"strconv"
	"strings"
)

type Record struct {
	Pos  image.Point
	Hist image.Point
}

type State struct {
	Loc  Record
	Heat int
}

func main() {
	inputBytes, _ := os.ReadFile("input/17.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	var heatMap [][]int
	for _, line := range strings.Split(input, "\n") {
		var row []int
		for _, char := range line {
			heat, _ := strconv.Atoi(string(char))
			row = append(row, heat)
		}
		heatMap = append(heatMap, row)
	}

	// Solve
	fmt.Println("Part 1:", solve(heatMap, genNext1, canStop1))
	fmt.Println("Part 2:", solve(heatMap, genNext2, canStop2))
}

func solve(heatMap [][]int, genNext func(Record) []Record, canStop func(Record) bool) int {
	maxX, maxY := len(heatMap)-1, len(heatMap[0])-1
	q := []State{{}}
	seen := make(map[Record]int)
	for len(q) > 0 {
		state := q[0]
		q = q[1:]

		// Been here with a better heat loss
		if heat, ok := seen[state.Loc]; ok && heat <= state.Heat {
			continue
		}
		seen[state.Loc] = state.Heat

		// Queue up next moves
		for _, loc := range genNext(state.Loc) {
			if loc.Pos.X >= 0 && loc.Pos.X <= maxX && loc.Pos.Y >= 0 && loc.Pos.Y <= maxY {
				q = append(q, State{loc, state.Heat + heatMap[loc.Pos.X][loc.Pos.Y]})
			}
		}

	}

	solution := math.MaxInt
	for loc, heat := range seen {
		if loc.Pos.X == maxX && loc.Pos.Y == maxY && canStop(loc) {
			solution = min(solution, heat)
		}
	}
	return solution
}
func genNext1(r Record) []Record {
	var next []Record

	for _, dir := range []image.Point{{0, 1}, {0, -1}, {1, 0}, {-1, 0}} {
		// Can't reverse direction
		if r.Hist.X*dir.X < 0 || r.Hist.Y*dir.Y < 0 {
			continue
		}
		// Must not go straight for more than 3 steps
		if dir.X != 0 && Abs(r.Hist.X) == 3 {
			continue
		}
		if dir.Y != 0 && Abs(r.Hist.Y) == 3 {
			continue
		}

		nextR := Record{r.Pos.Add(dir), r.Hist.Add(dir)}
		// Reset history if we change direction
		if dir.X == 0 {
			nextR.Hist.X = 0
		} else if dir.Y == 0 {
			nextR.Hist.Y = 0
		}
		next = append(next, nextR)
	}

	return next
}

func genNext2(r Record) []Record {
	var next []Record

	for _, dir := range []image.Point{{0, 1}, {0, -1}, {1, 0}, {-1, 0}} {
		// Can't reverse direction
		if r.Hist.X*dir.X < 0 || r.Hist.Y*dir.Y < 0 {
			continue
		}

		// Must move straight for at least 4 steps
		if dir.Y != 0 && r.Hist.X != 0 && Abs(r.Hist.X) < 4 {
			continue
		}
		if dir.X != 0 && r.Hist.Y != 0 && Abs(r.Hist.Y) < 4 {
			continue
		}

		// Can at most go 10 steps straight
		if dir.X != 0 && Abs(r.Hist.X) == 10 {
			continue
		}
		if dir.Y != 0 && Abs(r.Hist.Y) == 10 {
			continue
		}

		nextR := Record{r.Pos.Add(dir), r.Hist.Add(dir)}
		// Reset history if we change direction
		if dir.X == 0 {
			nextR.Hist.X = 0
		} else if dir.Y == 0 {
			nextR.Hist.Y = 0
		}
		next = append(next, nextR)
	}

	return next
}

func canStop1(_ Record) bool {
	return true
}

func canStop2(r Record) bool {
	return r.Hist.X >= 4 || r.Hist.Y >= 4
}

func Abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

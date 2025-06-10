package main

import (
	"fmt"
	"image"
	"os"
	"strings"
)

func main() {
	start, m := ParseInput("input/21.txt")
	fmt.Println("Part 1:", Part1(start, &m))
	fmt.Println("Part 2:", Part2(start, &m))
}

func ParseInput(filename string) (image.Point, Map) {
	inputBytes, _ := os.ReadFile(filename)
	input := strings.TrimSpace(string(inputBytes))

	start := image.Point{-1, -1}
	var plots [][]rune
	rocks := make(map[image.Point]bool)
	for x, line := range strings.Split(input, "\n") {
		var row []rune
		for y, c := range line {
			if c == 'S' {
				start = image.Point{x, y}
				c = '.'
			}
			row = append(row, c)
			if c == '#' {
				rocks[image.Point{x, y}] = true
			}
		}
		plots = append(plots, row)
	}
	m := Map{rocks: rocks, lim: image.Point{len(plots), len(plots[0])}}
	return start, m
}

func Part1(start image.Point, m *Map) int {
	return CountReachablePoints(start, m, 64)
}

func CountReachablePoints(start image.Point, m *Map, steps int) int {
	distances := m.Walk(start, steps)

	solution := 0
	for _, d := range distances {
		if d%2 == steps%2 {
			solution++
		}
	}

	return solution
}

func Part2(start image.Point, m *Map) int {
	/*
		The solution to part 2 is not a generalisation of part 1. Instead, it relies on two specific
		propreties of the input data and the problem statement:

		1. The amount of steps required, 26501365, aligns with the size of the map: the map size is 131x131,
		   and 26501365 = 65 + 202300 * 131. Starting from the centre of the map, (65, 65), one needs 65
		   steps to reach the edge of the map. So, the maximal distance one will reach is exactly 202300 map
		   tiles away from the central original map tile.
		2. The rocks are placed in such a way that taking 65 + n * 131 steps from the centre for any n will cover
		   all reachable points within that Manhattan distance.

		Given these two properties, the solution can be found by counting the number of covered map tiles.
		Because distances are measured in Manhattan metric, the reachable points form a rhombus shape around the
		centre of the map. For example, for 65 steps, the original map tile is divided into 5 parts: the central
		diamond, and the four corner triangles. These parts is all that is needed to compose the final big rhombus
		of reachable points.

		An additional detail which needs to be taken into account is whether the step number is even or odd. For an
		even step number only the reachable points with even Manhattan distance from the centre are counted, and
		analogously for the odd step number. So, each of the five parts of the map tile is divided into two
		subparts: one with even and one with odd distances.

		Here's a schematic of the five parts:

		 ______
		|  /\  |
		|a/  \b|
		|/ M  \|
		|\    /| = W
		|c\  /d|
		|__\/__|

		Let's make the following definitions:

		- M: all reachable points in the central rhombus.
		- a, b, c, d: all reachable points in the four corner parts.
		- x := a + b + c + d: all reachable corner points.
		- W := M + x: all reachable points in the original map tile.
		- <symbol>_e, <symbol>_o: even and odd points in <symbol>, meaning the distances from the centre point is even or odd.
		- N_n: the number of points which can be reached after exactly 65 + n * 131 steps.

		By drawing the examples of the step coverage for the first few values of n one can convince oneself that
		N_n are given by the following:

		N_0 = M_o
		N_1 = M_e + 4M_o + 2x
		N_2 = M_o + 4M_e + 8M_o + 6x
		N_3 = M_e + 4M_o + 8M_e +  12M_o + 12x

		These can be rewritten in terms of whole whole tiles W:

		N_0 =       W_o -  x_o
		N_1 =  W + 3W_o - 2x_o +  x_e
		N_2 = 4W + 5W_o - 3x_o + 2x_e
		N_3 = 9W + 7W_o - 4x_o + 3x_e

		We can now read off the general formula:

		N_n = n^2W + (2n+1)W_o - (n+1)x_o + nx_e
	*/
	N := 26501365
	n := (N - 65) / 131
	if 65+n*131 != N {
		panic(fmt.Sprintf("The given N=%d is not of the form 65 + n * 131", N))
	}

	return ReachablePointsByTiles(n, start, m)
}

func ReachablePointsByTiles(n int, start image.Point, m *Map) int {
	M_o := 0
	M_e := 0
	x_o := 0
	x_e := 0

	// Walk far enough to cover all reachable points in the original map tile.
	reachablePoints := m.Walk(start, 65+131)
	for pt := range reachablePoints {
		d := dist(pt, start)
		if d <= 65 {
			if (pt.X+pt.Y)%2 == 0 {
				M_e++
			} else {
				M_o++
			}
		} else if pt.X >= 0 && pt.X < m.lim.X && pt.Y >= 0 && pt.Y < m.lim.Y {
			if (pt.X+pt.Y)%2 == 0 {
				x_e++
			} else {
				x_o++
			}
		}
	}

	W_o := M_o + x_o
	W_e := M_e + x_e
	W := W_o + W_e

	return n*n*W + (2*n+1)*W_o - (n+1)*x_o + n*x_e
}

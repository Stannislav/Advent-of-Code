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
	distances := m.walk(start, steps)

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
	reachablePoints := m.walk(start, 65+131)
	for pt := range reachablePoints {
		d := dist(pt, start)
		if d <= 65 {
			if (pt.X+pt.Y)%2 == 0 {
				M_e++
			} else {
				M_o++
			}
		} else if m.isPointInBounds(pt) {
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

func Part2Debug(start image.Point, m *Map) int {

	fmt.Printf("Map size is %v\n", m.lim)

	// Distances from start to corners - all 130!
	from := start
	tos := []image.Point{{0, 0}, {0, 130}, {130, 0}, {130, 130}}
	cornerDistances := make(map[image.Point]int)
	for _, to := range tos {
		d, _ := m.Dist(from, to)
		cornerDistances[to] = d
	}
	fmt.Printf("Distances from start to corners: %v\n", cornerDistances)

	// Distances to cross through the centre - all 130!
	dHorizontal, _ := m.Dist(image.Point{65, 0}, image.Point{65, 130})
	fmt.Printf("Crossing through centre horizontally: %d\n", dHorizontal)
	dVertical, _ := m.Dist(image.Point{0, 65}, image.Point{130, 65})
	fmt.Printf("Crossing through centre vertically: %d\n", dVertical)

	// Distances for crossing the map - not all 130 and fluctuating. So, no regularity.
	crossingDistances := []int{}
	for x := 0; x < m.lim.X; x++ {
		d, _ := m.Dist(image.Point{x, 0}, image.Point{x, 130})
		crossingDistances = append(crossingDistances, d)
	}
	fmt.Printf("Distances to cross: %v\n", crossingDistances)

	// Maximal distances from edge centers - all 195 = 130 + 65.
	// maxDistanceLeft := 0
	// maxDistanceRight := 0
	// maxDistanceTop := 0
	// maxDistanceBottom := 0
	// for x := 0; x < m.lim.X; x++ {
	// 	for y := 0; y < m.lim.Y; y++ {
	// 		if m.isFree(image.Point{x, y}) {
	// 			dLeft, _ := m.Dist(image.Point{x, y}, image.Point{65, 0})
	// 			dRight, _ := m.Dist(image.Point{x, y}, image.Point{65, 130})
	// 			dTop, _ := m.Dist(image.Point{x, y}, image.Point{0, 65})
	// 			dBottom, _ := m.Dist(image.Point{x, y}, image.Point{130, 65})
	// 			if dLeft > maxDistanceLeft {
	// 				maxDistanceLeft = dLeft
	// 			}
	// 			if dRight > maxDistanceRight {
	// 				maxDistanceRight = dRight
	// 			}
	// 			if dTop > maxDistanceTop {
	// 				maxDistanceTop = dTop
	// 			}
	// 			if dBottom > maxDistanceBottom {
	// 				maxDistanceBottom = dBottom
	// 			}
	// 		}
	// 	}
	// }
	// fmt.Printf("Max distances from edge centers: Left: %d, Right: %d, Top: %d, Bottom: %d\n", maxDistanceLeft, maxDistanceRight, maxDistanceTop, maxDistanceBottom)

	// Compute reachable plots for the middle part and the corner parts.
	fmt.Printf("Total number of plots: %d\n", m.lim.X*m.lim.Y-len(m.rocks))
	fmt.Printf("Reachable plots in the middle part: %d\n", CountReachablePoints(start, m, 64)+CountReachablePoints(start, m, 65))
	fmt.Println("Reachable plots from the top left corner:", CountReachablePoints(image.Point{0, 0}, m, 65))
	fmt.Println("Reachable plots from the top right corner:", CountReachablePoints(image.Point{0, 130}, m, 65))
	fmt.Println("Reachable plots from the bottom left corner:", CountReachablePoints(image.Point{130, 0}, m, 65))
	fmt.Println("Reachable plots from the bottom right corner:", CountReachablePoints(image.Point{130, 130}, m, 65))

	// Test scaling
	solutions := []int{}
	for scale := 0; scale <= 5; scale++ {
		steps := 65 + scale*131
		solution := CountReachablePoints(start, m, steps)
		fmt.Printf("For scale %d, with %d steps, reachable plots: %d\n", scale, steps, solution)
		solutions = append(solutions, solution)
	}
	fmt.Println("Differences")
	for i := 1; i < len(solutions); i++ {
		fmt.Printf("Scale %d: %d\n", i, solutions[i]-solutions[0])
	}
	n := 3
	fmt.Printf("At %d: %d\n", n, CountReachablePoints(start, m, n))

	c := CountReachablePoints(start, m, 65)
	cPrime := CountReachablePoints(start, m, 64)
	evenCoords := []image.Point{}
	oddCoords := []image.Point{}
	for x := 0; x < m.lim.X; x++ {
		for y := 0; y < m.lim.Y; y++ {
			pt := image.Point{x, y}
			if _, ok := m.rocks[pt]; !ok {
				if (x+y)%2 == 0 {
					evenCoords = append(evenCoords, pt)
				} else {
					oddCoords = append(oddCoords, pt)
				}
			}
		}
	}
	x := len(evenCoords) - c
	xPrime := len(oddCoords) - cPrime
	fmt.Printf("c=%d\n", c)
	fmt.Printf("c'=%d\n", cPrime)
	fmt.Printf("x=%d\n", x)
	fmt.Printf("x'=%d\n", xPrime)
	fmt.Printf("c + c' + x + x' + rocks = %d\n", c+cPrime+x+xPrime+len(m.rocks))
	fmt.Printf("131 * 131 = %d\n", 131*131)

	distancesAll := m.walk(start, 65+131)
	cDist := make(map[image.Point]int)
	cPrimeDist := make(map[image.Point]int)
	xDist := make(map[image.Point]int)
	xPrimeDist := make(map[image.Point]int)

	walkedCPrime := filterEven(m.walk(start, 64))
	fmt.Printf("Walked c' from start: %d\n", len(walkedCPrime))
	for x := 0; x < m.lim.X; x++ {
		for y := 0; y < m.lim.Y; y++ {
			pt := image.Point{x, y}
			if _, reachable := distancesAll[pt]; !reachable {
				continue
			}
			d := abs(pt.X-start.X) + abs(pt.Y-start.Y)
			if d <= 65 {
				if (x+y)%2 == 0 {
					cPrimeDist[pt] = 0
				} else {
					cDist[pt] = 0
				}
			} else {
				if (x+y)%2 == 0 {
					xPrimeDist[pt] = 0
				} else {
					xDist[pt] = 0
				}
			}
		}
	}
	fmt.Printf("c=%d, len(cDist)=%d\n", c, len(cDist))
	fmt.Printf("c'=%d, len(cPrimeDist)=%d\n", cPrime, len(cPrimeDist))
	fmt.Printf("x=%d, len(xDist)=%d\n", x, len(xDist))
	fmt.Printf("x'=%d, len(xPrimeDist)=%d\n", xPrime, len(xPrimeDist))
	fmt.Printf("Distances from start with target 65+131: %v\n", len(distancesAll))
	for pt := range xDist {
		d := abs(pt.X-start.X) + abs(pt.Y-start.Y)
		if d <= 65 {
			fmt.Printf("Point in xDist with dist <= 65: %v, dist: %d\n", pt, d)
		}
	}
	distancesEven := filterEven(distancesAll)
	distancesOdd := filterOdd(distancesAll)
	fmt.Printf("Even distances: %d\nOdd distances: %d\n", len(distancesEven), len(distancesOdd))
	fmt.Printf("According to formula expected even distances to be c + 4c' + 2(x + x') = %d\n", cPrime+4*c+2*(x+xPrime))
	fmt.Printf("According to formula expected even distances to be c + 4c' + 2(x + x') = %d\n", len(cPrimeDist)+4*len(cDist)+2*(len(xDist)+len(xPrimeDist)))

	cc := len(cDist)
	ccPrime := len(cPrimeDist)
	xx := len(xDist)
	xxPrime := len(xPrimeDist)
	cube := cc + xx
	cubePrime := ccPrime + xxPrime
	w := cube + cubePrime
	fmt.Printf("According to formula expected even distances '1' to be c' + 4c + 2(x + x') = %d\n", ccPrime+4*cc+2*(xx+xxPrime))
	fmt.Printf("According to formula expected even distances '2' to be 9c + 4c' + 6(x + x') = %d\n", 9*cc+4*ccPrime+6*(xx+xxPrime))
	fmt.Printf("Cube-based formula for '1': cube + 4*cubePrime - 2*xxPrime + xx = %d\n", 4*cube+cubePrime+xxPrime-2*xx)

	// borderPoints := []image.Point{}
	// for pt, _ := range dist {
	// 	if abs(pt.X-start.X)+abs(pt.Y-start.Y) == target {
	// 		borderPoints = append(borderPoints, pt)
	// 	}
	// }
	// slices.SortFunc(borderPoints, func(a, b image.Point) int { return a.X - b.X })
	// fmt.Printf("Border points: %v (%d)\n", borderPoints, len(borderPoints))
	// for x := 0; x < m.lim.X; x++ {
	// 	for y := 0; y < m.lim.Y; y++ {
	// 		if _, ok := dist[image.Point{x, y}]; ok {
	// 			fmt.Print("#")
	// 		} else {
	// 			fmt.Print(".")
	// 		}
	// 	}
	// 	fmt.Println()
	// }
	nn := 202300
	// N_n = n^2W + 2(n+1)W_o - (n+1)x_o + nx_e
	return nn*nn*w + (2*nn+1)*cube - (nn+1)*xx + nn*xxPrime
}

func filterEven(distances map[image.Point]int) map[image.Point]int {
	result := make(map[image.Point]int)
	for pt, steps := range distances {
		if steps%2 == 0 {
			result[pt] = steps
		}
	}
	return result
}

func filterOdd(distances map[image.Point]int) map[image.Point]int {
	result := make(map[image.Point]int)
	for pt, steps := range distances {
		if steps%2 == 1 {
			result[pt] = steps
		}
	}
	return result
}

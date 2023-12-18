package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type Cmd struct {
	Dir  rune
	Dist int
}

func main() {
	inputBytes, _ := os.ReadFile("input/18.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	dir := map[rune]rune{'0': 'R', '1': 'D', '2': 'L', '3': 'U'}
	re := regexp.MustCompile(`([UDLR]) (\d+) \(#([0-9a-f]{6})\)`)
	var cmds1, cmds2 []Cmd
	for _, line := range strings.Split(input, "\n") {
		match := re.FindStringSubmatch(line)

		// Part 1
		dir1 := rune(match[1][0])
		dist1, _ := strconv.Atoi(match[2])
		cmds1 = append(cmds1, Cmd{dir1, dist1})

		// Part 2
		dir2 := dir[rune(match[3][5])]
		dist2, _ := strconv.ParseInt(match[3][:5], 16, 64)
		cmds2 = append(cmds2, Cmd{dir2, int(dist2)})
	}

	// Solve
	fmt.Println("Part 1:", getArea(cmds1))
	fmt.Println("Part 2:", getArea(cmds2))
}

func getArea(cmds []Cmd) int {
	/*
		Compute the area using a path integral around the closed boundary.

		The formula can be found using Green's theorem:

			A = 1/2 integral (xdy - ydx)

		Additionally, we have to account for the thickness of the boundary by
		adding the term

			A_b = L/2 + 1

		where L is the length of the boundary. To verify this, consider the
		following example:

			2 ####
			1 ####
			0 ####
			  0123

		One can imagine that the coordinates refer to the centres of the
		blocks. By connecting the centres of the corners one obtains a
		rectangle with the inside area of 6 (2 complete blocks, 6 halves, and
		4 quarters), and the outside area of 6 (10 halves, and 4 quarters).

		The inside area is readily given by the formula for A above:

			A = 1/2 * (3 * 2 - 2 * (-3)) = 6

		For the outside area each boundary block contributes a half, except
		for the corner blocks, which contribute an additional quarter. This
		generalises to any boundary shape and gives the formula for A_b above:

			A_b = 1/2 * 10 + 1 = 6

		The total area is given by the sum of A and A_b:

			A_total = A + A_b
					= A + L/2 + 1
					= 1/2 integral (xdy - y dx) + L/2 + 1
	*/
	dx := map[rune]int{'U': -1, 'D': 1, 'L': 0, 'R': 0}
	dy := map[rune]int{'U': 0, 'D': 0, 'L': -1, 'R': 1}

	x, y := 0, 0
	length, area := 0, 0
	for _, cmd := range cmds {
		length += cmd.Dist
		area += cmd.Dist * (y*dx[cmd.Dir] - x*dy[cmd.Dir])
		x += cmd.Dist * dx[cmd.Dir]
		y += cmd.Dist * dy[cmd.Dir]
	}
	if area < 0 { // if boundary is traced clockwise, but we don't care.
		area = -area
	}
	return area/2 + length/2 + 1
}

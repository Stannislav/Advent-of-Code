package main

import (
	"fmt"
	"image"
	"os"
	"strings"
)

func main() {
	start, m := ParseInput("input/21.txt")
	fmt.Println("Part 1 :", bruteForce(start, m, 64))

}

func ParseInput(filename string) (image.Point, Map) {
	inputBytes, _ := os.ReadFile(filename)
	input := strings.TrimSpace(string(inputBytes))

	start := image.Point{X: -1, Y: -1}
	var plots [][]rune
	rocks := make(map[image.Point]bool)
	for x, line := range strings.Split(input, "\n") {
		var row []rune
		for y, c := range line {
			if c == 'S' {
				start = image.Point{X: x, Y: y}
				c = '.'
			}
			row = append(row, c)
			if c == '#' {
				rocks[image.Point{X: x, Y: y}] = true
			}
		}
		plots = append(plots, row)
	}
	m := Map{rocks: rocks, lim: image.Point{X: len(plots), Y: len(plots[0])}}
	return start, m
}

func bruteForce(start image.Point, m Map, target int) int {
	q := []Record{{Pos: start, Steps: 0}}
	dist := make(map[image.Point]int)
	for len(q) > 0 {
		r := q[0]
		q = q[1:]
		cont := r.Steps < target
		for _, n := range m.next(r.Pos) {
			if d, ok := dist[n]; !ok || r.Steps+1 < d {
				dist[n] = r.Steps + 1
				if cont {
					q = append(q, Record{Pos: n, Steps: r.Steps + 1})
				}
			}
		}
	}

	solution := 0
	for _, steps := range dist {
		if steps%2 == target%2 {
			solution++
		}
	}

	return solution
}

type Record struct {
	Pos   image.Point
	Steps int
}

type Map struct {
	rocks map[image.Point]bool
	lim   image.Point
}

func (m Map) next(pt image.Point) []image.Point {
	var result []image.Point
	for _, d := range []image.Point{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} {
		nxt := pt.Add(d)
		if m.isFree(nxt) {
			result = append(result, nxt)
		}
	}
	return result
}

func (m Map) isFree(p image.Point) bool {
	if _, ok := m.rocks[mod(p, m.lim)]; ok {
		return false
	}
	return true
}

func mod(pt, lim image.Point) image.Point {
	return image.Point{
		X: (pt.X%lim.X + lim.X) % lim.X,
		Y: (pt.Y%lim.Y + lim.Y) % lim.Y,
	}
}

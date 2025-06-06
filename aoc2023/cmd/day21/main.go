package main

import (
	"container/list"
	"fmt"
	"image"
	"os"
	"strings"
)

func main() {
	start, m := ParseInput("input/21.txt")
	fmt.Println("Part 1 :", Part1(start, &m))

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

func Part1(start image.Point, m *Map) int {
	return bruteForce(start, m, 64)
}

func bruteForce(start image.Point, m *Map, target int) int {
	q := list.New()
	q.PushBack(Record{Pos: start, Steps: 0})
	dist := make(map[image.Point]int)
	for q.Len() > 0 {
		r := q.Remove(q.Front()).(Record)
		cont := r.Steps < target
		for _, n := range m.next(r.Pos) {
			if d, ok := dist[n]; !ok || r.Steps+1 < d {
				dist[n] = r.Steps + 1
				if cont {
					q.PushBack(Record{Pos: n, Steps: r.Steps + 1})
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

func (m *Map) next(pt image.Point) []image.Point {
	var result []image.Point
	for _, d := range []image.Point{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} {
		nxt := pt.Add(d)
		if m.isFree(nxt) {
			result = append(result, nxt)
		}
	}
	return result
}

func (m *Map) nextNoWrap(pt image.Point) []image.Point {
	var result []image.Point
	for _, d := range []image.Point{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} {
		nxt := pt.Add(d)
		if m.isPointInBounds(nxt) && m.isFree(nxt) {
			result = append(result, nxt)
		}
	}
	return result
}

func (m *Map) isPointInBounds(p image.Point) bool {
	return p.X >= 0 && p.X < m.lim.X && p.Y >= 0 && p.Y < m.lim.Y
}

func (m *Map) isFree(p image.Point) bool {
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

func (m *Map) Dist(from image.Point, to image.Point) (int, error) {
	if !m.isFree(from) {
		return -1, fmt.Errorf("the starting point %v is not free", from)
	}
	if !m.isFree(to) {
		return -1, fmt.Errorf("the target point %v is not free", to)
	}
	dist := map[image.Point]int{from: 0}
	q := list.New()
	q.PushBack(from)
	for q.Len() > 0 {
		p := q.Remove(q.Front()).(image.Point)
		if p == to {
			break
		}
		for _, n := range m.nextNoWrap(p) {
			if _, ok := dist[n]; !ok {
				dist[n] = dist[p] + 1
				q.PushBack(n)
			}
		}
	}
	if d, ok := dist[to]; ok {
		return d, nil
	} else {
		return -1, fmt.Errorf("no path found from %v to %v", from, to)
	}
}

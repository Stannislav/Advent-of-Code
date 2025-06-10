package main

import (
	"container/list"
	"image"
)

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

func (m *Map) isPointInBounds(p image.Point) bool {
	return p.X >= 0 && p.X < m.lim.X && p.Y >= 0 && p.Y < m.lim.Y
}

func (m *Map) isFree(p image.Point) bool {
	if _, ok := m.rocks[mod(p, m.lim)]; ok {
		return false
	}
	return true
}

type Record struct {
	Pos   image.Point
	Steps int
}

func (m *Map) Walk(from image.Point, steps int) map[image.Point]int {
	q := list.New()
	q.PushBack(Record{Pos: from, Steps: 0})
	dist := map[image.Point]int{from: 0}
	for q.Len() > 0 {
		r := q.Remove(q.Front()).(Record)
		if r.Steps == steps {
			break
		}
		for _, n := range m.next(r.Pos) {
			if d, ok := dist[n]; !ok || r.Steps+1 < d {
				dist[n] = r.Steps + 1
				q.PushBack(Record{Pos: n, Steps: r.Steps + 1})
			}
		}
	}

	return dist
}

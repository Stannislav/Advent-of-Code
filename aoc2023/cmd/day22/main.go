package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/22.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	re := regexp.MustCompile(`\d+`)
	var bricks []*Brick
	for _, line := range strings.Split(input, "\n") {
		numbersStr := re.FindAllString(line, -1)
		var numbers []int
		for _, s := range numbersStr {
			x, _ := strconv.Atoi(s)
			numbers = append(numbers, x)
		}
		brick := Brick{
			Point{numbers[0], numbers[1], numbers[2]},
			Point{numbers[3], numbers[4], numbers[5]},
		}
		bricks = append(bricks, &brick)
	}

	// Let all bricks fall
	moved := true
	for moved {
		moved = false
		for _, brick := range bricks {
			if brick.canMove(bricks) {
				brick.down()
				moved = true
			}
		}
	}

	// Record which bricks support each other (parents support children).
	children := make([][]int, len(bricks))
	parents := make([][]int, len(bricks))
	for p, parent := range bricks {
		for c, child := range bricks {
			if parent.supports(child) {
				children[p] = append(children[p], c)
				parents[c] = append(parents[c], p)
			}
		}
	}

	// Solve
	part1 := 0
	part2 := 0
	for i := 0; i < len(bricks); i++ {
		nFallen := countFallenIfRemove(i, children, parents)
		if nFallen == 0 {
			part1++
		}
		part2 += nFallen
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}

type Point struct {
	X, Y, Z int
}

type Brick struct {
	P1, P2 Point
}

func (b *Brick) down() {
	b.P1.Z--
	b.P2.Z--
}

func (b *Brick) up() {
	b.P1.Z++
	b.P2.Z++
}

func (b *Brick) intersects(other *Brick) bool {
	intersectX := (b.P1.X <= other.P2.X && other.P2.X <= b.P2.X) || (other.P1.X <= b.P2.X && b.P2.X <= other.P2.X)
	intersectY := (b.P1.Y <= other.P2.Y && other.P2.Y <= b.P2.Y) || (other.P1.Y <= b.P2.Y && b.P2.Y <= other.P2.Y)
	intersectZ := (b.P1.Z <= other.P2.Z && other.P2.Z <= b.P2.Z) || (other.P1.Z <= b.P2.Z && b.P2.Z <= other.P2.Z)
	return intersectX && intersectY && intersectZ
}

func (b *Brick) supports(other *Brick) bool {
	if b == other {
		return false
	}
	other.down()
	result := b.intersects(other)
	other.up()

	return result
}

func (b *Brick) canMove(bricks []*Brick) bool {
	if b.P1.Z == 1 {
		return false
	}
	result := true
	for _, other := range bricks {
		if other.supports(b) {
			result = false
			break
		}
	}
	return result
}

func countFallenIfRemove(toRemoveFirst int, children [][]int, parents [][]int) (nFallen int) {
	fallen := make(map[int]bool)
	for i := 0; i < len(children); i++ {
		fallen[i] = false
	}

	q := []int{toRemoveFirst}
	for len(q) > 0 {
		toRemove := q[0]
		q = q[1:]
		fallen[toRemove] = true

		// For all children of toRemove, if they have no other support, add them to the queue.
		for _, j := range children[toRemove] {
			if fallen[j] {
				continue
			}
			hasOtherSupport := false
			for _, k := range parents[j] {
				if !fallen[k] {
					hasOtherSupport = true
					break
				}
			}
			if !hasOtherSupport {
				q = append(q, j)
			}
		}
	}

	fallen[toRemoveFirst] = false // initial brick should not be counted
	for i := 0; i < len(children); i++ {
		if fallen[i] {
			nFallen++
		}
	}
	return
}

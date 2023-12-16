package main

import (
	"fmt"
	"os"
	"strings"
)

type Vec struct {
	I, J int
}

func (v Vec) Add(w Vec) Vec {
	return Vec{v.I + w.I, v.J + w.J}
}

type Beam struct {
	Loc, Dir Vec
}

func main() {
	inputBytes, _ := os.ReadFile("input/16.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse Input
	lines := strings.Split(input, "\n")
	iLim, jLim := len(lines), len(lines[0])
	mirrors := make(map[Vec]rune)
	for i, line := range lines {
		for j, char := range line {
			mirrors[Vec{i, j}] = char
		}
	}

	// Solve
	fmt.Println("Part 1:", shoot(Beam{Vec{0, 0}, Vec{0, 1}}, mirrors))
	part2 := 0
	for i := 0; i < iLim; i++ {
		part2 = max(part2, shoot(Beam{Vec{i, 0}, Vec{0, 1}}, mirrors))
		part2 = max(part2, shoot(Beam{Vec{i, jLim - 1}, Vec{0, -1}}, mirrors))
	}
	for j := 0; j < jLim; j++ {
		part2 = max(part2, shoot(Beam{Vec{0, j}, Vec{1, 0}}, mirrors))
		part2 = max(part2, shoot(Beam{Vec{iLim - 1, j}, Vec{-1, 0}}, mirrors))
	}
	fmt.Println("Part 2:", part2)
}

func shoot(startBeam Beam, mirrors map[Vec]rune) int {
	beams := []Beam{startBeam}
	seen := make(map[Beam]bool)

	for len(beams) > 0 {
		var updatedBeams []Beam
		for _, beam := range beams {
			// If we've seen this beam before, then we're looping.
			if _, ok := seen[beam]; ok {
				continue
			}

			// Apply mirror logic
			char, ok := mirrors[beam.Loc]
			if !ok { // Discard beams that go off bounds
				continue
			}
			seen[beam] = true

			// Going parallel to splitters does nothing
			if (char == '-' && beam.Dir.I == 0) || (char == '|' && beam.Dir.J == 0) {
				char = '.'
			}

			// Beam step
			switch char {
			case '.':
				updatedBeams = append(updatedBeams, Beam{beam.Loc.Add(beam.Dir), beam.Dir})
			case '/':
				newDir := Vec{-beam.Dir.J, -beam.Dir.I}
				updatedBeams = append(updatedBeams, Beam{beam.Loc.Add(newDir), newDir})
			case '\\':
				newDir := Vec{beam.Dir.J, beam.Dir.I}
				updatedBeams = append(updatedBeams, Beam{beam.Loc.Add(newDir), newDir})
			case '-', '|':
				newDir1 := Vec{beam.Dir.J, beam.Dir.I}
				newDir2 := Vec{-beam.Dir.J, -beam.Dir.I}
				updatedBeams = append(updatedBeams, Beam{beam.Loc.Add(newDir1), newDir1})
				updatedBeams = append(updatedBeams, Beam{beam.Loc.Add(newDir2), newDir2})
			}
		}
		beams = updatedBeams
	}

	energised := make(map[Vec]bool)
	for beam := range seen {
		energised[beam.Loc] = true
	}
	return len(energised)
}

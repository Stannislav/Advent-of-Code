package main

import (
	"image"
	"testing"
)

func TestParseInput(t *testing.T) {
	start, m := ParseInput("example_input.txt")
	expectedStart := image.Point{X: 5, Y: 5}
	expectedLim := image.Point{X: 11, Y: 11}
	expectedRockCount := 40
	if start != expectedStart {
		t.Errorf("Expected start point %v, got %v", expectedStart, start)
	}
	if len(m.rocks) != expectedRockCount {
		t.Errorf("Expected %d rocks in the map, but got %d", expectedRockCount, len(m.rocks))
	}
	if m.lim != expectedLim {
		t.Errorf("Expected map limit %v, got %v", expectedLim, m.lim)
	}
}

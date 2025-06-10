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

func TestCountReachablePoints(t *testing.T) {
	start, m := ParseInput("example_input.txt")
	test_cases := []struct {
		steps    int
		expected int
	}{
		{steps: 6, expected: 16},
		{steps: 10, expected: 50},
		{steps: 50, expected: 1594},
		{steps: 100, expected: 6536},
		{steps: 500, expected: 167004},
		{steps: 1000, expected: 668697},
		{steps: 5000, expected: 16733044},
	}
	for _, tc := range test_cases {
		result := CountReachablePoints(start, &m, tc.steps)
		if result != tc.expected {
			t.Errorf("Expected %d steps to reach %d plots, but got %d", tc.steps, tc.expected, result)
		}
	}
}

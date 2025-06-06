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

func TestSolve(t *testing.T) {
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
		result := bruteForce(start, &m, tc.steps)
		if result != tc.expected {
			t.Errorf("Expected %d steps to reach %d plots, but got %d", tc.steps, tc.expected, result)
		}
	}
}

func TestDist(t *testing.T) {
	start, m := ParseInput("example_input.txt")
	test_cases := []struct {
		from          image.Point
		to            image.Point
		expected      int
		expectedError string
	}{
		{start, start, 0, ""},
		{image.Point{1, 5}, image.Point{8, 3}, -1, "the starting point (1,5) is not free"},
		{start, image.Point{6, 5}, 1, "the target point (6,5) is not free"},
		{image.Point{0, 0}, image.Point{10, 10}, 20, ""},
		{image.Point{1, 8}, image.Point{4, 8}, 5, ""},
		{image.Point{3, 3}, image.Point{6, 8}, 10, ""},
	}
	for _, tc := range test_cases {
		result, err := m.Dist(tc.from, tc.to)
		if err == nil && result != tc.expected {
			t.Errorf("Expected distance from %v to %v to be %d, but got %d", tc.from, tc.to, tc.expected, result)
		}
		if err != nil && err.Error() != tc.expectedError {
			t.Errorf("Expected error %s, but got %s", tc.expectedError, err.Error())
		}
	}
}

package main

import (
	"image"
	"testing"
)

func TestParseInput(t *testing.T) {
	m := ParseInput("example_input.txt")
	if len(m) != 23*23 {
		t.Errorf("Expected the number of map points to be 23*23=%d, but got %d\n", 23*23, len(m))
	}

	test_cases := []struct {
		pt image.Point
		r  rune
	}{
		{image.Point{0, 0}, '#'},
		{image.Point{0, 1}, '.'},
		{image.Point{5, 4}, '>'},
		{image.Point{12, 21}, 'v'},
		{image.Point{22, 21}, '.'},
		{image.Point{22, 22}, '#'},
	}

	for _, tc := range test_cases {
		if m[tc.pt] != tc.r {
			t.Errorf("Expected the map at point %v to be %c but got %c", tc.pt, tc.r, m[tc.pt])
		}
	}
}

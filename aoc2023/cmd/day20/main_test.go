package main

import "testing"

func TestParseInput(t *testing.T) {
	test_cases := []struct {
		filename     string
		moduleNumber int
	}{
		{"example_input_1.txt", 5},
		{"example_input_2.txt", 5},
	}
	for _, tc := range test_cases {
		modules, connections := parseInput(tc.filename)
		if len(modules) != tc.moduleNumber {
			t.Errorf("Expected %d modules in %s, but found %d", tc.moduleNumber, tc.filename, len(modules))
		}
		if len(connections) != tc.moduleNumber {
			t.Errorf("Expected %d connections in %s, but found %d", tc.moduleNumber, tc.filename, len(connections))
		}
	}
}

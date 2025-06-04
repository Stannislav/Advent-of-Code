package main

import "testing"

func TestParseInput(t *testing.T) {
	test_cases := []struct {
		filename    string
		connections map[string][]string
		moduleTypes map[string]string
	}{
		{
			"example_input_1.txt",
			map[string][]string{"broadcaster": {"a", "b", "c"}, "a": {"b"}, "b": {"c"}, "c": {"inv"}, "inv": {"a"}},
			map[string]string{"broadcaster": "Broadcast", "a": "FlipFlop", "b": "FlipFlop", "c": "FlipFlop", "inv": "Conjunction"},
		},
		{
			"example_input_2.txt",
			map[string][]string{"broadcaster": {"a"}, "a": {"inv", "con"}, "inv": {"b"}, "b": {"con"}, "con": {"output"}},
			map[string]string{"broadcaster": "Broadcast", "a": "FlipFlop", "inv": "Conjunction", "b": "FlipFlop", "con": "Conjunction"},
		},
	}
	for _, tc := range test_cases {
		modules, connections := parseInput(tc.filename)

		// Test connections
		if len(connections) != len(tc.connections) {
			t.Fatalf("Expected %d connections in %s, but found %d", len(tc.connections), tc.filename, len(connections))
		}
		for name, targets := range tc.connections {
			if len(connections[name]) != len(targets) {
				t.Errorf("Expected %d connections for %s in %s, but found %d", len(targets), name, tc.filename, len(connections[name]))
			}
			for i, target := range targets {
				if connections[name][i] != target {
					t.Errorf("Expected connection %s -> %s in %s, but found %s", name, target, tc.filename, connections[name][i])
				}
			}
		}

		// Test module types
		if len(modules) != len(tc.moduleTypes) {
			t.Fatalf("Expected %d modules in %s, but found %d", len(tc.moduleTypes), tc.filename, len(modules))
		}
	}
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"slices"
	"strings"
)

type Edge struct {
	a, b string
}

func NewEdge(a, b string) Edge {
	if a < b {
		return Edge{a, b}
	}
	return Edge{b, a}
}

func main() {
	inputBytes, _ := os.ReadFile("input/25.txt")
	input := strings.TrimSpace(string(inputBytes))

	// Parse input
	graph := make(map[string][]string)
	re := regexp.MustCompile(`\w+`)
	for _, line := range strings.Split(input, "\n") {
		match := re.FindAllString(line, -1)
		for _, s := range match[1:] {
			graph[match[0]] = append(graph[match[0]], s)
			graph[s] = append(graph[s], match[0])
		}
	}

	// Part1
	BCs := getBetweennessCentrality(graph)
	var toDelete Edge
	for i := 0; i < 3; i++ {
		maxBC := 0
		for edge, BC := range BCs {
			if BC > maxBC {
				maxBC = BC
				toDelete = edge
			}
		}
		delete(BCs, toDelete)
		removeEdge(graph, toDelete)
	}

	fmt.Println("Part 1:", componentSize(toDelete.a, graph)*componentSize(toDelete.b, graph))
}

func removeEdge(graph map[string][]string, edge Edge) {
	idx := slices.Index(graph[edge.a], edge.b)
	graph[edge.a] = slices.Delete(graph[edge.a], idx, idx+1)
	idx = slices.Index(graph[edge.b], edge.a)
	graph[edge.b] = slices.Delete(graph[edge.b], idx, idx+1)
}

// componentSize computes the size of the connected component containing start.
func componentSize(start string, graph map[string][]string) int {
	seen := make(map[string]bool)
	q := []string{start}
	for len(q) > 0 {
		v := q[0]
		q = q[1:]
		seen[v] = true
		for _, w := range graph[v] {
			if !seen[w] {
				q = append(q, w)
			}
		}
	}
	return len(seen)
}

func getBetweennessCentrality(graph map[string][]string) map[Edge]int {
	// Collect all node names
	var nodes []string
	for node, targets := range graph {
		if !slices.Contains(nodes, node) {
			nodes = append(nodes, node)
		}
		for _, target := range targets {
			if !slices.Contains(nodes, target) {
				nodes = append(nodes, target)
			}
		}
	}

	// Compute betweenness centrality
	bc := make(map[Edge]int)
	for i := 0; i < len(nodes); i++ {
		prev := pathsFrom(nodes[i], graph)
		for j := i + 1; j < len(nodes); j++ {
			path := makePath(nodes[i], nodes[j], prev)
			for k := 0; k < len(path)-1; k++ {
				bc[NewEdge(path[k], path[k+1])]++
			}
		}
	}
	return bc
}

// makePath reconstructs the shortest path from start to end using prev map returned by pathsFrom.
func makePath(start, end string, prev map[string]string) (path []string) {
	for v := end; v != start; v = prev[v] {
		path = append(path, v)
	}
	path = append(path, start)
	slices.Reverse(path)
	return
}

// pathsFrom uses Dijkstra's algorithm to find the shortest paths from start to all other nodes.
func pathsFrom(start string, graph map[string][]string) map[string]string {
	dist := make(map[string]int)
	done := make(map[string]bool)
	prev := make(map[string]string)

	q := []string{start}
	dist[start] = 0
	for len(q) > 0 {
		v := q[0]
		q = q[1:]
		done[v] = true
		var next []string
		for _, w := range graph[v] {
			if !done[w] {
				d, ok := dist[w]
				if !ok || dist[v]+1 < d {
					dist[w] = dist[v] + 1
					prev[w] = v
					next = append(next, w)
				}
			}
		}
		slices.SortFunc(next, func(i, j string) int { return dist[i] - dist[j] })
		q = append(q, next...)
	}
	return prev
}

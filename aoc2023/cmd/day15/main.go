package main

import (
	"fmt"
	"os"
	"slices"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/15.txt")
	input := strings.TrimSpace(string(inputBytes))
	tokens := strings.Split(input, ",")

	// Part 1
	part1 := 0
	for _, token := range tokens {
		part1 += int(hash(token))
	}
	fmt.Println("Part 1:", part1)

	// Part 2
	labels := make(map[byte][]string)
	lenses := make(map[byte][]int)
	for _, token := range tokens {
		if token[len(token)-1] == '-' {
			label := token[:len(token)-1]
			h := hash(label)
			idx := slices.Index(labels[h], label)
			if idx >= 0 {
				labels[h] = append(labels[h][:idx], labels[h][idx+1:]...)
				lenses[h] = append(lenses[h][:idx], lenses[h][idx+1:]...)
			}
		} else {
			label := token[:len(token)-2]
			focal, _ := strconv.Atoi(token[len(token)-1:])
			h := hash(label)
			idx := slices.Index(labels[h], label)
			if idx >= 0 {
				lenses[h][idx] = focal
			} else {
				labels[h] = append(labels[h], label)
				lenses[h] = append(lenses[h], focal)
			}
		}
	}
	part2 := 0
	for box, focalLengths := range lenses {
		for i, focal := range focalLengths {
			part2 += (int(box) + 1) * (i + 1) * focal
		}
	}
	fmt.Println("Part 2:", part2)
}

func hash(token string) (result byte) {
	for _, c := range token {
		result = (result + byte(c)) * 17
	}
	return
}

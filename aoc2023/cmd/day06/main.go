package main

import (
	"fmt"
	"math"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	inputBytes, _ := os.ReadFile("input/06.txt")
	input := string(inputBytes)
	var time, dist []int

	// Part 1
	time, dist = parse(input)
	fmt.Println("Part 1:", solve(time, dist))

	// Part 2
	fixedInput := strings.Replace(input, " ", "", -1)
	time, dist = parse(fixedInput)
	fmt.Println("Part 2:", solve(time, dist))
}

func parse(input string) (time []int, dist []int) {
	re := regexp.MustCompile(`\d+`)
	numbers := re.FindAllString(input, -1)
	for i := 0; i < len(numbers)/2; i += 1 {
		t, _ := strconv.Atoi(numbers[i])
		d, _ := strconv.Atoi(numbers[i+len(numbers)/2])
		time = append(time, t)
		dist = append(dist, d)
	}
	return
}

func nWin(time, dist int) int {
	epsilon := 0.000000000001
	t := float64(time)
	d := float64(dist)
	w1 := math.Ceil((t-math.Sqrt(t*t-4*d))/2 + epsilon)
	w2 := math.Floor((t+math.Sqrt(t*t-4*d))/2 - epsilon)

	return int(w2 - w1 + 1)
}

func solve(time []int, dist []int) int {
	solution := 1
	for i := 0; i < len(time); i += 1 {
		solution *= nWin(time[i], dist[i])
	}
	return solution
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	data, _ := os.ReadFile("input/02.txt")
	lines := strings.Split(strings.Trim(string(data), "\n"), "\n")

	reLine := regexp.MustCompile(`Game \d+: (.*)`)
	reRed := regexp.MustCompile(`(\d+) red`)
	reGreen := regexp.MustCompile(`(\d+) green`)
	reBlue := regexp.MustCompile(`(\d+) blue`)

	maxCount := []int{12, 13, 14}
	idSum := 0
	totalPower := 0
	for id, line := range lines {
		match := reLine.FindStringSubmatch(line)
		isPossible := 1
		minCount := []int{0, 0, 0}
		for _, set := range strings.Split(match[1], "; ") {
			drawnStr := [][]string{
				reRed.FindStringSubmatch(set),
				reGreen.FindStringSubmatch(set),
				reBlue.FindStringSubmatch(set),
			}
			drawn := make([]int, 3)
			for i, result := range drawnStr {
				if len(result) > 1 {
					drawn[i], _ = strconv.Atoi(result[1])
				} else {
					drawn[i] = 0
				}
			}
			for i, count := range drawn {
				minCount[i] = max(minCount[i], count)
				if count > maxCount[i] {
					isPossible = 0
				}
			}
		}
		totalPower += minCount[0] * minCount[1] * minCount[2]
		idSum += (id + 1) * isPossible
	}
	fmt.Println("Part 1:", idSum)
	fmt.Println("Part 2:", totalPower)
}

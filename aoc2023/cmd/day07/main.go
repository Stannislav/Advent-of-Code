package main

import (
	"fmt"
	"os"
	"regexp"
	"sort"
	"strconv"
	"strings"
)

type Hand struct {
	cards []rune
	bid   int
	type_ int
}

func main() {
	inputBytes, _ := os.ReadFile("input/07.txt")
	lines := strings.Split(strings.TrimSpace(string(inputBytes)), "\n")

	// Parse input
	re := regexp.MustCompile(`(.*) (\d+)`)
	var hands []Hand
	for _, line := range lines {
		match := re.FindStringSubmatch(line)
		cards := []rune(match[1])
		bid, _ := strconv.Atoi(match[2])
		hands = append(hands, Hand{cards, bid, getType(cards)})
	}

	// Part 1
	sortHands(hands, false)
	fmt.Println("Part 1:", score(hands))

	// Part 2
	var updatedHands []Hand
	for _, hand := range hands {
		newHand := Hand{hand.cards, hand.bid, getType(applyJoker(hand.cards))}
		updatedHands = append(updatedHands, newHand)
	}
	sortHands(updatedHands, true)
	fmt.Println("Part 2:", score(updatedHands))
}

func getType(cards []rune) int {
	// 1 << 5: Five of a kind
	// 1 << 4: Four of a kind
	// 1 << 3: Full house
	// 1 << 2: Three of a Kind
	// 1 << 1: Two pair
	// 1 << 0: One pair
	//      0: High card
	counts := make(map[rune]int)
	for _, card := range cards {
		counts[card] += 1
	}
	var buckets []int
	for _, count := range counts {
		buckets = append(buckets, count)
	}

	if len(buckets) == 1 {
		return 1 << 5
	} else if len(buckets) == 2 {
		if buckets[0] == 4 || buckets[1] == 4 {
			return 1 << 4
		} else {
			return 1 << 3
		}
	} else if len(buckets) == 3 {
		if buckets[0] == 3 || buckets[1] == 3 || buckets[2] == 3 {
			return 1 << 2
		} else {
			return 1 << 1
		}
	} else if len(buckets) == 4 {
		return 1 << 0
	} else {
		return 0
	}
}

func applyJoker(cards []rune) []rune {
	// Replace each Joker with the card that has the highest count
	counts := make(map[rune]int)
	for _, card := range cards {
		if card != 'J' {
			counts[card] += 1
		}
	}
	currentBest := func() rune {
		var bestCard rune
		for card, count := range counts {
			if count > counts[bestCard] {
				bestCard = card
			}
		}
		return bestCard
	}

	newCards := make([]rune, len(cards))
	for i, card := range cards {
		if card == 'J' {
			newCard := currentBest()
			newCards[i] = newCard
			counts[newCard] += 1
		} else {
			newCards[i] = card
		}
	}

	return newCards
}

func sortHands(hands []Hand, withJoker bool) {
	cardValues := make(map[rune]int)
	var order string
	if withJoker {
		order = "J23456789TQKA"
	} else {
		order = "23456789TJQKA"
	}
	for i, card := range order {
		cardValues[card] = i
	}

	sort.Slice(hands, func(i, j int) bool {
		if hands[i].type_ == hands[j].type_ {
			for k := 0; k < len(hands[i].cards); k += 1 {
				card1 := hands[i].cards[k]
				card2 := hands[j].cards[k]
				if card1 != card2 {
					return cardValues[card1] < cardValues[card2]
				}
			}
			return false
		} else {
			return hands[i].type_ < hands[j].type_
		}
	})
}

func score(hands []Hand) (score int) {
	for i, hand := range hands {
		score += hand.bid * (i + 1)
	}
	return
}

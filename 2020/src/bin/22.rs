//! Solutions for day 22.
use std::collections::{HashSet, VecDeque};
use std::fs;

/// A deck modeled by a double-ended queue
type Deck = VecDeque<usize>;

/// Parse deck from an input string.
///
/// # Arguments
/// * `deck_s`: The part of the problem input corresponsing to one deck.
///
/// # Returns
/// `Deck`: The parsed deck.
fn parse_deck(deck_s: &str) -> Deck {
    deck_s
        .split('\n')
        .skip(1)
        .map(|x| x.parse().unwrap())
        .collect()
}

/// Play a game of Combat and return the winning score.
///
/// Using the `recursive_game` flag it's possible to simulate
/// both the part 1 and part 2 versions.
///
/// # Arguments
/// * `decks`: The starting decks.
/// * `recursive_game`: Type of game. Set to `false` for part 1, and
/// to `true` for part 2.
///
/// # Returns
/// `usize`: The score of the winner.
fn winning_score(decks: &[Deck], recursive_game: bool) -> usize {
    // `play` needs mutable decks.
    let mut decks = [decks[0].clone(), decks[1].clone()];
    let winner = play(&mut decks, recursive_game);
    // Compute the score of the winner.
    decks[winner]
        .iter()
        .rev()
        .enumerate()
        .map(|(i, card)| (i + 1) * card)
        .sum()
}

/// Run the game simulation and return the winner.
///
/// # Arguments
/// * `decks`: The two player's initial decks.
/// * `recursive_game`: Type of game. Set to `false` for part 1, and
/// to `true` for part 2.
///
/// # Returns
/// `usize`: The index of the winner (0 or 1).
fn play(decks: &mut [Deck; 2], recursive_game: bool) -> usize {
    // Needed for part 2 to resolve loops
    let mut seen: HashSet<[Deck; 2]> = HashSet::new();
    // Loop over rounds
    loop {
        // Check the any of the decks is empty. If yes, the other player wins.
        if decks[0].is_empty() {
            return 1;
        }
        if decks[1].is_empty() {
            return 0;
        }
        // Loop detected - first player wins.
        if recursive_game && seen.contains(decks) {
            return 0;
        }
        // Mark current state as seen.
        seen.insert(decks.clone());
        // Deal cards.
        let card_0 = decks[0].pop_front().unwrap();
        let card_1 = decks[1].pop_front().unwrap();
        // Determine the round winnter based on dealt cards.
        let do_recurse = decks[0].len() >= card_0 && decks[1].len() >= card_1;
        let round_winner = match do_recurse & recursive_game {
            // Winner based on a subgame. This is the new rule of part 2.
            true => {
                let mut sub_decks = [
                    decks[0].iter().take(card_0).cloned().collect(),
                    decks[1].iter().take(card_1).cloned().collect(),
                ];
                play(&mut sub_decks, recursive_game)
            }
            // Winner based on card value like in part 1.
            false => match card_0 > card_1 {
                true => 0,
                false => 1,
            },
        };
        // The winner takes the two cards
        if round_winner == 0 {
            decks[0].push_back(card_0);
            decks[0].push_back(card_1);
        } else {
            decks[1].push_back(card_1);
            decks[1].push_back(card_0);
        }
    }
}

#[doc(hidden)]
fn main() {
    // Read input
    let decks: Vec<Deck> = fs::read_to_string("input/22.txt")
        .expect("Can't read input file")
        .trim()
        .split("\n\n")
        .map(|s| parse_deck(s))
        .collect();

    // Part 1
    println!("Part 1: {}", winning_score(&decks, false));

    // Part 2
    println!("Part 2: {}", winning_score(&decks, true));
}

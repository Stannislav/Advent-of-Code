use std::collections::HashMap;
use std::fs;

#[doc(hidden)]
fn main() {
    // Read input
    let input: Vec<usize> = fs::read_to_string("input/23.txt")
        .expect("Can't read input")
        .trim()
        .chars()
        .map(|c| c.to_digit(10).unwrap() as usize)
        .collect();

    // Part 1
    // Play
    let cups = play(&input, 100);
    // Transform the linked list to the output string
    let mut output: Vec<String> = Vec::new();
    let mut cup = cups.get(&1).unwrap();
    while *cup != 1 {
        output.push(format!("{}", cup));
        cup = cups.get(&cup).unwrap();
    }
    println!("Part 1: {}", output.join(""));

    // Part 2
    // Extend the input
    let mut extended_input = input.clone();
    extended_input.extend(input.len() + 1..=1_000_000);
    // Play
    let cups = play(&extended_input, 10_000_000);
    // Compute the output
    let v1 = cups.get(&1).unwrap();
    let v2 = cups.get(v1).unwrap();
    println!("Part 2: {}", v1 * v2);
}

/// Play the crab cup game.
///
/// # Arguments
/// * `input`: A vector with the starting cup order.
/// * `n_rounds`: The number of moves to play.
///
/// # Returns
/// `HashMap<usize, usize>`: The final state of the cups.
fn play(input: &[usize], n_moves: usize) -> HashMap<usize, usize> {
    // Transform the input vector to a circular linked list
    let mut cups: HashMap<usize, usize> = HashMap::new();
    for (&i, &next) in input.iter().zip(input.iter().skip(1)) {
        cups.insert(i, next);
    }
    // Close the circle
    cups.insert(input[input.len() - 1], input[0]);

    // Run the game iterations. Start with the first element in the input vector.
    let size = cups.len();
    let mut current: usize = input[0];
    for _ in 0..n_moves {
        // Remove the next 3 elements. Need to dereference to make a copy
        // because we need to make a mutable borrow of cups later on.
        let cup1 = *cups.get(&current).unwrap();
        let cup2 = *cups.get(&cup1).unwrap();
        let cup3 = *cups.get(&cup2).unwrap();
        cups.insert(current, *cups.get(&cup3).unwrap());

        // Compute the destination.
        // What we want to do is `destination = (destination - 1) % size`,
        // but because the numbering starts with 1 and not with 0, we need
        // to shift the left by 1 before the modulo and then back to the right,
        // which gives `(destination - 2) % size + 1`. Additionally, because
        // `destination` is unsigned we need to make sure that the subtraction
        // doesn't give a negative number and therefore add `size`.
        let mut destination = (current + size - 2) % size + 1;
        while destination == cup1 || destination == cup2 || destination == cup3 {
            destination = (destination + size - 2) % size + 1;
        }

        // Insert the 3 removed elements to the destination
        *cups.entry(cup3).or_default() = *cups.get(&destination).unwrap();
        *cups.entry(destination).or_default() = cup1;
        current = *cups.get(&current).unwrap();
    }

    // Return the final state of the game
    cups
}

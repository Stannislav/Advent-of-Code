use std::collections::HashMap;
use std::fs;

#[doc(hidden)]
fn main() {
    // Read input data
    let starters: Vec<isize> = fs::read_to_string("input/15.txt")
        .expect("Can't read input")
        .trim()
        .split(',')
        .map(|x| x.parse().unwrap())
        .collect();

    // Solutions
    println!("Part 1: {}", memory_game(&starters, 2020));
    println!("Part 2: {}", memory_game(&starters, 30_000_000));
}

/// Run the memory game described in the problem statement.
///
/// # Arguments
/// * `starters` -- The starting numbers from the input
/// * `n_iterations` -- The number of iterations to run the game for.
fn memory_game(starters: &[isize], n_iterations: isize) -> isize {
    // We'll use starters.pop() below, so we make a copy
    let mut starters = starters.to_owned();

    // Initialize the game; `last_n` is the last said number and
    // `said_when` tracks when a given number was last said.
    let n_starting = starters.len() as isize;
    let mut last_n: isize = starters.pop().unwrap();
    let mut said_when: HashMap<isize, isize> = starters
        .into_iter()
        .enumerate()
        .map(|(i, n)| (n, i as isize + 1))
        .collect();

    // For each iteration the said number is going to be the difference
    // between the current turn (`i`) and the turn it was last said
    // previously (`last_i`). If the number is said for the first time
    // then the difference is 0.
    for last_i in n_starting..n_iterations {
        let say = match said_when.get(&last_n) {
            Some(i) => last_i - i,
            None => 0,
        };
        // Update when the last number was last said
        said_when.insert(last_n, last_i);
        // The last number is what whe say during this turn
        last_n = say;
    }
    last_n
}

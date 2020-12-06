// Switch to nightly toolchain via `rustup default nightly` first.
// This is necessary for `fold_first`.
#![feature(iterator_fold_self)]

use std::collections::HashSet;
use std::fs;


fn main() {
    // Read input file
    let groups: Vec<String> = fs::read_to_string("input/06.txt")
        .unwrap()
        .split("\n\n")
        .map(|s| s.to_string())
        .collect();
    
    // Note the use of `flat_map`
    let part_1: usize = groups
        .iter()
        .map(|group| {
            group
                .lines()
                .flat_map(|line| line.chars())
                .collect::<HashSet<char>>()
                .len()
        })
        .sum();
    println!("Part 1: {}", part_1);

    // Note the use of `fold_first`
    let part_2: usize = groups
        .iter()
        .map(|group| {
            group
                .lines()
                .map(|line| line.chars().collect::<HashSet<char>>())
                .fold_first(|h1, h2| h1.intersection(&h2).cloned().collect())
                .unwrap()
                .len()
        }).sum();
    println!("Part 2: {}", part_2);
}

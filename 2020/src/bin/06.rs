use std::collections::HashSet;
use std::fs;

#[doc(hidden)]
fn main() {
    // Read input
    // * First vector = group
    // * Second vector = person in group
    // * Hash set = answers of person
    let data: Vec<Vec<HashSet<char>>> = fs::read_to_string("input/06.txt")
        .expect("Can't read input file")
        .split("\n\n")
        .map(|group| {
            group
                .lines()
                .map(|person| person.chars().collect())
                .collect()
        })
        .collect();

    // Part 1
    // For each group compute the union of all answers of all people in that group.
    // The count for that group is the size of the union. Sum all counts.
    let part_1 = data
        .iter()
        .map(|group| {
            group
                .iter()
                .fold(HashSet::new(), |acc, hs| acc.union(hs).cloned().collect())
                .len()
        })
        .fold(0, |c1, c2| c1 + c2);
    println!("Part 1: {}", part_1);

    // Part 2
    // For each group compute the intersection of all answers of all people in that group.
    // The count for that group is the size of the intersection. Sum all counts.
    let part_2 = data
        .iter()
        .map(|group| {
            group
                .iter()
                .skip(1)
                .fold(group[0].clone(), |acc, hs| {
                    acc.intersection(hs).cloned().collect()
                })
                .len()
        })
        .fold(0, |c1, c2| c1 + c2);
    println!("Part 2: {}", part_2);
}

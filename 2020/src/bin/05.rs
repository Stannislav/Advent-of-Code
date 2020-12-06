use std::fs;

/// Parse the seat ID from an input pattern.
///
/// The binary search described in the problem is equivalent
/// to interpreting the pattern as a binary representation of
/// an unsigned integer where `'F'` and `'L'` translate to `0`
/// and `'B'` and `'R'` translate to `1`. For example:
///
///     FBFBBFFRLR => 0101100101 = 357
///
/// # Arguments
/// `pattern` -- A pattern from the input file, e.g. `"FBFBBFFRLR"`.
fn seat_id(pattern: &str) -> u16 {
    let mut result: u16 = 0;
    for x in pattern.chars() {
        result <<= 1;
        if x == 'B' || x == 'R' {
            result |= 1;
        }
    }
    result
}

#[doc(hidden)]
fn main() {
    // Read data
    let mut all_ids: Vec<u16> = fs::read_to_string("input/05.txt")
        .expect("Can't read input data.")
        .lines()
        .map(seat_id)
        .collect();

    // Part 1
    println!("Part 1: {}", all_ids.iter().max().unwrap());

    // Part 2
    all_ids.sort_unstable();
    for i in 0..all_ids.len() - 1 {
        if all_ids[i + 1] - all_ids[i] == 2 {
            println!("Part 2: {}", all_ids[i] + 1);
            break;
        }
    }
}

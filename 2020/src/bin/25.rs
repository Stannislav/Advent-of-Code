use std::fs;

#[doc(hidden)]
fn main() {
    // Read input
    let input: Vec<usize> = fs::read_to_string("input/25.txt")
        .expect("Can't read input")
        .trim()
        .lines()
        .map(|line| line.parse().unwrap())
        .collect();
    let card_key = input[0];
    let door_key = input[1];

    // Determine the loop values
    let card_loop = find_loop_size(card_key);
    let door_loop = find_loop_size(door_key);

    // Compute the encryption key
    let encryption_key = transform(door_key, card_loop);

    // Consistency check
    assert_eq!(encryption_key, transform(card_key, door_loop));

    // Result
    println!("Part 1: {}", encryption_key);
}

/// Brute-force the loop size.
///
/// Find the loop size such that `subject^loop_size % 20201227 = key`,
/// with `subject = 7`.
///
/// # Arguments
/// * `key`: The target key.
///
/// # Returns
/// `loop_size`: The brute-forced loop size.
fn find_loop_size(key: usize) -> usize {
    let mut loop_size = 0;
    let mut value = 1;
    let subject = 7;
    while value != key {
        value = value * subject % 20201227;
        loop_size += 1;
    }
    loop_size
}

/// Run the transformation with given subject and loop size.
///
/// We need to compute `value = subject^loop_size % 20201227`.
///
/// # Arguments
/// * `subject`: The input subject.
/// * `loop_size`: The input loop size.
///
/// # Returns
/// `value`: The output value produced by the transformation.
fn transform(subject: usize, loop_size: usize) -> usize {
    let mut value = 1;
    for _ in 0..loop_size {
        value = value * subject % 20201227;
    }
    value
}

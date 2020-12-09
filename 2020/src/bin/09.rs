use std::fs;

// The length of the preamble. Can be adjusted for testing purposes.
const N_PREAMBLE: usize = 25;

/// Check validity agains a given preamble.
///
/// A number is valid agains a given preamble if it
/// can be written as a sum of two elements of the preamble.
///
/// This check is used for part 1 of the challenge.
///
/// # Arguments
/// * `n` -- The number to check.
/// * `preamble` -- The preamble.
///
/// # Returns
/// * `bool` -- The outcome of the check.
fn is_valid(n: usize, preamble: &[usize]) -> bool {
    for (i1, n1) in preamble.iter().enumerate() {
        for n2 in preamble.iter().skip(i1 + 1) {
            if n == n1 + n2 {
                return true;
            }
        }
    }
    false
}

/// Solve part 1.
///
/// Iterate over all elements of data and check if they are valid
/// agains the corresponding preamble. The preamble are the
/// `N_PREAMBLE` elements preceding the given element.
///
/// The validity check is done using the [`is_valid`](is_valid) function.
///
/// This function returns the first invalid element found in the data.
///
/// # Arguments
/// * `data` -- The input data.
///
/// # Returns
/// * `usize` -- The first invalid element in `data`.
fn solve_1(data: &[usize]) -> usize {
    for (i, &n) in data.iter().enumerate().skip(N_PREAMBLE) {
        if !is_valid(n, &data[i - N_PREAMBLE..i]) {
            return n;
        }
    }
    0
}

/// Solve part 2.
///
/// For a given target number, find a contiguous range in the
/// given data the elements of which sum to target. Return the
/// sum of the smallest and largest number of that range
///
/// # Arguments
/// `data` -- The input data.
/// `target` -- The target number. Usually the result of solving part 1.
///
/// # Returns
/// `usize` -- The sum of the smallest and largest number of the found range.
fn solve_2(data: &[usize], target: usize) -> usize {
    for start in 0..data.len() {
        let mut sum = 0;
        for (end, n) in data.iter().enumerate().skip(start) {
            sum += n;
            if sum == target {
                let slice = &data[start..end + 1];
                return slice.iter().min().unwrap() + slice.iter().max().unwrap();
            }
        }
    }
    0
}

#[doc(hidden)]
fn main() {
    // Read input
    let data: Vec<usize> = fs::read_to_string("input/09.txt")
        .expect("Can't read input file")
        .trim()
        .lines()
        .map(|x| x.parse().unwrap())
        .collect();

    // Part 1
    let part_1 = solve_1(&data);
    println!("Part 1: {}", part_1);

    // Part 2
    let part_2 = solve_2(&data, part_1);
    println!("Part 2: {}", part_2);
}

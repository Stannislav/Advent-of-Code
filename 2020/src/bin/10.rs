use std::fs;

#[doc(hidden)]
fn main() {
    // Read input
    let mut data: Vec<usize> = fs::read_to_string("input/10.txt")
        .expect("Can't read input file")
        .trim()
        .lines()
        .map(|x| x.parse().unwrap())
        .collect();

    // Add the first and the last elements as described in the problem statement
    data.push(0);
    data.push(*data.iter().max().unwrap() + 3);

    // For both parts it's useful to have the data sorted
    data.sort_unstable();

    // Solutions
    println!("Part 1: {}", solve_1(&data));
    println!("Part 2: {}", solve_2(&data));
}

/// Solve part 1.
///
/// The goal is given a sorted vector of integers to find
/// the number of intervals of size 1 and 3 and to compute
/// their product.
///
/// # Arguments
/// * `data` -- The sorted sequence of input data with two added elements.
///
/// # Returns
/// `usize` -- The problem solution.
fn solve_1(data: &[usize]) -> usize {
    let diffs: Vec<usize> = data
        .iter()
        .zip(data.iter().skip(1))
        .map(|(&n1, &n2)| n2 - n1)
        .collect();
    let count_1 = diffs.iter().filter(|&&x| x == 1).count();
    let count_3 = diffs.iter().filter(|&&x| x == 3).count();
    count_1 * count_3
}

/// Solve part 2.
///
/// The goal is to find all possible paths from the smallest
/// to the largest value by using jumps of at most length 3.
/// We are assuming that data is sorted and that its elements
/// are unique.
///
/// We use dynamic programming and starting from the end build
/// up an array `n_paths` that contains the number of paths from
/// the correspondign element to the end.
fn solve_2(data: &[usize]) -> usize {
    // Initialize the memoization array, 0 means not processed yet.
    let mut n_paths: Vec<usize> = vec![0; data.len()];

    // The number of paths from the last element to itself is 1
    *n_paths.last_mut().unwrap() = 1;

    // Starting from the end start filling `n_paths`
    for pos in (0..data.len()).rev() {
        // For a given position `pos` the next item will be one of the three following ones
        for next_pos in (pos + 1)..(pos + 4) {
            if let Some(next_value) = data.get(next_pos) {
                if next_value - data[pos] <= 3 {
                    n_paths[pos] += n_paths[next_pos];
                }
            }
        }
    }
    n_paths[0]
}

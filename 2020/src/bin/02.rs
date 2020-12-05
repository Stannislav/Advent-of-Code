extern crate regex;

use regex::Regex;
use std::fs;

/// Solve both part 1 and part 2.
///
/// # Arguments
/// * `lines`: The lines from the input file.
///
/// # Description
/// Check line by line if the conditions of part 1 and part 2 are fulfilled.
fn solve(lines: &Vec<String>) -> (i32, i32) {
    let re = Regex::new(r"(\d+)-(\d+) ([a-z]): ([a-z]+)").expect("Broken regex");
    let (mut part_1, mut part_2) = (0, 0);

    for line in lines.iter() {
        // Parse line of the form "n_1-n_2 c: password"
        let caps = re.captures(line).unwrap();
        let n_1: usize = (&caps[1]).parse().unwrap();
        let n_2: usize = (&caps[2]).parse().unwrap();
        let c: char = (&caps[3]).parse().unwrap();
        let password: String = (&caps[4]).parse().unwrap();

        // Part 1
        // Count occurrences of c and check n_1 <= count <= n_2
        let count = password.matches(c).count();
        if count >= n_1 && count <= n_2 {
            part_1 += 1;
        }

        // Part 2
        // Check that (password[n_1 - 1] == c) xor (password[n_2 - 1] == c)
        let has_1 = c == password.chars().nth(n_1 - 1).unwrap();
        let has_2 = c == password.chars().nth(n_2 - 1).unwrap();
        if (has_1 && !has_2) || (!has_1 && has_2) {
            part_2 += 1;
        }
    }

    (part_1, part_2)
}

#[doc(hidden)]
fn main() {
    // Read input
    let lines: Vec<String> = fs::read_to_string("input/02.txt")
        .expect("Can't read input file")
        .lines()
        .map(|x| x.parse().expect("Can't parse line"))
        .collect();

    // Solve
    let (part_1, part_2) = solve(&lines);
    println!("Part 1: {}", part_1);
    println!("Part 2: {}", part_2);
}

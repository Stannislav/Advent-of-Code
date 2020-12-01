use std::fs;

/// Solution to part 1
///
/// Assuming that `numbers` is sorted it runs indices, `i` and `j`,
/// one from the left and one from the right. Because `numbers` is
/// sorted the sum `number[i] + numbers[j]` is always decreasing, so
/// we can optimize checking if this number is below the target value
/// of 2020.
fn solve_1(numbers: &Vec<i32>) -> i32 {
    let n = numbers.len();
    for i in 0..n {
        for j in (i + 1..n).rev() {
            if numbers[i] + numbers[j] == 2020 {
                return numbers[i] * numbers[j];
            } else if numbers[i] + numbers[j] < 2020 {
                // we won't find a good j for this i any more
                break;
            }
        }
    }

    return -1;
}

/// Solution to part 2
///
/// Similarly to part 1 `i` runs from the left and `j` runs from the right.
/// The third index, `k`, runs starting from `j` towards `i`. Again we can
/// make optimizations based on the fact that `numbers` is sorted.
fn solve_2(numbers: &Vec<i32>) -> i32 {
    let n = numbers.len();
    for i in 0..n {
        for j in (i + 1..n).rev() {
            if numbers[i] + numbers[j] > 2020 {
                // a third number would only increase the sum even more
                continue;
            }
            for k in (i + 1..j).rev() {
                if numbers[i] + numbers[j] + numbers[k] == 2020 {
                    return numbers[i] * numbers[j] * numbers[k];
                } else if numbers[i] + numbers[j] + numbers[k] < 2020 {
                    // won't find a good k in this branch any more
                    break;
                }
            }
        }
    }

    return -1;
}

fn main() {
    // Read input
    let mut numbers: Vec<i32> = fs::read_to_string("input/01.txt")
        .expect("Can't read the input file")
        .split_whitespace()
        .map(|s| s.parse().unwrap())
        .collect();
    numbers.sort();

    // part 1
    let solution_1 = solve_1(&numbers);
    println!("Part 1: {}", solution_1);

    // part 2
    let solution_2 = solve_2(&numbers);
    println!("Part 2: {}", solution_2);
}

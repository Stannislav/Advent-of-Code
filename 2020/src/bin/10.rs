use std::fs;

fn main() {
    // Read input
    let mut data: Vec<usize> = fs::read_to_string("input/10.txt")
        .expect("Can't read input file")
        .trim()
        .lines()
        .map(|x| x.parse().unwrap())
        .collect();
    data.push(0);
    data.push(*data.iter().max().unwrap() + 3);
    data.sort_unstable();

    // Solutions
    println!("Part 1: {}", solve_1(&data));
    println!("Part 2: {}", solve_2(&data));
}

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

fn solve_2(data: &[usize]) -> usize {
    let mut n_paths: Vec<usize> = vec![0; data.len()];
    *n_paths.last_mut().unwrap() = 1;

    for pos in (0..data.len()).rev() {
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

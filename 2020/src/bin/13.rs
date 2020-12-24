use std::collections::HashMap;
use std::fs;

fn main() {
    let lines: Vec<String> = fs::read_to_string("input/13.txt")
        .expect("Can't read input")
        .lines()
        .map(|line| line.to_string())
        .collect();
    let timestamp: usize = lines[0].parse().unwrap();
    let ids: HashMap<usize, usize> = lines[1]
        .trim()
        .split(',')
        .enumerate()
        .filter(|&(_i, s)| s != "x")
        .map(|(i, s)| (i, s.parse().unwrap()))
        .collect();

    // let timestamp: usize = 939;
    // let ids: Vec<usize> = vec![7,13,59,31,19].into_iter().collect();

    // Part 1
    let mut min_wait = *(ids.values().max().unwrap());
    let mut part_1: usize = 0;
    for &id in ids.values() {
        let wait = id - (timestamp % id);
        // println!("id = {}, wait = {}, min_wait = {}, {} {}", id, wait, min_wait, timestamp % id, timestamp);
        if wait < min_wait {
            min_wait = wait;
            part_1 = id * min_wait;
        }
    }
    println!("Part 1: {}", part_1); // = 3035

    // Part 2 -- brute-force -- takes too long
    let (_i, id) = ids.iter().max_by(|a, b| a.1.cmp(&b.1)).unwrap();
    let mut timestamp = *id;
    while !check(&ids, timestamp) {
        timestamp += id;
        // println!("{}", timestamp);
    }
    println!("Part 2: {}", timestamp);
}

fn check(ids: &HashMap<usize, usize>, timestamp: usize) -> bool {
    for (&i, &id) in ids.iter() {
        if i != id - (timestamp % id) {
            return false;
        }
    }
    true
}

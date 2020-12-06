use std::fs;

fn main() {
    // Read input
    let mut ids: Vec<usize> = fs::read_to_string("input/05.txt")
        .expect("Can't read input file")
        .trim()
        .replace(&['F', 'L'][..], "0")
        .replace(&['B', 'R'][..], "1")
        .lines()
        .map(|x| usize::from_str_radix(x, 2).expect("Can't parse line"))
        .collect();

    // Solutions
    println!("Part 1: {}", ids.iter().max().unwrap());
    ids.sort_unstable();
    for (x1, x2) in ids.iter().zip(ids.iter().skip(1)) {
        if x2 - x1 == 2 {
            println!("Part 2: {}", x1 + 1);
            break;
        }
    }
}

use std::fs;

fn main() {
    let data: Vec<String> = fs::read_to_string("input/07.txt")
        .expect("Can't read input file")
        .lines()
        .map(|line| line.to_string())
        .collect();
    
    println!("{:#?}", data);
}
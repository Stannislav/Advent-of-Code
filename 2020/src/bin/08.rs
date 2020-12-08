use std::fs;

fn parse_input(lines: &Vec<String>) {

}

fn main() {
    let lines: Vec<String> = fs::read_to_string("input/08.txt")
        .expect("Can't read input file.")
        .trim()
        .lines()
        .map(|x| x.to_string())
        .collect();
    
    println!("{:#?}", lines);

    // Parse input
    parse_input(&lines);
}
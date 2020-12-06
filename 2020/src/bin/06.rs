use std::fs;

fn main() {
    let data: Vec<String> = fs::read_to_string("input/06.txt")
        .expect("Can't read input file")
        .split("\n\n")
        .map(|x| x.to_string())
        .collect();
    
    println!("{:#?}", data);
}

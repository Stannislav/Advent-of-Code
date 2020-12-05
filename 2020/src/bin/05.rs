use std::fs;

fn main() {
    let data = fs::read_to_string("input/05.txt")
        .expect("Can't read input data.");
    println!("{}", data);
}

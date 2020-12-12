extern crate num;

use num::complex;
use std::fs;
use std::str::FromStr;

#[derive(Debug)]
enum Command {
    N(i32),
    W(i32),
    S(i32),
    E(i32),
    L(i32),
    R(i32),
    F(i32),
}

impl FromStr for Command {
    type Err = ();

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        // Line is of the form `<c><n>`, e.g. "N15" or "R90"
        let mut line = line.to_string();
        let c = line.remove(0);
        let n: i32 = line.parse().unwrap();
        match c {
            'N' => Ok(Self::N(n)),
            'W' => Ok(Self::W(n)),
            'S' => Ok(Self::S(n)),
            'E' => Ok(Self::E(n)),
            'L' => Ok(Self::L(n)),
            'R' => Ok(Self::R(n)),
            'F' => Ok(Self::F(n)),
            _ => panic!("Unknown command: {}", c),
        }
    }
}

#[doc(hidden)]
fn main() {
    // Read input
    let data: Vec<Command> = fs::read_to_string("input/12.txt")
        .expect("Can't read input data")
        .lines()
        .map(|line| line.trim().parse().unwrap())
        .collect();

    // Solutions
    let i = complex::Complex::i();

    // Part 1
    let mut pos = complex::Complex::new(0, 0);
    let mut dir = complex::Complex::new(1, 0);
    for cmd in &data {
        match cmd {
            Command::N(n) => pos += n * i,
            Command::W(n) => pos -= n,
            Command::S(n) => pos -= n * i,
            Command::E(n) => pos += n,
            Command::L(n) => dir *= i.powi(n / 90),
            Command::R(n) => dir *= i.powi(-n / 90),
            Command::F(n) => pos += n * dir,
        }
    }
    println!("Part 1: {}", pos.l1_norm());

    // Part 2
    let mut pos = complex::Complex::new(0, 0);
    let mut waypoint = complex::Complex::new(10, 1);
    for cmd in &data {
        match cmd {
            Command::N(n) => waypoint += n * i,
            Command::W(n) => waypoint -= n,
            Command::S(n) => waypoint -= n * i,
            Command::E(n) => waypoint += n,
            Command::L(n) => waypoint *= i.powi(n / 90),
            Command::R(n) => waypoint *= i.powi(-n / 90),
            Command::F(n) => pos += n * waypoint,
        }
    }
    println!("Part 2: {}", pos.l1_norm());
}

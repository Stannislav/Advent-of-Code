extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::fs;

#[doc(hidden)]
fn main() {
    // Read input
    let program: Vec<String> = fs::read_to_string("input/14.txt")
        .expect("Can't read input file")
        .lines()
        .map(|line| line.to_string())
        .collect();

    // Solutions
    println!("Part 1: {}", run_program(&program, write_memory_1));
    println!("Part 2: {}", run_program(&program, write_memory_2));
}

type WriteMemoryFn = fn(address: u64, value: u64, mask_str: &str, memory: &mut HashMap<u64, u64>);

/// The main entrypoint for running the input program.
///
/// We loop over the lines in the input and parse the contents with regexes.
/// There are two different types of instructions possible: a `mask` and a
/// `mem` instruction. For example:
///
///     mask = 0000011011111X1001100X0001X1001100X0
///     mem[43805] = 6934
///     mem[57564] = 3741
///
/// If the instruction is `mask`, then we just cache its value. If the
/// instruction is `mem` then we extract the address and the value and call
/// the `write_memory` function that combines them with the cached mask and
/// populates the memory hash map.
///
/// # Arguments
/// * `program` -- The lines of the input file containing the program instructions.
/// * `write_memory` -- The function that writes memory values. This is what differs
///   part 1 from part 2.
///
/// # Returns
/// `u64` -- The sum of all values in the memory hash map after running the program.
fn run_program(program: &[String], write_memeory: WriteMemoryFn) -> u64 {
    let reg_mask = Regex::new(r"mask = (?P<mask_str>[01X]{36})").unwrap();
    let reg_mem = Regex::new(r"mem\[(?P<reg>[0-9]+)\] = (?P<value>[0-9]+)").unwrap();
    let mut memory = HashMap::<u64, u64>::new();
    let mut mask_str = "";
    for line in program.iter() {
        // Match second character: "mask" => 'a', "mem" => 'e'
        match line.chars().nth(1).unwrap() {
            'a' => {
                let caps = reg_mask.captures(line).unwrap();
                mask_str = caps.name("mask_str").unwrap().as_str();
            }
            'e' => {
                let caps = reg_mem.captures(line).unwrap();
                let address: u64 = caps.name("reg").unwrap().as_str().parse().unwrap();
                let value: u64 = caps.name("value").unwrap().as_str().parse().unwrap();
                write_memeory(address, value, mask_str, &mut memory);
            }
            _ => panic!("Parsing error!"),
        }
    }
    memory.values().sum::<u64>()
}

/// Write memory function for part 1.
///
/// The characters in `mask_str` specify which bits should be changed
/// in the given value. The Xs are ignored, while 1s and 0s are set
/// in the same position as found in the mask.
fn write_memory_1(address: u64, value: u64, mask_str: &str, memory: &mut HashMap<u64, u64>) {
    // Mask for setting 0s: turn X into 1 and apply abinary AND, e.g. "10XX10" => `& 101110`
    let and_mask = u64::from_str_radix(&mask_str.replace("X", "1"), 2).unwrap();
    // Mask for settins 1s: turn X into 0 and apply a binary OR, e.g. "10XX10" => `| 100010`
    let or_mask = u64::from_str_radix(&mask_str.replace("X", "0"), 2).unwrap();
    memory.insert(address, value & and_mask | or_mask);
}

/// Write memory function for part 2.
///
/// The characters in `mask_str` specify how to generate new addresses
/// from the given address. The function [`generate_addresses`](generate_addresses)
/// is used to generate these new addresses and the `value` parameter is written
/// to each of them.
fn write_memory_2(address: u64, value: u64, mask_str: &str, memory: &mut HashMap<u64, u64>) {
    for new_address in generate_addresses(address, mask_str) {
        memory.insert(new_address, value);
    }
}

/// Generate new addresses for part 2.
///
/// For all floating bits marked by 'X' in the mask we need to
/// generate all possible substitutions by 0s and 1s into the
/// original address.
fn generate_addresses(address: u64, mask_str: &str) -> Vec<u64> {
    // First set all 1 bits in the mask
    let or_mask = u64::from_str_radix(&mask_str.replace("X", "0"), 2).unwrap();
    let address = address | or_mask;

    // Find positions of all floating bits marked by X, e.g. 0X01X => [3, 0]
    let float_idx = mask_str
        .char_indices()
        .filter(|&(_i, c)| c == 'X')
        .map(|(i, _c)| 35 - i)
        .collect::<Vec<usize>>();

    // Compute the number of all possible combinations of floating bits: 2^(# of Xs)
    let n_float = float_idx.len();
    let n_states: usize = 1 << n_float; // = 2^n_float

    // We can represent all possible combinations of floating bits by bits of integers
    // from 0 to `n_combos`. For example, if mask_str="0X01X" with two floating bits at
    // positions [3, 0] all possible two-bit states are [00, 01, 10, 11], which are binary
    // representations of integers [0, 1, 2, 3]. For each state represented in such a way
    // we need to mask out each bit and to shift it to the position where it needs to be
    // inserted into the address. For 1s we need to apply a bitwise OR, for 0s a bitwise AND.
    let mut addresses: Vec<u64> = Vec::new();
    for state in 0..n_states {
        // We'll build up a new address by applying each bit in the state to the original address
        let mut new_address = address;
        let mut state = state as u64;
        // Strictly speaking `.rev()` is not necessary since we iterate over all combinations anyway
        for &pos in float_idx.iter().rev() {
            // For each floating bit position grab the next bit in the state and apply it to the address
            match state & 1 {
                1 => new_address |= 1 << pos,
                0 => new_address &= !(1 << pos),
                _ => unreachable!(),
            }
            // Shift to the next bit in the state
            state >>= 1;
        }
        addresses.push(new_address);
    }
    addresses
}

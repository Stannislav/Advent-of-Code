use std::collections::HashSet;
use std::fs;
use std::str::FromStr;

/// An intcode command.
#[derive(Debug)]
enum Cmd {
    Nop(isize),
    Acc(isize),
    Jmp(isize),
}

impl FromStr for Cmd {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let cmd_name = &s[..3];
        let x = s[4..].parse().unwrap();
        let cmd = match cmd_name {
            "nop" => Cmd::Nop(x),
            "acc" => Cmd::Acc(x),
            "jmp" => Cmd::Jmp(x),
            _ => panic!("Unknown command: {}", cmd_name),
        };
        Ok(cmd)
    }
}

/// Run intcodes.
///
/// # Arguments
/// * `cmd` -- A sequence of intcode commands
///
/// # Returns
/// * `acc` -- The value of the accumulator
/// * `looped` -- Boolean, true if the program ended in an infinite loop
/// * `seen` -- Indices of commands that were executed.
fn run(cmd: &[Cmd]) -> (isize, bool, HashSet<isize>) {
    let mut acc: isize = 0;
    let mut ptr: isize = 0;
    let mut seen: HashSet<isize> = HashSet::new();
    let mut looped = false;
    while ptr < cmd.len() as isize {
        if seen.contains(&ptr) {
            looped = true;
            break;
        }

        seen.insert(ptr);
        match cmd[ptr as usize] {
            Cmd::Nop(_) => (),
            Cmd::Acc(x) => acc += x,
            Cmd::Jmp(x) => ptr += x - 1,
        }
        ptr += 1;
    }

    (acc, looped, seen)
}

#[doc(hidden)]
fn main() {
    // Read input
    let mut cmd: Vec<Cmd> = fs::read_to_string("input/08.txt")
        .expect("Can't read input file.")
        .trim()
        .lines()
        .map(|s| s.parse::<Cmd>().expect("Can't parse command"))
        .collect();

    // Part 1
    let (acc, _looped, seen) = run(&cmd);
    println!("Part 1: {}", acc);

    // Part 2
    // One of the "jmp" commands that were exectued in part 1 has to
    // be replaced by a "nop" command (or vice versa, which, however,
    // is not the case). Brute-force the solution by trying to replace
    // each of the "jmp" commands in `seen` by `nop` until the code
    // doesn't loop.
    for ptr in seen {
        if let Cmd::Jmp(x) = cmd[ptr as usize] {
            cmd[ptr as usize] = Cmd::Nop(x);
            let (acc, looped, _seen) = run(&cmd);
            if !looped {
                println!("Part 2: {}", acc);
                break;
            }
            // Restore the original "jmp" command.
            cmd[ptr as usize] = Cmd::Jmp(x);
        }
    }
}

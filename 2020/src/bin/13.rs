//! Solutions for day 13.
use std::collections::HashMap;
use std::fs;

#[doc(hidden)]
fn main() {
    // Parse input
    let lines: Vec<String> = fs::read_to_string("input/13.txt")
        .expect("Can't read input")
        .lines()
        .map(|line| line.to_string())
        .collect();
    // First line: timestamp
    let timestamp: usize = lines[0].parse().unwrap();
    // Second line: bus IDs. We don't need the xs, only the bus IDs,
    // and the offsets that they're at. We collect everything in a
    // mapping (offset => bus ID)
    let buses: HashMap<usize, usize> = lines[1]
        .trim()
        .split(',')
        .enumerate()
        .filter(|&(_i, s)| s != "x")
        .map(|(i, s)| (i, s.parse().unwrap()))
        .collect();

    // Part 1
    // We only need bus IDs. These IDs are equal to the time
    // interval between the corresponding bus departures. All buses start
    // driving at t = 0. Which bus is departing the earliest after the
    // given timestamp?
    // For every ID calculate how much wait one has. It can't be bigger than
    // the smallest ID that we have.
    let mut min_id = *(buses.values().min().unwrap());
    let mut min_wait = min_id;
    for &id in buses.values() {
        // (timestamp % id) = t is the last time this bus was seen before the
        // timestamp. Thus it will come again t' = (id - t) minutes after
        // timestamp.
        let wait = id - (timestamp % id);
        if wait < min_wait {
            min_wait = wait;
            min_id = id;
        }
    }
    println!("Part 1: {}", min_wait * min_id);

    // Part 2
    // In principle we can start with t = 0, keep increasing it by dt = 1
    // and check every time if the offsets work out. However, this brute-force solution
    // takes too long.
    // Instead, focus on just one bus, and increate t until we hit the right offset. We
    // would like to keep this offset, so we need to increase t by ID instead of by 1.
    // Consder the next bus and its ID. Generally for the given t the offset will be wrong.
    // We increase t (by the new dt!) until the offset works out also for this bus.
    // Remember that the offset for the first bus doesn't change and stays correct too. To
    // also keep this offset fixed we may only increase t by a certain number of dts. We can
    // compute it manually by simply checking which period the remainders ID % dt have, but
    // because all IDs are prime we know that the period is ID itself. So if we now update
    // dt => dt * ID both offsets will stay fixed. Now proceed in exactly the same way to
    // fix the offsets for the remaining buses. After all offsets have been fixed the
    // timesteps is exactly the solution of the problem.
    let mut timestamp = 0;
    let mut delta = 1;
    for (offset, id) in buses {
        while (timestamp + offset) % id != 0 {
            timestamp += delta;
        }
        // Normally we would need to calculate the cycle length, i.e. how many
        // additions of delta it takes to get back to the same remainder. But because
        // all IDs are prime the cycle is always going to be equal to the ID.
        delta *= id;
    }
    println!("Part 2: {}", timestamp);
}

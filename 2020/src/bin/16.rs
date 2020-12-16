//! Solutions for day 16.
extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::fs;

/// A rule specifies two ranges: `(i, j, k, l)` => `i..=j` and `k..=l`.
type Rule = (u32, u32, u32, u32);

#[doc(hidden)]
fn main() {
    // Parse the 3 blocks of the input: rules for ticket fields, my ticket, nearby tickets
    let data: Vec<String> = fs::read_to_string("input/16.txt")
        .expect("Can't read input")
        .trim()
        .split("\n\n")
        .map(|s| s.to_string())
        .collect();

    // Parse block 1: rules for ticket fields
    let re = Regex::new(r"(?P<name>[a-z\s]+): (?P<s1>\d+)-(?P<e1>\d+) or (?P<s2>\d+)-(?P<e2>\d+)")
        .unwrap();
    let rules: HashMap<String, Rule> = data[0]
        .lines()
        .map(|line| {
            let caps = re.captures(line).unwrap();
            let name = caps["name"].to_string();
            let s1: u32 = caps["s1"].parse().unwrap();
            let e1: u32 = caps["e1"].parse().unwrap();
            let s2: u32 = caps["s2"].parse().unwrap();
            let e2: u32 = caps["e2"].parse().unwrap();
            (name, (s1, e1, s2, e2))
        })
        .collect();

    // Parse block 2: my ticket
    let my_ticket: Vec<u32> = data[1]
        .lines()
        .nth(1)
        .unwrap()
        .split(',')
        .map(|x| x.parse().unwrap())
        .collect();

    // Parse block 3: nearby tickets
    let other_tickets: Vec<Vec<u32>> = data[2]
        .lines()
        .skip(1)
        .map(|line| line.split(',').map(|x| x.parse().unwrap()).collect())
        .collect();

    // Part 1
    let part_1: u32 = other_tickets
        .iter()
        .flat_map(|ticket| ticket.iter().filter(|&&value| !match_any(&value, &rules)))
        .sum();
    println!("Part 1: {:?}", part_1);

    // Discard invalid tickets and add my ticket
    let mut other_tickets: Vec<_> = other_tickets
        .into_iter()
        .filter(|ticket| ticket.iter().all(|value| match_any(&value, &rules)))
        .collect();
    other_tickets.push(my_ticket.clone());

    // Part 2
    let departure_ids = get_departure_ids(&rules, &other_tickets);
    let part_2 = departure_ids
        .iter()
        .fold(1, |acc, &idx| acc * my_ticket[idx] as u64);
    println!("Part 2: {:?}", part_2);
}

/// Check if a value complies with a rule.
///
/// The check is done by verifying if the value is contained
/// in either of the two intervals specified by the rule.
///
/// # Arguments
/// * `value` -- The value to verify.
/// * `(s1, s1, s2, e2)` -- The unpacked bounds of the rule.
///
/// # Return
/// * `bool` -- The result of the check.
fn check_rule(value: &u32, (s1, e1, s2, e2): &Rule) -> bool {
    (s1..=e1).contains(&value) | (s2..=e2).contains(&value)
}

/// Check if a value complies with any of the rules.
///
/// # Arguments
/// * `value` -- The valuel to verify.
/// * `rules` -- A map with rules as values.
///
/// # Return
/// * `bool` -- The result of the check.
fn match_any(value: &u32, rules: &HashMap<String, Rule>) -> bool {
    rules.values().any(|rule| check_rule(&value, rule))
}

/// Determine indices of departure fields.
///
/// This solves part 2 by matching all tickets against
/// the rules that are provided.
///
/// The strategy is to build a cross-table of ticket fields vs. their
/// possible positions. We start by assuming that any field can
/// correspond to any position, so the cross-table is initialized with
/// values `true`. Then we iterate through all tickets and all positions
/// and fill the cross-table. A field can only be in a given position if
/// the values of all tickets at the given position comply with the rule for
/// the given field.
///
/// Once the cross-table is filled not all fields will be assigned to a
/// unique column. We pick those fields for which the assignment is
/// unique - the corresponding positions cannot be taken by other fields.
/// So we update the other fields to include this restriction. This will
/// uniquely determine the position of more fields, and so forth. We
/// iterate this procedure until all positions are determined.
///
/// Once all positions are determined, the problem statement asks to pick
/// only the positions corresponding to fields with names starting with
/// "departure".
///
/// # Arguments
/// * `rules` -- All rules that apply to the ticket fields.
/// * `tickets` -- A sequence of tickets that will be matched against the rules.
///
/// # Return
/// * `Vec<usize>` -- The positions of all fields that start with "departure".
fn get_departure_ids(rules: &HashMap<String, Rule>, tickets: &[Vec<u32>]) -> Vec<usize> {
    let n_fields = rules.len();

    // Initialise cross-table for all ticket fields vs. their possible positions
    let mut cross_table: HashMap<String, Vec<bool>> = HashMap::new();
    for key in rules.keys() {
        cross_table.insert(key.clone(), vec![true; n_fields]);
    }

    // Build ticket field columns = transpose of the vector of tickets
    // This will help filling the cross-table, as we want to fix a position
    // and then iterate through all ticket values at that position.
    let columns: Vec<Vec<u32>> = (0..n_fields)
        .map(|i| tickets.iter().map(|ticket| ticket[i]).collect())
        .collect();

    // Fill cross-table
    for (i, column) in columns.iter().enumerate() {
        for (key, rule) in rules.iter() {
            cross_table.get_mut(key).unwrap()[i] =
                column.iter().all(|value| check_rule(value, rule));
        }
    }

    // Iteratively assign positions that are uniquely determined, i.e. only have one true entry.
    // The map `field_positions` saves the fields for which the position has been determined.
    let mut field_positions: HashMap<String, usize> = HashMap::new();
    let mut finished = false;
    while !finished {
        finished = true;
        for field in rules.keys() {
            // Does this field have only one single position that it could correspond to?
            if cross_table[field].iter().filter(|&&x| x).count() == 1 {
                // Get the index of the single `true` entry
                let idx = cross_table[field].iter().position(|&x| x).unwrap();
                // Save the found position for this field
                field_positions.insert(field.clone(), idx);
                // All other fields cannot be in this position any more
                for other_field in rules.keys().filter(|&key| key != field) {
                    cross_table.get_mut(other_field).unwrap()[idx] = false;
                }
            } else {
                // We found at least one field that was not uniquely determined,
                // so we're not finished yet.
                finished = false;
            }
        }
    }

    // Extract the positions of all fields that start with "departure"
    field_positions
        .iter()
        .filter(|&(key, _idx)| key.starts_with("departure"))
        .map(|(_key, &idx)| idx)
        .collect()
}

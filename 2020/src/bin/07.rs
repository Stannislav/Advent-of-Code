extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::collections::HashSet;
use std::fs;

// Represent the bag nesting using a graph
type Edge = (String, usize); // (target_node, weight)
type Graph = HashMap<String, HashSet<Edge>>; // (node, outgoing_edges)

/// Parse the input lines
///
/// # Arguments
/// `lines` -- The lines from the input file
///
/// # Returns
/// `parents` -- The directed graph representing the parent bags
/// `children` -- The directed graph representing the child bags
fn parse_input(lines: &[String]) -> (Graph, Graph) {
    let mut parents = Graph::new();
    let mut children = Graph::new();

    // Regex for parsing the second part of the sentences, e.g.
    // "5 dull maroon bags, 1 shiny olive bag.".
    let re = Regex::new(r"(?P<n>\d)+ (?P<color>[a-z ]+) bag[s]?[,|.]").expect("Bad regex");

    for line in lines.iter() {
        let pair: Vec<&str> = line.split(" bags contain ").collect();
        let parent_color = &pair[0].to_string();
        // Extract all children with the regex.
        for caps in re.captures_iter(pair[1]) {
            let n = (&caps["n"]).parse::<usize>().unwrap();
            let child_color = &caps["color"];
            children
                .entry(parent_color.to_string())
                .or_insert_with(HashSet::new)
                .insert((child_color.to_string(), n));
            parents
                .entry(child_color.to_string())
                .or_insert_with(HashSet::new)
                .insert((parent_color.to_string(), 1)); // Always one of each parent type
        }
    }
    (parents, children)
}

/// Find how many different bags can contain a given color.
///
/// This solves part 1 of the problem by running it on "shiny gold".
///
/// The strategy is to recursively walk up the parent graph and to
/// remember all parent colors seen. If a parent colors has alrady
/// been reached by a different path, then it doesn't need to be
/// considered again.
///
/// # Arguments
/// * `graph` -- The graph of parent bags.
/// * `color` -- The bag color for which to find all possible parents.
/// * `different_colors` -- A mutable set where all different parents will be stored.
fn outside_colors(graph: &Graph, color: &str, different_colors: &mut HashSet<String>) {
    if let Some(edges) = graph.get(color) {
        for (parent_color, _n) in edges.iter() {
            if !different_colors.contains(parent_color) {
                // Only consider this parent if we haven't seen it
                different_colors.insert(parent_color.clone());
                outside_colors(graph, parent_color, different_colors);
            }
        }
    }
}

/// Find the cumulative sum of all child bags in a given one.
///
/// This solves part 1 of the problem by running it on "shiny gold".
///
/// We descend down the child graph recursively and sum up all its children.
/// Note that unlike in `outside_colors` we cannot prune away children that
/// have already been visited because we must double count them if they were
/// reached by a different path. At this point memoization could be applied
/// to avoid re-computing a branch that has been visited (not done here).
///
/// # Arguments
/// * `graph` -- The graph of children bags.
/// * `color` -- The bag color for which to find the sum of all children.
///
/// # Returns
/// * `usize` -- The cumulative sum of all child bags.
fn traverse_count(graph: &Graph, color: &str) -> usize {
    // If bag A has 5 bags B, then the cumulative number of bags
    // in A is 5 x (B + children in B), this is where the expression
    // n * (1 + traverse_count(...)) comes from.
    match graph.get(color) {
        None => 0,
        Some(edges) => edges
            .iter()
            .map(|(child_color, n)| n * (1 + traverse_count(graph, child_color)))
            .sum(),
    }
}

#[doc(hidden)]
fn main() {
    // Read input
    let lines: Vec<String> = fs::read_to_string("input/07.txt")
        .expect("Can't read input file")
        .trim()
        .lines()
        .map(|line| line.to_string())
        .collect();

    // Parse input
    let (parents, children) = parse_input(&lines);

    // Part 1
    let mut all_outside_colors = HashSet::<String>::new();
    outside_colors(&parents, "shiny gold", &mut all_outside_colors);
    println!("Part 1: {}", all_outside_colors.len());

    // Part 2
    println!("Part 2: {}", traverse_count(&children, "shiny gold"));
}

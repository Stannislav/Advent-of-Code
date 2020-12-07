extern crate regex;

use regex::Regex;
use std::collections::HashMap;
use std::collections::HashSet;
use std::fs;

type Edge = (String, usize); // (target_node, weight)
type Graph = HashMap<String, HashSet<Edge>>; // (node, outgoing_edges)

fn parse_input(lines: &[String]) -> (Graph, Graph) {
    let mut parents = Graph::new();
    let mut children = Graph::new();
    let re = Regex::new(r"(?P<n>\d)+ (?P<color>[a-z ]+) bag[s]?[,|.]").expect("Bad regex");

    for line in lines.iter() {
        let pair: Vec<&str> = line.split(" bags contain ").collect();
        let parent_color = &pair[0].to_string();
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
                .insert((parent_color.to_string(), 1));
        }
    }
    (parents, children)
}

fn traverse_count(graph: &Graph, color: &str) -> usize {
    match graph.get(color) {
        None => 0,
        Some(edges) => edges
            .iter()
            .map(|(child_color, n)| n * (1 + traverse_count(graph, child_color)))
            .sum(),
    }
}

fn outside_colors(graph: &Graph, color: &str, different_colors: &mut HashSet<String>) {
    if let Some(edges) = graph.get(color) {
        for (parent_color, _n) in edges.iter() {
            if !different_colors.contains(parent_color) {
                different_colors.insert(parent_color.clone());
                outside_colors(graph, parent_color, different_colors);
            }
        }
    }
}

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

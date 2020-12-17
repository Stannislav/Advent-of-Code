//! Solutions for day 17.
use std::collections::HashSet;
use std::fs;

/// A set of points representing the state of the game.
type Points = HashSet<Vec<i32>>;

#[doc(hidden)]
fn main() {
    // Read input
    let data: Vec<Vec<char>> = fs::read_to_string("input/17.txt")
        .expect("Can't read input")
        .lines()
        .map(|line| line.chars().collect())
        .collect();

    let mut points: Points = HashSet::new();
    for (i, line) in data.iter().enumerate() {
        for (j, &c) in line.iter().enumerate() {
            if c == '#' {
                points.insert(vec![i as i32, j as i32, 0]);
            }
        }
    }

    // Part 1
    println!("Part 1: {}", run(&points).iter().count());

    // Part 2
    let mut new_points: Points = HashSet::new();
    for point in points.iter() {
        let mut point = point.clone();
        point.push(0);
        new_points.insert(point);
    }
    println!("Part 2: {}", run(&new_points).iter().count());
}

/// Run the game for 6 iterations.
///
/// # Arguments
/// * `points` -- The initial active points.
///
/// # Returns
/// `points` -- The active points after 6 iterations.
fn run(points: &Points) -> Points {
    let mut points = points.clone();
    let n_dim = points.iter().next().unwrap().len();
    let neighbour_deltas: HashSet<Vec<i32>> = get_neighbour_deltas(n_dim);

    for _ in 0..6 {
        points = step(&points, &neighbour_deltas);
    }
    points
}

/// One step in the game execution.
///
/// # Arguments
/// * `points` -- The current active points.
/// * `neighbour_deltas` -- The cached neighbour deltas. These should
///   be computed wiht [`get_neighbour_deltas`](get_neighbour_deltas).
///
/// # Returns
/// `new_points` -- The active points after the step.
fn step(points: &Points, neighbour_deltas: &HashSet<Vec<i32>>) -> Points {
    let mut new_points: Points = HashSet::new();
    let mut inactive_candidates: Points = HashSet::new();

    // Which active cubes become inactive?
    for point in points.iter() {
        let neighbours = get_neighbours(point, neighbour_deltas);
        // Decide if active cube becomes inactive
        match neighbours.iter().filter(|&p| points.contains(p)).count() {
            2 => new_points.insert(point.clone()),
            3 => new_points.insert(point.clone()),
            _ => false,
        };
        // Extract inactivte neighbours
        inactive_candidates.extend(neighbours.iter().filter(|&p| !points.contains(p)).cloned());
    }

    // Which inactive cubes become activte?
    for point in inactive_candidates.iter() {
        let neighbours = get_neighbours(point, neighbour_deltas);
        // Decide if active cube becomes inactive
        match neighbours.iter().filter(|&p| points.contains(p)).count() {
            3 => new_points.insert(point.clone()),
            _ => false,
        };
    }
    new_points
}

/// Get all neighbours of a point.
///
/// The neighbours of the given point are obtained by adding
/// delta vectors to it. These delta vectors need to be pre-
/// computed using the [`get_neighbour_deltas`](get_neighbour_deltas)
/// function and their dimension must match that of the point.
///
/// # Arguments
/// * `point` -- The input point.
/// * `neighbour_deltas` -- The delta vectors for all neighbours. These should
///   be computed wiht [`get_neighbour_deltas`](get_neighbour_deltas).
///
/// # Returns
/// `neighbours` -- The set of all neighbours of `point`.
fn get_neighbours(point: &[i32], neighbour_deltas: &HashSet<Vec<i32>>) -> HashSet<Vec<i32>> {
    neighbour_deltas
        .iter()
        .map(|delta| point.iter().zip(delta).map(|(x1, x2)| x1 + x2).collect())
        .collect()
}

/// Pre-compute all neighbour deltas in a given dimension.
///
/// The neighbours of a point are all points where any number
/// of coordinates differ from those of `point` by exactly one.
///
/// All neighbour deltas can be obtained by starting with a zero
/// vector and by iteratively mutating its entries by +1 and -1.
/// We have to keep track of the entries which have already been
/// mutated, so that each entry is mutated at most once.
///
/// # Arguments
/// * `n_dim` -- The dimension of space in which the points live.
///
/// # Returns
/// `neighbour_deltas` -- The set of all neighbour deltas.
fn get_neighbour_deltas(n_dim: usize) -> HashSet<Vec<i32>> {
    // The final set of neighbour deltas.
    let mut neighbour_deltas: HashSet<Vec<i32>> = HashSet::new();
    // The candidates for mutation. The second boolean vector determines which
    // coordinates of the given delta vector have not been mutated yet.
    let mut to_mutate: HashSet<(Vec<i32>, Vec<bool>)> = HashSet::new();
    // Start with a zero vector.
    to_mutate.insert((vec![0; n_dim], vec![true; n_dim]));

    for _ in 0..n_dim {
        // Mutations will produce a new set of candidates for mutations.
        let mut new_to_mutate: HashSet<(Vec<i32>, Vec<bool>)> = HashSet::new();
        for (delta, flags) in to_mutate.iter() {
            // In the given delta iterate over all coordinates that have not
            // been mutated yet.
            for (mod_idx, _flag) in flags.iter().enumerate().filter(|(_i, &flag)| flag) {
                // Mutate the delta by +1 and -1 and update the boolean flags vector.
                let mut new_flags = flags.clone();
                let mut delta_plus = delta.clone();
                let mut delta_minus = delta.clone();
                new_flags[mod_idx] = false;
                delta_plus[mod_idx] += 1;
                delta_minus[mod_idx] -= 1;
                // We'll try to mutate the new deltas in the next round.
                new_to_mutate.insert((delta_plus.clone(), new_flags.clone()));
                new_to_mutate.insert((delta_minus.clone(), new_flags.clone()));
                // The deltas produced in this step are already valid neighbour deltas.
                neighbour_deltas.insert(delta_plus);
                neighbour_deltas.insert(delta_minus);
            }
        }
        to_mutate = new_to_mutate;
    }
    neighbour_deltas
}

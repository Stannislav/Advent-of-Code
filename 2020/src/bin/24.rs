//! Solutions for day 24.
use std::cmp::min;
use std::collections::HashSet;
use std::fs;
use std::str::FromStr;

/// Representation of a single tile and its coordinates
#[derive(Copy, Clone, Debug, Eq, PartialEq, Hash)]
struct Tile {
    e: usize,
    w: usize,
    ne: usize,
    nw: usize,
    se: usize,
    sw: usize,
}

/// Transform coordinates on a hexagonal grid.
///
/// If x1 + x2 = y, then replace x1 + x2 by y as many times as
/// possible until one of the components x1 or x2 is eliminated.
///
/// Assume that for 3 coordinates coordinatex X1, X2, and Y it holds that
/// X1 + X2 = Y. This can be written in component form as (1, 1, 0) = (0, 0, 1).
/// Thus given a point with components (x1, x2, y) we can eliminate one
/// coordinate by writing it as (0, x2 - x1, y + x1)
///
/// # Arguments
/// * `x1`: The first coordinate.
/// * `x2`: The second coordinate.
/// * `y`: The third coordinate for which `x1 + x2 = y` holds. If x1 + x2 = 0
///   then `y` should be set to `None`.
///
/// # Returns
/// * `change`: The number of times `x1 + x2 = y` was applied.
fn reduce(x1: &mut usize, x2: &mut usize, y: Option<&mut usize>) -> usize {
    // This makes sure that the components stay positive.
    let change = min(*x1, *x2);
    *x1 -= change;
    *x2 -= change;
    if let Some(y) = y {
        *y += change;
    }
    change
}

// Instance methods of Tile
impl Tile {
    /// Get all neighboring tiles.
    ///
    /// # Returns
    /// `Vec<Self>`: The neighboring tiles.
    fn neighbors(&self) -> Vec<Self> {
        let mut neighbors = vec![
            Tile {
                e: self.e + 1,
                ..*self
            },
            Tile {
                w: self.w + 1,
                ..*self
            },
            Tile {
                ne: self.ne + 1,
                ..*self
            },
            Tile {
                nw: self.nw + 1,
                ..*self
            },
            Tile {
                se: self.se + 1,
                ..*self
            },
            Tile {
                sw: self.sw + 1,
                ..*self
            },
        ];
        for neighbor in &mut neighbors {
            neighbor.simplify();
        }
        neighbors
    }

    /// Simplify the coordinates of a tile on a hexagonal grid.
    ///
    /// We parametrize the two-simensional space by 6 positive coordinates,
    /// so the coordinates of any point on the hexagonal grid are not unique.
    /// They can be made unique by minimizing the total number of coordinate
    /// components. For example, moving two steps to the east and one to the
    /// west shall be replaced by one step to the east, and so forth.
    ///
    /// There are are number of relationships that hold between different
    /// coordinate components, which can be used to simplify the coordinates
    /// of a given tile. We keep applying these rules until no further
    /// simplification can be made.
    fn simplify(&mut self) {
        loop {
            // Track if any changes have been made in this iteration.
            let mut change: usize = 0;
            // 1. Simplify vertical moves by applying
            //    se + ne = e
            //    sw + nw = w
            change += reduce(&mut self.se, &mut self.ne, Some(&mut self.e));
            change += reduce(&mut self.sw, &mut self.nw, Some(&mut self.w));

            // 2. Simplify horizontal moves by applying e + w = 0
            change += reduce(&mut self.e, &mut self.w, None);

            // 3. Remove unnecessary horizontal moves by applying
            //    e + sw = se
            //    e + nw = ne
            //    w + se = sw
            //    w + ne = nw
            change += reduce(&mut self.e, &mut self.sw, Some(&mut self.se));
            change += reduce(&mut self.e, &mut self.nw, Some(&mut self.ne));
            change += reduce(&mut self.w, &mut self.se, Some(&mut self.sw));
            change += reduce(&mut self.w, &mut self.ne, Some(&mut self.nw));

            // 4. Simplify diagonal moves by applying
            //    ne + sw = 0
            //    nw + se = 0
            change += reduce(&mut self.ne, &mut self.sw, None);
            change += reduce(&mut self.nw, &mut self.se, None);

            if change == 0 {
                break;
            }
        }
    }
}

impl FromStr for Tile {
    type Err = ();

    /// Parse a tile from the input string.
    ///
    /// # Arguments
    /// `s`: The input string.
    ///
    /// # Returns
    /// `Result<Self, Self::Err>`: The parsed tile.
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut tile = Tile {
            e: 0,
            w: 0,
            ne: 0,
            nw: 0,
            se: 0,
            sw: 0,
        };
        // For two-character coordinates (ne, nw, se, sw) remember
        // the prefix (n, s, or no prefix for simple coordinates e and w)
        let mut prefix: i8 = 0; // 1 = n, -1 = s
        for c in s.trim().chars() {
            match c {
                'n' => prefix = 1,
                's' => prefix = -1,
                'e' => {
                    match prefix {
                        1 => tile.ne += 1,
                        -1 => tile.se += 1,
                        0 => tile.e += 1,
                        _ => unreachable!(),
                    }
                    prefix = 0;
                }
                'w' => {
                    match prefix {
                        1 => tile.nw += 1,
                        -1 => tile.sw += 1,
                        0 => tile.w += 1,
                        _ => unreachable!(),
                    }
                    prefix = 0;
                }
                _ => unreachable!(),
            }
        }
        tile.simplify();
        Ok(tile)
    }
}

/// Perform the daily tile flip from part 2.
///
/// # Arguments
/// `black_tiles`: The state of the floor before the flip.
///
/// # Returns
/// `new_black_tiles`: The state of the floor after the flip.
fn step(black_tiles: HashSet<Tile>) -> HashSet<Tile> {
    let mut new_black_tiles: HashSet<Tile> = HashSet::new();
    // By looking at all neighbors of black tiles we'll find all white
    // tiles that have some black tiles as neighbors.
    let mut white_tiles: HashSet<Tile> = HashSet::new();
    // Apply the flipping rule to the black tiles.
    for tile in black_tiles.iter() {
        let mut n_black: usize = 0;
        for neighbor in tile.neighbors() {
            if black_tiles.contains(&neighbor) {
                n_black += 1;
            } else {
                white_tiles.insert(neighbor);
            }
        }
        if n_black > 0 && n_black <= 2 {
            new_black_tiles.insert(*tile);
        }
    }
    // Apply the flipping rule to the white tiles.
    for tile in white_tiles {
        let mut n_black = 0;
        for neighbor in tile.neighbors() {
            if black_tiles.contains(&neighbor) {
                n_black += 1;
            }
        }
        if n_black == 2 {
            new_black_tiles.insert(tile);
        }
    }
    new_black_tiles
}

#[doc(hidden)]
fn main() {
    // Read input
    let tiles: Vec<Tile> = fs::read_to_string("input/24.txt")
        .expect("Can't read input")
        .trim()
        .lines()
        .map(|line| line.parse().unwrap())
        .collect();

    // Part 1
    // Initally all tiles are white, we don't need to store them.
    // Each line in the input stands for a tile flip. If a tile becomes
    // black we add it to the set, if it becomes white we remove it from the
    // set.
    let mut black_tiles: HashSet<Tile> = HashSet::new();
    for tile in &tiles {
        if !black_tiles.remove(tile) {
            black_tiles.insert(*tile);
        }
    }
    println!("Part 1: {}", black_tiles.len());

    // Part 2
    for _day in 1..=100 {
        black_tiles = step(black_tiles);
    }
    println!("Part 2: {}", black_tiles.len());
}

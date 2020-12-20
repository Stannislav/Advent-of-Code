use std::collections::HashMap;
use std::fmt;
use std::fs;
use std::str::FromStr;

#[derive(Debug, Clone)]
struct Tile {
    id: usize,
    rows: Vec<String>,
    edges: Vec<(String, String)>,
}

// Class methods
impl Tile {
    fn make_tile(id: usize, rows: &Vec<String>) -> Self {
        let mut edges: Vec<(String, String)> = Vec::new();

        let edge_top = rows[0].to_string();
        let edge_right: String = rows
            .iter()
            .map(|line| line.chars().rev().next().unwrap())
            .collect();
        let edge_bottom = rows.iter().rev().next().unwrap().chars().rev().collect();
        let edge_left: String = rows
            .iter()
            .rev()
            .map(|line| line.chars().next().unwrap())
            .collect();
        for edge in [edge_top, edge_right, edge_bottom, edge_left].iter() {
            // (edge, edge_when_flipped)
            edges.push((edge.to_string(), edge.chars().rev().collect()));
        }

        Self {
            id,
            rows: rows.to_owned(),
            edges,
        }
    }
}

// Instance methods
impl Tile {
    fn flip(&self) -> Tile {
        let rows = (0..self.rows[0].len())
            .map(|i| {
                self.rows
                    .iter()
                    .map(|row| row.chars().nth(i).unwrap())
                    .collect()
            })
            .collect();

        Tile::make_tile(self.id, &rows)
    }

    // Rotate clockwise
    fn rotate(&self, n: isize) -> Tile {
        // a.rem_euclid(b) = a % b with b > 0
        let rows = match n.rem_euclid(4) {
            0 => self.rows.clone(),
            1 => (0..self.rows[0].len())
                .map(|i| {
                    self.rows
                        .iter()
                        .rev()
                        .map(|row| row.chars().nth(i).unwrap())
                        .collect()
                })
                .collect(),
            2 => self
                .rows
                .iter()
                .rev()
                .map(|row| row.chars().rev().collect::<String>())
                .collect(),
            3 => (0..self.rows[0].len())
                .map(|i| {
                    self.rows
                        .iter()
                        .map(|row| row.chars().rev().nth(i).unwrap())
                        .collect()
                })
                .collect(),
            _ => unreachable!(),
        };
        Tile::make_tile(self.id, &rows)
    }
}

impl FromStr for Tile {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let id = s[5..9].to_string().parse().unwrap();
        let rows: Vec<String> = s.lines().skip(1).map(|line| line.to_string()).collect();
        let tile = Tile::make_tile(id, &rows);
        Ok(tile)
    }
}

fn count_adjacent(tile: &Tile, tiles: &HashMap<usize, Tile>) -> usize {
    tile.edges
        .iter()
        .filter(|edge| find_matching(tile.id, &edge.0, tiles).is_some())
        .count()
}

impl fmt::Display for Tile {
    fn fmt(&self, formatter: &mut fmt::Formatter) -> fmt::Result {
        writeln!(formatter, "ID = {}", self.id)?;
        for row in self.rows.iter() {
            writeln!(
                formatter,
                "{}",
                row.chars()
                    .map(|c| c.to_string())
                    .collect::<Vec<String>>()
                    .join(" ")
            )?;
        }
        Ok(())
    }
}

fn find_matching(
    id: usize,
    edge: &str,
    tiles: &HashMap<usize, Tile>,
) -> Option<(usize, usize, bool)> {
    for (&other_id, other_tile) in tiles.iter() {
        if id == other_id {
            continue;
        }
        for (i, other_edge) in other_tile.edges.iter().enumerate() {
            if edge == other_edge.0 {
                return Some((other_id, i, true));
            }
            if edge == other_edge.1 {
                return Some((other_id, i, false));
            }
        }
    }
    None
}

fn main() {
    let mut tiles: HashMap<usize, Tile> = fs::read_to_string("input/20.txt")
        .expect("Can't read input file")
        .trim()
        .split("\n\n")
        .map(|block| {
            let tile: Tile = block.parse().unwrap();
            (tile.id, tile)
        })
        .collect();
    // Part 1
    // - Interpret the edges of tiles a binary representation of integer
    // - All integers should pair up except for those at the borders
    // - The 4 corner tiles will have 2 edges that don't pair up
    let corner_ids: Vec<usize> = tiles
        .keys()
        .filter(|key| count_adjacent(tiles.get(key).unwrap(), &tiles) == 2)
        .cloned()
        .collect();
    assert_eq!(corner_ids.len(), 4);
    let part_1 = corner_ids.iter().fold(1, |acc, id| acc * id);
    println!("Part 1: {}", part_1);

    // Part 2
    // Start with one corner and assume it's top left. Rotate it appropriately
    // so that the top and the left sides don't have matching tiles.
    let mut corner_tile = tiles.get(&corner_ids[0]).unwrap().clone();

    // Rotate the corner tile: need edges 1 and 2 to match
    while !(find_matching(corner_tile.id, &corner_tile.edges[1].0, &tiles).is_some()
        && find_matching(corner_tile.id, &corner_tile.edges[2].0, &tiles).is_some())
    {
        corner_tile = corner_tile.rotate(1);
    }
    tiles.insert(corner_tile.id, corner_tile.clone());

    // Find matching tiles row by row
    let mut current_tile = corner_tile;
    let mut grid: Vec<Vec<usize>> = Vec::new();
    let mut current_row: Vec<usize> = vec![current_tile.id];
    loop {
        match find_matching(current_tile.id, &current_tile.edges[1].0, &tiles) {
            // There's another tile to the right
            Some((next_id, next_edge_id, need_flip)) => {
                let mut next_tile = tiles.get(&next_id).unwrap().clone();
                let mut next_edge_id = next_edge_id;
                if need_flip {
                    next_tile = next_tile.flip();
                    next_edge_id = 3 - next_edge_id;
                }
                next_tile = next_tile.rotate(3 - next_edge_id as isize);
                // println!("\ninserted:");
                // print_tile(&next_tile, &tiles);
                tiles.insert(next_tile.id, next_tile.clone());
                // println!("{:?}", find_matching(current_tile.id, &current_tile.edges[1].0, &tiles).unwrap());
                current_row.push(next_tile.id);
                current_tile = next_tile.clone();
            }
            // No more tiles to the right, proceed to the next row
            None => {
                let first_tile = tiles.get(&current_row[0]).unwrap();
                grid.push(current_row);
                current_row = Vec::new();
                match find_matching(first_tile.id, &first_tile.edges[2].0, &tiles) {
                    Some((next_id, next_edge_id, need_flip)) => {
                        let mut next_tile = tiles.get(&next_id).unwrap().clone();
                        let mut next_edge_id = next_edge_id;
                        if need_flip {
                            next_tile = next_tile.flip();
                            next_edge_id = 3 - next_edge_id;
                        }
                        next_tile = next_tile.rotate(0 - next_edge_id as isize);
                        tiles.insert(next_tile.id, next_tile.clone());
                        current_row.push(next_tile.id);
                        current_tile = next_tile.clone();
                    }
                    // No more rows below, we're done;
                    None => break,
                }
            }
        }
    }

    // Convert grid to a vector of strings
    let grid: Vec<String> = grid.iter().flat_map(|row| join_row(row, &tiles)).collect();

    // Construct the tile representing the monster
    let monster_vec = &vec![
        "                  # ".to_string(),
        "#    ##    ##    ###".to_string(),
        " #  #  #  #  #  #   ".to_string(),
    ];
    let mut monster = Tile::make_tile(0, monster_vec);

    // Initialize the counts.
    let water_count: usize = grid
        .iter()
        .map(|row| row.chars().filter(|&c| c == '#').count())
        .sum();
    let mut monster_tile_count: usize = 0;

    // Search for monsters in all possible rotations and flips
    for _rot in 0..4 {
        monster_tile_count += count_monsters(&monster, &grid);
        monster = monster.rotate(1);
    }
    monster = monster.flip();
    for _rot in 0..4 {
        monster_tile_count += count_monsters(&monster, &grid);
        monster = monster.rotate(1);
    }

    println!(
        "Part 2: {}",
        water_count as isize - monster_tile_count as isize
    );
}

fn join_row(row: &Vec<usize>, tiles: &HashMap<usize, Tile>) -> Vec<String> {
    // number for rows (and columns) in a tile
    let size: usize = tiles.get(&row[0]).unwrap().rows.len();
    (1..size - 1)
        .map(|i| {
            // each i has to produce a String
            row.iter()
                .map(|id| tiles.get(&id).unwrap().rows[i][1..size - 1].to_string())
                .collect::<Vec<String>>()
                .join("")
        })
        .collect()
}

fn convolve(v1: &Vec<String>, v2: &Vec<String>) -> usize {
    v1.iter()
        .zip(v2)
        .map(|(r1, r2)| {
            r1.chars()
                .zip(r2.chars())
                .filter(|&(c1, c2)| c1 == '#' && c1 == c2)
                .count()
        })
        .sum()
}

fn count_monsters(monster: &Tile, grid: &Vec<String>) -> usize {
    let h = monster.rows.len();
    let w = monster.rows[0].len();
    // Number of '#' characters in the monster tile.
    let target: usize = monster
        .rows
        .iter()
        .map(|row| row.chars().filter(|&c| c == '#').count())
        .sum();

    let mut count = 0;
    for i in 0..grid.len() - h + 1 {
        for j in 0..grid[0].len() - w + 1 {
            let sub: Vec<String> = grid[i..i + h]
                .iter()
                .map(|row| row[j..j + w].to_string())
                .collect();
            if convolve(&monster.rows, &sub) == target {
                count += 1;
            }
        }
    }
    count * target
}

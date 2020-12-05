use std::fs;

/// Traverse the terrain and count the trees.
///
/// # Arguments
/// * `lines`: The lines of the input file.
/// * `dw`: The horizontal component of the slope.
/// * `dh`: The vertical component of the slope.
///
/// # Description
/// Starting at coordinates `(0, 0)` step through the terrain by
/// doing steps of the form `(h, w) -> (h + dh, w + dw)` while wrapping
/// around the horizontal (`w`) axis. Record the number of trees marked
/// by `'#'` that are encountered along the trajectory.
fn toboggan_travel(lines: &Vec<Vec<char>>, dw: usize, dh: usize) -> usize {
    let (h_max, w_max) = (lines.len(), lines[0].len());
    let (mut h, mut w) = (0, 0);
    let mut count = 0;
    while h < h_max {
        if lines[h][w] == '#' {
            count += 1;
        }
        w = (w + dw) % w_max;
        h = h + dh;
    }

    count
}

#[doc(hidden)]
fn main() {
    // Read input
    let lines: Vec<Vec<char>> = fs::read_to_string("input/03.txt")
        .expect("Can't read input file")
        .lines()
        .map(|x| x.chars().collect())
        .collect();

    // Part 1
    let part_1 = toboggan_travel(&lines, 3, 1);
    println!("Part 1: {}", part_1);

    // Part 2
    let mut part_2 = 1;
    for (dw, dh) in vec![(1, 1), (3, 1), (5, 1), (7, 1), (1, 2)].iter() {
        part_2 *= toboggan_travel(&lines, *dw, *dh);
    }
    println!("Part 2: {}", part_2);
}

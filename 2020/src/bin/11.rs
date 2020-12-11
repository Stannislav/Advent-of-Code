use std::fs;

fn main() {
    // Read input
    let mut data: Vec<Vec<char>> = fs::read_to_string("input/11.txt")
        .expect("Can't read input file")
        .lines()
        .map(|x| x.chars().collect())
        .collect();

    // let mut data: Vec<Vec<char>> = {
    // "L.LL.LL.LL
    // LLLLLLL.LL
    // L.L.L..L..
    // LLLL.LL.LL
    // L.LL.LL.LL
    // L.LLLLL.LL
    // ..L.L.....
    // LLLLLLLLLL
    // L.LLLLLL.L
    // L.LLLLL.LL"
    // .lines()
    // .map(|x| x.trim().chars().collect())
    // .collect()
    // };

    // println!("{:#?}", data);

    // Add padding
    for line in &mut data {
        line.insert(0, 'x');
        line.push('x');
    }
    data.insert(0, vec!['x'; data[0].len()]);
    data.push(vec!['x'; data[0].len()]);
    // show(&data);

    // Solutions
    println!("Part 1: {}", run_simulation(&data, get_neighbours_1, 4));
    println!("Part 2: {}", run_simulation(&data, get_neighbours_2, 5));
}

type NeighbourFn = fn(&[Vec<char>], i: usize, j: usize) -> Vec<char>;

fn run_simulation(data: &[Vec<char>], get_neighbours: NeighbourFn, min_occupied: usize) -> usize {
    let mut data: Vec<Vec<char>> = data.iter().map(|line| line.clone()).collect();
    let mut n_changed = 999;
    while n_changed != 0 {
        let (data_new, n_changed_new) = step(&data, get_neighbours, min_occupied);
        data = data_new;
        n_changed = n_changed_new;
    }
    count_occupied(&data)
}

fn count_occupied(data: &[Vec<char>]) -> usize {
    data.into_iter()
        .map(|line| line.iter().filter(|&&c| c == '#').count())
        .sum()
}

// fn show(data: &[Vec<char>]) {
//     for line in data.iter() {
//         println!("{}", line.iter().collect::<String>());
//     }
// }

fn get_neighbours_1(data: &[Vec<char>], i: usize, j: usize) -> Vec<char> {
    let n: Vec<char> = vec![
        (i - 1, j),
        (i + 1, j),
        (i, j - 1),
        (i, j + 1),
        (i - 1, j - 1),
        (i + 1, j - 1),
        (i - 1, j + 1),
        (i + 1, j + 1),
    ]
    .iter()
    .map(|&(ni, nj)| data[ni][nj])
    .collect();
    n
}

fn get_neighbours_2(data: &[Vec<char>], i: usize, j: usize) -> Vec<char> {
    let mut neighbours: Vec<char> = Vec::new();
    let deltas: Vec<(isize, isize)> = vec![
        (-1, 0),
        (1, 0),
        (0, -1),
        (0, 1),
        (-1, -1),
        (1, -1),
        (-1, 1),
        (1, 1),
    ];
    for (di, dj) in deltas {
        let (mut ci, mut cj): (isize, isize) = (i as isize + di, j as isize + dj);
        while data[ci as usize][cj as usize] == '.' {
            ci += di;
            cj += dj;
        }
        neighbours.push(data[ci as usize][cj as usize]);
    }
    neighbours
}

fn step(
    data: &[Vec<char>],
    get_neighbours: NeighbourFn,
    min_occupied: usize,
) -> (Vec<Vec<char>>, usize) {
    let mut data_new: Vec<Vec<char>> = data.iter().map(|line| vec!['.'; line.len()]).collect();
    let mut n_changed = 0;

    for (i, line) in data.iter().enumerate() {
        for (j, c) in line.iter().enumerate() {
            data_new[i][j] = match c {
                'L' => {
                    if get_neighbours(data, i, j).iter().all(|&c| c != '#') {
                        n_changed += 1;
                        '#'
                    } else {
                        'L'
                    }
                }
                '#' => {
                    if get_neighbours(data, i, j)
                        .iter()
                        .filter(|&&c| c == '#')
                        .count()
                        >= min_occupied
                    {
                        data_new[i][j] = 'L';
                        n_changed += 1;
                        'L'
                    } else {
                        '#'
                    }
                }
                _ => *c,
            }
        }
    }
    (data_new, n_changed)
}

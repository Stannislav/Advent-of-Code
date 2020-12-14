# :christmas_tree: Advent of Code 2020 :christmas_tree:

- Web: [AoC](https://adventofcode.com/2020)
- Discussions: [Reddit](https://www.reddit.com/r/adventofcode/)
- Language: [Rust](https://www.rust-lang.org/)

## How to Run
Run
```bash
$ cargo run --release --bin <xx>
```
and replace `<xx>` by the day number, e.g. `05`, or `17`.

Some solutions use new Rust features not yet available in the `stable` release.
They can be recognized by the following error:
```bash
error[E0554]: `#![feature]` may not be used on the stable release channel
```
If you see this error you need compile using the `nightly` toolchain of Rust:
```bash
$ cargo +nightly run --release --bin <xx>
```

## Solutions
The last column contains improved solutions inspired by solutions of other people.

| Day | Problem | Solution | Improved Solution |
|----:|:--------|:---------|:------------------|
|   1 | [Report Repair](https://adventofcode.com/2020/day/1) | [src/bin/01.rs](src/bin/01.rs) |  |
|   2 | [Password Philosophy](https://adventofcode.com/2020/day/2) | [src/bin/02.rs](src/bin/02.rs) |  |
|   3 | [Toboggan Trajectory](https://adventofcode.com/2020/day/3) | [src/bin/03.rs](src/bin/03.rs) |  |
|   4 | [Passport Processing](https://adventofcode.com/2020/day/4) | [src/bin/04.rs](src/bin/04.rs) |  |
|   5 | [Binary Boarding](https://adventofcode.com/2020/day/5) | [src/bin/05.rs](src/bin/05.rs) | [src/bin/05_up.rs](src/bin/05_up.rs) |
|   6 | [Custom Customs](https://adventofcode.com/2020/day/6) | [src/bin/06.rs](src/bin/06.rs) | [src/bin/06_up.rs](src/bin/06_up.rs) |
|   7 | [Handy Haversacks](https://adventofcode.com/2020/day/7) | [src/bin/07.rs](src/bin/07.rs) |  |
|   8 | [Handheld Halting](https://adventofcode.com/2020/day/8) | [src/bin/08.rs](src/bin/08.rs) | [src/bin/08_up.rs](src/bin/08_up.rs) |
|   9 | [Encoding Error](https://adventofcode.com/2020/day/9) | [src/bin/09.rs](src/bin/09.rs) |  |
|  10 | [Adapter Array](https://adventofcode.com/2020/day/10) | [src/bin/10.rs](src/bin/10.rs) |  |
|  11 | [Seating System](https://adventofcode.com/2020/day/11) | [src/bin/11.rs](src/bin/11.rs) |  |
|  12 | [Rain Risk](https://adventofcode.com/2020/day/12) | [src/bin/12.rs](src/bin/12.rs) |  |
|  13 | [Shuttle Search](https://adventofcode.com/2020/day/13) | [src/bin/13.rs](src/bin/13.rs) |  |
|  14 | [Docking Data](https://adventofcode.com/2020/day/14) | [src/bin/14.rs](src/bin/14.rs) |  |
|  15 | [???](https://adventofcode.com/2020/day/15) | [src/bin/15.rs](src/bin/15.rs) |  |
|  16 | [???](https://adventofcode.com/2020/day/16) | [src/bin/16.rs](src/bin/16.rs) |  |
|  17 | [???](https://adventofcode.com/2020/day/17) | [src/bin/17.rs](src/bin/17.rs) |  |
|  18 | [???](https://adventofcode.com/2020/day/18) | [src/bin/18.rs](src/bin/18.rs) |  |
|  19 | [???](https://adventofcode.com/2020/day/19) | [src/bin/19.rs](src/bin/19.rs) |  |
|  20 | [???](https://adventofcode.com/2020/day/20) | [src/bin/20.rs](src/bin/20.rs) |  |
|  21 | [???](https://adventofcode.com/2020/day/21) | [src/bin/21.rs](src/bin/21.rs) |  |
|  22 | [???](https://adventofcode.com/2020/day/22) | [src/bin/22.rs](src/bin/22.rs) |  |
|  23 | [???](https://adventofcode.com/2020/day/23) | [src/bin/23.rs](src/bin/23.rs) |  |
|  24 | [???](https://adventofcode.com/2020/day/24) | [src/bin/24.rs](src/bin/24.rs) |  |
|  25 | [???](https://adventofcode.com/2020/day/25) | [src/bin/25.rs](src/bin/25.rs) |  |

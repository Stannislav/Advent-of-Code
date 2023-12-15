# 🎄 Advent of Code 2023 🎄

* Web: [AoC](https://adventofcode.com/2023)
* Discussions: [Reddit](https://www.reddit.com/r/adventofcode)

This year I'm going to use [Golang](https://golang.org/) to solve the puzzles.

## How to Use
To run a particular solution, for example day 1, use the following command:

```bash
go run cmd/day01/main.go
```

## Solutions
The last column contains improved solutions inspired by solutions of other people.

| Day | Problem                                                                | Solution                   | Improved Solution                   |
|----:|:-----------------------------------------------------------------------|:---------------------------|:------------------------------------|
|   1 | [Trebuchet?!](https://adventofcode.com/2023/day/1)                     | [day01](cmd/day01/main.go) | [day01](cmd/day01-improved/main.go) |
|   2 | [Cube Conundrum](https://adventofcode.com/2023/day/2)                  | [day02](cmd/day02/main.go) |                                     |
|   3 | [Gear Ratios](https://adventofcode.com/2023/day/3)                     | [day03](cmd/day03/main.go) | [day03](cmd/day03-improved/main.go) |
|   4 | [Scratchcards](https://adventofcode.com/2023/day/4)                    | [day04](cmd/day04/main.go) |                                     |
|   5 | [If You Give A Seed A Fertilizer](https://adventofcode.com/2023/day/5) | [day05](cmd/day05/main.go) |                                     |
|   6 | [Wait For It](https://adventofcode.com/2023/day/6)                     | [day06](cmd/day06/main.go) |                                     |
|   7 | [Camel Cards](https://adventofcode.com/2023/day/7)                     | [day07](cmd/day07/main.go) |                                     |
|   8 | [Haunted Wasteland](https://adventofcode.com/2023/day/8)               | [day08](cmd/day08/main.go) | [day08](cmd/day08-improved/main.go) |
|   9 | [Mirage Maintenance](https://adventofcode.com/2023/day/9)              | [day09](cmd/day09/main.go) |                                     |
|  10 | [Pipe Maze](https://adventofcode.com/2023/day/10)                      | [day10](cmd/day10/main.go) |                                     |
|  11 | [Cosmic Expansion](https://adventofcode.com/2023/day/11)               | [day11](cmd/day11/main.go) |                                     |
|  12 | [Hot Springs](https://adventofcode.com/2023/day/12)                    | [day12](cmd/day12/main.go) |                                     |
|  13 | [Point of Incidence](https://adventofcode.com/2023/day/13)             | [day13](cmd/day13/main.go) |                                     |
|  14 | [Parabolic Reflector Dish](https://adventofcode.com/2023/day/14)       | [day14](cmd/day14/main.go) |                                     |
|  15 | [Lens Library](https://adventofcode.com/2023/day/15)                   | [day15](cmd/day15/main.go) |                                     |
|  16 | [???](https://adventofcode.com/2023/day/16)                            | [day16](cmd/day16/main.go) |                                     |
|  17 | [???](https://adventofcode.com/2023/day/17)                            | [day17](cmd/day17/main.go) |                                     |
|  18 | [???](https://adventofcode.com/2023/day/18)                            | [day18](cmd/day18/main.go) |                                     |
|  19 | [???](https://adventofcode.com/2023/day/19)                            | [day19](cmd/day19/main.go) |                                     |
|  20 | [???](https://adventofcode.com/2023/day/20)                            | [day20](cmd/day20/main.go) |                                     |
|  21 | [???](https://adventofcode.com/2023/day/21)                            | [day21](cmd/day21/main.go) |                                     |
|  22 | [???](https://adventofcode.com/2023/day/22)                            | [day22](cmd/day22/main.go) |                                     |
|  23 | [???](https://adventofcode.com/2023/day/23)                            | [day23](cmd/day23/main.go) |                                     |
|  24 | [???](https://adventofcode.com/2023/day/24)                            | [day24](cmd/day24/main.go) |                                     |
|  25 | [???](https://adventofcode.com/2023/day/25)                            | [day25](cmd/day25/main.go) |                                     |

## Golang Offline Documentation
The documentation for the standard library can be viewed using `godoc`.

First, install the `godoc` tool:

```bash
go install golang.org/x/tools/cmd/godoc@latest
```

Next, run the following command to start the documentation server:

```bash
godoc -http=:6060
```

Finally, open a browser with the following URL: http://localhost:6060/.

If the `godoc` binary cannot be found, make sure to add `$GOPATH/bin` to `$PATH`:
```bash
export PATH="$PATH:$(go env GOPATH)/bin"
```

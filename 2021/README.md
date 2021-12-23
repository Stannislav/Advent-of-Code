# 🎄 Advent of Code 2021 🎄

* Web: [AoC](https://adventofcode.com/2021)
* Discussions: [Reddit](https://www.reddit.com/r/adventofcode)

## Goals
The goal this year is to try and use as many new features introduced
in python versions 3.7 to 3.10. Detailed descriptions of all new features
can be found in the python docs:
* [What’s New In Python 3.7](https://docs.python.org/3/whatsnew/3.7.html)
* [What’s New In Python 3.8](https://docs.python.org/3/whatsnew/3.8.html)
* [What’s New In Python 3.9](https://docs.python.org/3/whatsnew/3.9.html)
* [What’s New In Python 3.10](https://docs.python.org/3/whatsnew/3.10.html)

The new features include:
* `[py37]` Postponed evaluation of type annotations
* `[py37]` The `dataclasses` module
* `[py38]` The walrus operator (`:=`)
* `[py38]` Positional-only parameters (`/`)
* `[py38]` Self-documentation operator `=` in f-strings
* `[py38]` The new `@functools.cached_property` decorator
* `[py38]` `typing.Literal` and `typing.Final`
* `[py38]` `typing.Protocol` for structural typing
* `[py39]` Dictionary merge (`|`) and update (`|=`) operators
* `[py39]` `str.removeprefix(prefix)` and `str.removesuffix(suffix)`
* `[py39]` Type hinting generics
* `[py39]` The `graphlib` module and the `graphlib.TopologicalSorter` class
* `[py310]` Structural pattern matching
* `[py310]` Parenthesized context managers
* `[py310]` The type union operator (`|`)
* `[py310]` New method `int.bit_count()`

## How to Run
Create a virtual environment
```sh
pyenv local 3.10.0
virtualenv -p310 venv
source venv/bin/activate
pip install -U pip wheel setuptools
```

Install the package
```sh
pip install .
```

Run a solution (replace `<day>` by the day number)
```sh
aoc <day>
```

Run a community-based solution (replace `<day>` by the day number)
```sh
aoc <day> --extra
```

Installing with development extras allows running unit tests that test
solutions against example data from problem descriptions, as well as
linting, formatting, and typing checks:
```sh
pip install '.[dev]'
tox -e lint  # flake8 and black checks
tox -e format  # apply black formatting
tox -e type  # mypy
pytest  # (or tox -e py310) run unit tests
```

## Solutions
The last column contains improved solutions inspired by solutions of other people.

| Day | Problem                                                         | Solution                                   | Improved Solution                                |
|----:|:----------------------------------------------------------------|:-------------------------------------------|:-------------------------------------------------|
|   1 | [Sonar Sweep](https://adventofcode.com/2021/day/1)              | [day01.py](src/aoc2021/solutions/day01.py) | [day01.py](src/aoc2021/solutions_extra/day01.py) |
|   2 | [Dive!](https://adventofcode.com/2021/day/2)                    | [day02.py](src/aoc2021/solutions/day02.py) | [day02.py](src/aoc2021/solutions_extra/day02.py) |
|   3 | [Binary Diagnostic](https://adventofcode.com/2021/day/3)        | [day03.py](src/aoc2021/solutions/day03.py) |                                                  |
|   4 | [Giant Squid](https://adventofcode.com/2021/day/4)              | [day04.py](src/aoc2021/solutions/day04.py) | [day04.py](src/aoc2021/solutions_extra/day04.py) |
|   5 | [Hydrothermal Venture](https://adventofcode.com/2021/day/5)     | [day05.py](src/aoc2021/solutions/day05.py) |                                                  |
|   6 | [Lanternfish](https://adventofcode.com/2021/day/6)              | [day06.py](src/aoc2021/solutions/day06.py) |                                                  |
|   7 | [The Treachery of Whales](https://adventofcode.com/2021/day/7)  | [day07.py](src/aoc2021/solutions/day07.py) |                                                  |
|   8 | [Seven Segment Search](https://adventofcode.com/2021/day/8)     | [day08.py](src/aoc2021/solutions/day08.py) |                                                  |
|   9 | [Smoke Basin](https://adventofcode.com/2021/day/9)              | [day09.py](src/aoc2021/solutions/day09.py) |                                                  |
|  10 | [Syntax Scoring](https://adventofcode.com/2021/day/10)          | [day10.py](src/aoc2021/solutions/day10.py) |                                                  |
|  11 | [Dumbo Octopus](https://adventofcode.com/2021/day/11)           | [day11.py](src/aoc2021/solutions/day11.py) |                                                  |
|  12 | [Passage Pathing](https://adventofcode.com/2021/day/12)         | [day12.py](src/aoc2021/solutions/day12.py) |                                                  |
|  13 | [Transparent Origami](https://adventofcode.com/2021/day/13)     | [day13.py](src/aoc2021/solutions/day13.py) |                                                  |
|  14 | [Extended Polymerization](https://adventofcode.com/2021/day/14) | [day14.py](src/aoc2021/solutions/day14.py) |                                                  |
|  15 | [Chiton](https://adventofcode.com/2021/day/15)                  | [day15.py](src/aoc2021/solutions/day15.py) |                                                  |
|  16 | [Packet Decoder](https://adventofcode.com/2021/day/16)          | [day16.py](src/aoc2021/solutions/day16.py) |                                                  |
|  17 | [Trick Shot](https://adventofcode.com/2021/day/17)              | [day17.py](src/aoc2021/solutions/day17.py) |                                                  |
|  18 | [Snailfish](https://adventofcode.com/2021/day/18)               | [day18.py](src/aoc2021/solutions/day18.py) |                                                  |
|  19 | [Beacon Scanner](https://adventofcode.com/2021/day/19)          | [day19.py](src/aoc2021/solutions/day19.py) |                                                  |
|  20 | [Trench Map](https://adventofcode.com/2021/day/20)              | [day20.py](src/aoc2021/solutions/day20.py) |                                                  |
|  21 | [Dirac Dice](https://adventofcode.com/2021/day/21)              | [day21.py](src/aoc2021/solutions/day21.py) |                                                  |
|  22 | [Reactor Reboot](https://adventofcode.com/2021/day/22)          | [day22.py](src/aoc2021/solutions/day22.py) |                                                  |
|  23 | [Amphipod](https://adventofcode.com/2021/day/23)                | [day23.py](src/aoc2021/solutions/day23.py) |                                                  |
|  24 | [???](https://adventofcode.com/2021/day/24)                     | [day24.py](src/aoc2021/solutions/day24.py) |                                                  |
|  25 | [???](https://adventofcode.com/2021/day/25)                     | [day25.py](src/aoc2021/solutions/day25.py) |                                                  |

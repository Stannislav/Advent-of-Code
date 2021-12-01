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
* [py37] Postponed evaluation of type annotations
* [py37] The `dataclasses` module
* [py38] The walrus operator (`:=`)
* [py38] Positional-only parameters (`/`)
* [py38] Self-documentation operator `=` in f-strings
* [py38] The new `@functools.cached_property` decorator
* [py38] `Literal` and `Final` in `typing`
* [py39] Dictionary merge (`|`) and update (`|=`) operators
* [py39] `str.removeprefix(prefix)` and `str.removesuffix(suffix)`
* [py39] Type hinting generics
* [py39] The `graphlib` module and the `graphlib.TopologicalSorter` class
* [py310] Structural pattern matching
* [py310] Parenthesized context managers
* [py310] The type union operator (`|`)
* [py310] New method `int.bit_count()`

## How to Run
Create a virtual environment
```bash
pyenv local 3.10.0
virtualenv -p310 venv
source venv/bin/activate
pip install -U pip wheel setuptools
pip install -r requirements.txt
```

Run (replace `XX` by the day number, e.g. `05`, or `17`).
```bash
python src/XX.py
```

Code style and formatting can be checked and fixed using tox.
```bash
tox  # check
tox -e format  # fix
```

## Solutions
The last column contains improved solutions inspired by solutions of other people.

| Day | Problem | Solution | Improved Solution |
|----:|:--------|:---------|:------------------|
|   1 | [Sonar Sweep](https://adventofcode.com/2021/day/1) | [src/01.py](src/01.py) |  |
|   2 | [???](https://adventofcode.com/2021/day/2) | [src/02.py](src/02.py) |  |
|   3 | [???](https://adventofcode.com/2021/day/3) | [src/03.py](src/03.py) |  |
|   4 | [???](https://adventofcode.com/2021/day/4) | [src/04.py](src/04.py) |  |
|   5 | [???](https://adventofcode.com/2021/day/5) | [src/05.py](src/05.py) |  |
|   6 | [???](https://adventofcode.com/2021/day/6) | [src/06.py](src/06.py) |  |
|   7 | [???](https://adventofcode.com/2021/day/7) | [src/07.py](src/07.py) |  |
|   8 | [???](https://adventofcode.com/2021/day/8) | [src/08.py](src/08.py) |  |
|   9 | [???](https://adventofcode.com/2021/day/9) | [src/09.py](src/09.py) |  |
|  10 | [???](https://adventofcode.com/2021/day/10) | [src/10.py](src/10.py) |  |
|  11 | [???](https://adventofcode.com/2021/day/11) | [src/11.py](src/11.py) |  |
|  12 | [???](https://adventofcode.com/2021/day/12) | [src/12.py](src/12.py) |  |
|  13 | [???](https://adventofcode.com/2021/day/13) | [src/13.py](src/13.py) |  |
|  14 | [???](https://adventofcode.com/2021/day/14) | [src/14.py](src/14.py) |  |
|  15 | [???](https://adventofcode.com/2021/day/15) | [src/15.py](src/15.py) |  |
|  16 | [???](https://adventofcode.com/2021/day/16) | [src/16.py](src/16.py) |  |
|  17 | [???](https://adventofcode.com/2021/day/17) | [src/17.py](src/17.py) |  |
|  18 | [???](https://adventofcode.com/2021/day/18) | [src/18.py](src/18.py) |  |
|  19 | [???](https://adventofcode.com/2021/day/19) | [src/19.py](src/19.py) |  |
|  20 | [???](https://adventofcode.com/2021/day/20) | [src/20.py](src/20.py) |  |
|  21 | [???](https://adventofcode.com/2021/day/21) | [src/21.py](src/21.py) |  |
|  22 | [???](https://adventofcode.com/2021/day/22) | [src/22.py](src/22.py) |  |
|  23 | [???](https://adventofcode.com/2021/day/23) | [src/23.py](src/23.py) |  |
|  24 | [???](https://adventofcode.com/2021/day/24) | [src/24.py](src/24.py) |  |
|  25 | [???](https://adventofcode.com/2021/day/25) | [src/25.py](src/25.py) |  |



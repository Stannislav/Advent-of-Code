"""The command line application for running the advent of code solutions."""
import argparse
import importlib
from typing import cast

from aoc2021.lib import ModSolution


def main() -> None:
    """Run the main CLI entry point."""
    parser = argparse.ArgumentParser()
    parser.add_argument("day", type=int)
    parser.add_argument("-e", "--extra", action="store_true")
    args = parser.parse_args()

    if args.extra:
        module = "solutions_extra"
    else:
        module = "solutions"
    module = f"aoc2021.{module}.day{args.day:02d}"

    mod = cast(ModSolution, importlib.import_module(module))
    with open(f"input/{args.day:02d}.txt") as fh:
        raw_data = fh.read()

    part1, part2 = mod.run(raw_data)
    print(f"Part 1: {part1}")
    print(f"Part 2: {part2}")

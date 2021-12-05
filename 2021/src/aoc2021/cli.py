"""The command line application for running the advent of code solutions."""
import argparse
import importlib
import pathlib
from typing import cast

from aoc2021.lib import ModSolution


def main() -> int:
    """Run the main CLI entry point."""
    parser = argparse.ArgumentParser(
        description="Run the advent of code puzzle solutions."
    )
    parser.add_argument("day", type=int, help="the day number")
    parser.add_argument(
        "-e",
        "--extra",
        action="store_true",
        help="run the alternative community-based solution",
    )
    args = parser.parse_args()

    # Read the input data
    data_file = pathlib.Path(f"input/{args.day:02d}.txt")
    if not data_file.exists():
        print(f"Input data file not found: {data_file}")
        return 1
    with data_file.open() as fh:
        raw_data = fh.read()

    # Load the solution module
    if args.extra:
        submodule = "solutions_extra"
    else:
        submodule = "solutions"
    module = f"aoc2021.{submodule}.day{args.day:02d}"
    try:
        mod_solution = cast(ModSolution, importlib.import_module(module))
    except ModuleNotFoundError as exc:
        print(exc)
        return 1

    # Get the solutions
    part1, part2 = mod_solution.run(raw_data)
    print(f"Part 1: {part1}")
    print(f"Part 2: {part2}")

    return 0

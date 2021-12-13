"""Solutions for day 13."""
from typing import NamedTuple


class Dot(NamedTuple):
    """A single dot on the transparent paper."""

    x: int
    y: int


class Cmd(NamedTuple):
    """A folding command."""

    axis: str
    value: int

    def fold(self, dot: Dot) -> Dot:
        """Fold a dot.

        Args:
            dot: The dot to fold

        Returns:
            The folded dot.
        """
        if self.axis == "x":
            return Dot(self.fold_coord(dot.x), dot.y)
        else:
            return Dot(dot.x, self.fold_coord(dot.y))

    def fold_coord(self, coord: int) -> int:
        """Fold a coordinate.

        Args:
            coord: The coordinate value to fold over.

        Returns:
            The folded coordinate.
        """
        if coord > self.value:
            return 2 * self.value - coord  # = n - (coord - n)
        else:
            return coord


def step(dots: set[Dot], cmd: Cmd) -> set[Dot]:
    """Fold all dots with the given command.

    Args:
        dots: All dots to fold.
        cmd: The folding command.

    Returns:
        The dots after folding.
    """
    return {cmd.fold(dot) for dot in dots}


def render(dots: set[Dot], commands: list[Cmd]) -> str:
    """Render the dots into a string.

    Args:
        dots: The dots to render.
        commands: The folding commands. The commands are necessary to
          determine the dimensions of the folded paper: the positions
          of the last fold lines are exactly how big the resulting
          canvas is.

    Returns:
        The rendered dots.
    """
    nx = min(x for axis, x in commands if axis == "x")
    ny = min(y for axis, y in commands if axis == "y")
    lines = []
    for y in range(ny):
        line = "".join("#" if (x, y) in dots else "." for x in range(nx))
        lines.append(line)

    return "\n".join(lines)


def parse_input(data_s: str) -> tuple[set[Dot], list[Cmd]]:
    """Parse the input data.

    Args:
        data_s: The input data.

    Returns:
        The first element are the parsed dots the second the folding commands.
    """
    dots_s, commands_s = data_s.split("\n\n")

    dots = set()
    for line in dots_s.splitlines():
        x, _, y = line.partition(",")
        dots.add(Dot(int(x), int(y)))

    commands = []
    for line in commands_s.splitlines():
        axis, equal, n = line.removeprefix("fold along ").partition("=")
        commands.append(Cmd(axis, int(n)))

    return dots, commands


def run(data_s: str) -> tuple[int, str]:
    """Solve the puzzles."""
    # Parse
    dots, commands = parse_input(data_s)

    # Solve
    part1 = len(step(dots, commands[0]))
    for cmd in commands:
        dots = step(dots, cmd)
    part2 = "\n" + render(dots, commands)

    return part1, part2

"""Solutions for day 12."""
from collections import defaultdict
from typing import Generator

Links = dict[str, list[str]]
Path = tuple[str, ...]


def parse(data_s: str) -> Links:
    """Parse the input data.

    Args:
        data_s: The content of the puzzle input.

    Returns:
        The links between caves represented as a dictionary. The keys
        are the cave names and the values are sequences of other caves
        they connect to.
    """
    links = defaultdict(list)

    for line in data_s.splitlines():
        a, _dash, b = line.partition("-")
        links[a].append(b)
        links[b].append(a)

    return links


def make_paths(
    links: Links, *, extra_visit: bool = False
) -> Generator[Path, None, None]:
    """Generate all paths through the cave system.

    A path is characterized by the sequence of nodes in the order in
    which they were visited.

    Use dynamic programming and iteratively construct paths of
    lengths 1, 2, 3, etc. We can yield the paths as we find them and
    only need to know all the paths of length (n-1) to construct the
    paths of length n.

    Args:
        links: The links between caves. See `parse` for details.
        extra_visit: If true then a single small cave is allowed to be
          visited twice. False for part 1 and true for part 2.

    Returns:
        A generator over all paths. A path is a tuple with the cave
        names in the order they're visited.
    """
    # Holds all (incomplete) paths of length (n-1)
    # The boolean (="has_twice") tracks whether there's a small cave in the
    # path that has already been seen twice, which is something we need to
    # know for part 2. We could compute it directly from a given path every
    # time, but save it here for optimization purposes.
    paths: set[tuple[Path, bool]] = {(("start",), False)}

    # Keep adding nodes as long as there are still incomplete paths
    while len(paths) > 0:
        # Hold all new paths of length n
        new_paths = set()
        # For every path of length (n-1) try to add a new node.
        for path, has_twice in paths:
            for nxt in links[path[-1]]:
                new_path = path + (nxt,)
                if new_path in new_paths or nxt == "start":
                    continue
                if nxt == "end":
                    yield new_path
                elif nxt.isupper():
                    new_paths.add((new_path, has_twice))
                elif not (seen := nxt in path) or (extra_visit and not has_twice):
                    new_paths.add((new_path, has_twice or (nxt.islower() and seen)))

        # Path of length n become paths of length (n-1) in the next iteration.
        paths = new_paths


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    links = parse(data_s)
    part1 = sum(1 for _ in make_paths(links))
    part2 = sum(1 for _ in make_paths(links, extra_visit=True))

    return part1, part2

"""Solutions for day 25."""


def simulate(state: list[list[str]]) -> int:
    """Simulate the sea cucumber movement until they stop moving.

    Args:
        state: The initial sea cucumber state.

    Returns:
        The number of simulation steps done until the sea
        cucumbers stopped moving.
    """
    ni, nj = len(state), len(state[0])
    steps = 0
    right = [(0, 0, 0)]
    down = [(0, 0, 0)]

    while right or down:
        steps += 1
        right.clear()
        down.clear()

        # Move right
        for i in range(ni):
            for j in range(nj):
                if state[i][j] != ">":
                    continue
                k = (j + 1) % nj
                if state[i][k] == ".":
                    right.append((i, j, k))
        for i, j, k in right:
            state[i][j], state[i][k] = state[i][k], state[i][j]

        # Move down
        for i in range(ni):
            for j in range(nj):
                if state[i][j] != "v":
                    continue
                k = (i + 1) % ni
                if state[k][j] == ".":
                    down.append((i, j, k))
        for i, j, k in down:
            state[i][j], state[k][j] = state[k][j], state[i][j]

    return steps


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    cucumbers = [list(line) for line in data_s.splitlines()]

    return simulate(cucumbers), 0

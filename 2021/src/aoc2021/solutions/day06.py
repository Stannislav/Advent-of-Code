"""Solutions for day 6."""


def solve(timers: list[int], duration: int) -> int:
    """Simulate the lanternfish and find that final population.

    The solution is found using dynamic programming. Record how many
    fish are spawned on each given day. If on a day D N new fish spawn,
    then they'll produce an offspring of size N on each of the days
    D+9, D+9+7, D+9+7+7, etc.

    Args:
        timers: The timers of the starting population.
        duration: The duration of the simulation in days.

    Returns:
        The lanternfish population after the given duration.
    """
    births = [0] * duration

    # Compute all spawns from the starting population
    for t in timers:
        for day in range(t, duration, 7):
            births[day] += 1

    # Compute all spawns from the offspring
    for t in range(duration):
        for day in range(t + 9, duration, 7):
            births[day] += births[t]

    return sum(births) + len(timers)


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    timers = [int(t) for t in data_s.split(",")]

    return solve(timers, 80), solve(timers, 256)

"""Solutions for day 17."""
import math
import re


def turning_point(v: int) -> int:
    """Get the distance at which the top distance is reached."""
    return v * (v + 1) // 2


def escape_speed(dist: int) -> float:
    """Get the speed to at least reach dist. Inverse of turning_point."""
    return ((1 + 8 * dist) ** 0.5 - 1) / 2


def shoot(vx: int, vy: int, right: int, bottom: int) -> tuple[int, int]:
    """Simulate a shot and get the last position that's not out of bounds.

    Args:
        vx: The horizontal speed.
        vy: The vertical speed.
        right: The right bound.
        bottom: The bottom bound.

    Returns:
        The (x, y) coordinates of the last position that's not out of bounds.
    """
    x = y = 0
    dvx = vx // abs(vx)
    while x + vx <= right and y + vy >= bottom:
        x += vx
        y += vy
        if vx != 0:
            vx -= dvx
        vy -= 1

    return x, y


def solve(left: int, right: int, bottom: int, top: int) -> list[int]:
    """Solve the puzzle given the target bounds and find all heights reached.

    Args:
        left: The left target bound.
        right: The right target bound.
        bottom: The bottom target bound.
        top: The top target bound.

    Returns:
        A sequence of all heights reached when the target was hit.
    """
    vx_min = math.ceil(escape_speed(left))  # minimal speed to at least reach left
    vx_max = right  # otherwise, will overshoot after one step
    vy_min = bottom  # otherwise, will overshoot after one step (note: bottom < 0)
    vy_max = abs(bottom) - 1  # otherwise, will overshoot one step after crossing y = 0
    heights = []
    for vx in range(vx_min, vx_max + 1):
        for vy in range(vy_min, vy_max + 1):
            x, y = shoot(vx, vy, right, bottom)
            if left <= x <= right and bottom <= y <= top:
                heights.append(turning_point(vy))

    return heights


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    left, right, bottom, top = [int(s) for s in re.findall(r"-?\d+", data_s)]
    heights = solve(left, right, bottom, top)

    return max(heights), len(heights)

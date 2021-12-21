"""Solutions for day 21."""
from functools import cache
from itertools import product


def move(pos: int, steps: int) -> int:
    """Move the player's position by the given number of steps.

    The position is on a disc with labels 1 to 10. After reaching the
    value 10 the position wraps around to 1.

    Args:
        pos: Player's position.
        steps: Number of steps.

    Returns:
        The updated player's position.
    """
    return (pos + steps - 1) % 10 + 1


def play_deterministic(start1: int, start2: int) -> int:
    """Play a deterministic game for part 1.

    Args:
        start1: The starting position of the first player.
        start2: The starting position of the second player.

    Returns:
        The score of the losing player multiplied by the number of times
        the die was rolled during the game.
    """
    pos = [start1, start2]
    score = [0, 0]
    player = 0
    next_value = 1
    while all(s < 1000 for s in score):
        # Roll
        rolled = 3 * next_value + 3  # = x + (x + 1) + (x + 2)
        next_value += 3
        # Move
        pos[player] = move(pos[player], rolled)
        score[player] += pos[player]
        # Switch player
        player = 1 - player

    # the last updated player is the winner, so the current player is the loser
    losing_score = score[player]
    # next_value = n_rolls + 1
    n_rolls = next_value - 1

    return losing_score * n_rolls


@cache
def play_dirac(pos1: int, pos2: int, pts1: int = 0, pts2: int = 0) -> tuple[int, int]:
    """Play a Dirac game for part 2.

    Args:
        pos1: The position of player 1.
        pos2: The position of player 2.
        pts1: The current score of player 1.
        pts2: The current score of player 2.

    Returns:
        The number of wins of each of the player.
    """
    # Because of the player switching (see below) the last updated player is
    # always player 2. So only player 2 (more precisely: the player who is
    # currently taking the role of player 2) can win.
    if pts2 >= 21:
        return 0, 1

    # At each turn the universe splits into 3 * 3 * 3 = 27 universes
    wins1 = wins2 = 0
    for throws in product((1, 2, 3), repeat=3):
        # Move
        pos = move(pos1, sum(throws))
        pts = pts1 + pos
        # Recurse to play the next round. Switch the roles of player 1 and
        # player 2 so that it's the turn of player 2 next.
        w2, w1 = play_dirac(pos2, pos, pts2, pts)
        wins1 += w1
        wins2 += w2

    return wins1, wins2


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    # Parse
    start = []
    for line in data_s.splitlines():
        *_, x = line.rpartition(" ")
        start.append(int(x))

    # Solve
    part1 = play_deterministic(*start)
    wins = play_dirac(*start)
    part2 = max(wins)

    return part1, part2

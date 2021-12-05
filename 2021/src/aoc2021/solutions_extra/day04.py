"""Community-inspired solution to day 4."""


class Board:
    """A bingo board."""

    def __init__(self, values: list[int]) -> None:
        """Initialise the board.

        :param values: The board values row-wise or column-wise.
        """
        self.lines = []  # rows and columns
        for i in range(5):
            for j in range(5):
                self.lines.append({values[i * 5 + j] for i in range(5)})
                self.lines.append({values[i + j * 5] for i in range(5)})
        self.unmarked = set(values)

    def mark(self, n: int) -> None:
        """Mark a value on the board.

        :param n: The value to mark.
        """
        self.unmarked.discard(n)

    def has_won(self) -> bool:
        """Check if the board has won.

        :return: If the board has won.
        """
        for line in self.lines:
            if not line & self.unmarked:
                return True
        return False

    @property
    def score(self) -> int:
        """Compute the current score.

        :return: The current score.
        """
        return sum(self.unmarked)


def run(data: str) -> tuple[int, int]:
    """Compute the solutions."""
    # Parse data
    numbers_s, *boards_s = data.split("\n\n")
    numbers = [int(n) for n in numbers_s.split(",")]
    boards = [Board([int(n) for n in b.split()]) for b in boards_s]

    # Play out all numbers, and collect the scores of the boards in the
    # order they win.
    scores = []
    finished = set()
    for n in numbers:
        for i, board in enumerate(boards):
            if i in finished:
                continue
            board.mark(n)
            if board.has_won():
                scores.append(n * board.score)
                finished.add(i)

    # The solution for part 1 is the score of the first board that won.
    # The solution for part 2 is the score of the last board that won.
    return scores[0], scores[-1]

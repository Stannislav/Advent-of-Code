"""Solutions for day 4."""


class Board:
    """A bingo board."""

    def __init__(self, name: str, board_s: str) -> None:
        """Initialize the board.

        Args:
            name: The name of the board. Only used for bookkeeping.
            board_s: The string with the board data as seen in the input file.
        """
        self.name = name
        self.size = 5

        # Store the board state and the marks as one-dimensional sequences.
        self.data = [int(n) for n in board_s.split()]
        self.marked = [False] * len(self.data)
        if not len(self.data) == self.size ** 2:
            raise ValueError(f"The board data is not {self.size} by {self.size}")

        # Hash table for quick lookup of value positions in data, this only
        # works if there are no duplicate values, hence the check below.
        self.idx = {n: i for i, n in enumerate(self.data)}
        if not len(set(self.data)) == self.size ** 2:
            raise ValueError("Duplicate numbers found in board data.")

    def mark(self, n: int) -> None:
        """Mark a given number on the board."""
        if n in self.idx:
            self.marked[self.idx[n]] = True

    def score(self) -> int:
        """Compute the board score in the current state."""
        return sum(n for n, m in zip(self.data, self.marked) if not m)

    def won(self) -> bool:
        """Test if the board has won."""
        for i in range(self.size):
            # row
            if all(self.marked[i * self.size : (i + 1) * self.size]):
                return True
            # column
            if all(self.marked[i :: self.size]):
                return True
        return False

    def __str__(self) -> str:
        """Generate a string representation of the board."""
        lines = [self.name]
        for i in range(self.size):
            idx = slice(i * self.size, (i + 1) * self.size)
            board_part = " ".join(f"{n:2d}" for n in self.data[idx])
            marked_part = " ".join("XX" if m else "__" for m in self.marked[idx])
            lines.append(f"{board_part}  {marked_part}")
        return "\n".join(lines)


def parse_input(data_s: str) -> tuple[list[int], list[Board]]:
    """Parse the input data.

    Args:
        data_s: The input data.

    Returns:
        The sequence of the bingo numbers that are called out and
        all boards.
    """
    numbers_s, *boards_s = data_s.split("\n\n")
    numbers = [int(n) for n in numbers_s.split(",")]
    boards = [Board(f"Board {i:02d}", board_s) for i, board_s in enumerate(boards_s)]

    return numbers, boards


def part1(numbers: list[int], boards: list[Board]) -> int:
    """Solve part 1."""
    for n in numbers:
        for board in boards:
            board.mark(n)
            if board.won():
                return n * board.score()
    return 0


def part2(numbers: list[int], boards: list[Board]) -> int:
    """Solve part 2."""
    won = [False] * len(boards)
    for n in numbers:
        for i, board in enumerate(boards):
            if won[i]:
                continue
            board.mark(n)
            if board.won():
                won[i] = True
                if all(won):
                    return n * board.score()
    return 0


def run(raw_data: str) -> tuple[int, int]:
    """Solve the puzzles.

    Args:
        raw_data: The contents of the puzzle input file.
    """
    numbers, boards = parse_input(raw_data)
    # for board in boards:
    #     print(board)

    return part1(numbers, boards), part2(numbers, boards)

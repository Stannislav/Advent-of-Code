from typing import Protocol


class ModSolution(Protocol):
    def run(self, data: str) -> tuple[int, int]: ...

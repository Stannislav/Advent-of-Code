"""Solutions for day 18."""
from __future__ import annotations

import enum
from itertools import combinations


class Side(enum.IntEnum):
    """The side of a BTree child."""

    LEFT = 0
    RIGHT = 1

    @property
    def opposite(self) -> Side:
        """Get the opposite side."""
        return Side(1 - self)


class BTree:
    """A binary tree."""

    def __init__(self, parent: BTree | None = None):
        """Initialise a binary tree node without children."""
        self._left: BTree | None = None
        self._right: BTree | None = None
        self._parent = parent

    def __repr__(self) -> str:
        """Get the repr."""
        return f"[{self.left!r},{self.right!r}]"

    @property
    def parent(self) -> BTree | None:
        """Get the parent tree."""
        return self._parent

    @parent.setter
    def parent(self, parent: BTree) -> None:
        """Set the parent tree."""
        self._parent = parent

    @property
    def left(self) -> BTree:
        """Get the left child."""
        if self._left is None:
            raise ValueError("The left child is not set")
        return self._left

    @left.setter
    def left(self, left: BTree) -> None:
        """Set the left child."""
        left.parent = self
        self._left = left

    @property
    def right(self) -> BTree:
        """Get the right child."""
        if self._right is None:
            raise ValueError("The right child is not set")
        return self._right

    @right.setter
    def right(self, right: BTree) -> None:
        """Set the right child."""
        right.parent = self
        self._right = right

    def copy(self) -> BTree:
        """Make a copy of oneself."""
        tree = BTree(self.parent)
        tree.left = self.left.copy()
        tree.right = self.right.copy()
        return tree

    def replace_by(self, tree: BTree) -> None:
        """Replace oneself by a new tree and update the parent tree."""
        tree.parent = self.parent
        if self.parent is not None:
            if self.parent.left is self:
                self.parent.left = tree
            elif self.parent.right is self:
                self.parent.right = tree
            else:
                raise RuntimeError("The given node is not my child")

    def to_list(self) -> list | int:
        """Convert to a nested list. Leaf nodes should convert to their value."""
        return [self.left.to_list(), self.right.to_list()]

    @property
    def magnitude(self) -> int:
        """Compute the magnitude."""
        return 3 * self.left.magnitude + 2 * self.right.magnitude


class Leaf(BTree):
    """A leaf node in a binary tree."""

    def __init__(self, value: int, parent: BTree | None = None):
        """Initialise the leaf node."""
        super().__init__(parent)
        self.value = value

    def __repr__(self) -> str:
        """Get the repr."""
        return str(self.value)

    def copy(self) -> Leaf:
        """Make a copy of oneself."""
        return Leaf(self.value)

    def to_list(self) -> list | int:
        """Convert to a nested list. Leaf nodes should convert to their value."""
        return self.value

    @property
    def magnitude(self) -> int:
        """Compute the magnitude."""
        return self.value


def parse(primitive: list | int) -> BTree:
    """Parse a python primitive, a list or an integer, into a binary tree."""
    if isinstance(primitive, int):
        return Leaf(value=primitive)
    else:
        left, right = map(parse, primitive)
        node = BTree()
        node.left = left
        node.right = right
        return node


def get_child(node: BTree, side: Side) -> BTree:
    """Get the left or the right child of a binary tree node."""
    child = node.left if side is Side.LEFT else node.right
    if child is None:
        raise ValueError("This child is not set")

    return child


def find_neighbour(leaf: Leaf, side: Side) -> Leaf | None:
    """Find the neighbouring leaf in the binary tree.

    To find the left neighbour go up, left, and all the way to the right.
    The only thing we need to ensure is that after going up and left
    we don't end up where we started. If that's the case then go up
    again. Same logic for the right neighbour.
    """
    if leaf.parent is None:
        return None

    # Go up until a side neighbour that's not the original node is found
    child: BTree
    parent, child = leaf.parent, leaf
    while get_child(parent, side) is child:
        grandparent = parent.parent
        if grandparent is None:
            return None
        parent, child = grandparent, parent
    neighbour = get_child(parent, side)

    # Go down and opposite side as far as possible
    while not isinstance(neighbour, Leaf):
        neighbour = get_child(neighbour, side.opposite)

    return neighbour


def explode(node: BTree, depth: int = 0) -> bool:
    """Explode a binary tree as per puzzle instructions."""
    if isinstance(node, Leaf):
        return False

    left = node.left
    right = node.right
    if depth > 3 and isinstance(left, Leaf) and isinstance(right, Leaf):
        prev = find_neighbour(left, Side.LEFT)
        nxt = find_neighbour(right, Side.RIGHT)
        if prev is not None:
            prev.value += left.value
        if nxt is not None:
            nxt.value += right.value
        node.replace_by(Leaf(value=0))
        return True

    return explode(left, depth + 1) or explode(right, depth + 1)


def split(node: BTree) -> bool:
    """Split a binary tree as per puzzle instructions."""
    if isinstance(node, Leaf):
        if node.value < 10:
            return False
        value = node.value // 2
        new = BTree()
        new.left = Leaf(value=value)
        new.right = Leaf(value=node.value - value)
        node.replace_by(new)
        return True

    return split(node.left) or split(node.right)


def add(tree1: BTree, tree2: BTree) -> BTree:
    """Add two binary trees as per puzzle instructions."""
    parent = BTree()
    parent.left = tree1.copy()
    parent.right = tree2.copy()
    while explode(parent) or split(parent):
        pass

    return parent


def run(data_s: str) -> tuple[int, int]:
    """Solve the puzzles."""
    trees = [parse(eval(line)) for line in data_s.splitlines()]

    # Part 1
    total = trees[0]
    for other in trees[1:]:
        total = add(total, other)

    # Part 2
    magnitudes = []
    for tree1, tree2 in combinations(trees, 2):
        magnitudes.append(add(tree1, tree2).magnitude)
        magnitudes.append(add(tree2, tree1).magnitude)

    return total.magnitude, max(magnitudes)

import { readFileSync } from "fs";

class Range {
  constructor(
    public left: number,
    public right: number,
  ) {}

  public static fromString(s: string): Range {
    const [left, right] = s.split("-");
    if (left === undefined || right === undefined) throw Error(`invalid range: ${s}`);

    return new Range(parseInt(left), parseInt(right) + 1);
  }

  public size(): number {
    return this.right - this.left;
  }

  public contains(num: number): boolean {
    return num >= this.left && num < this.right;
  }

  public canMerge(other: Range): boolean {
    if (this.left <= other.left) return this.contains(other.left);
    else return other.contains(this.left);
  }

  public merge(other: Range): Range {
    return new Range(Math.min(this.left, other.left), Math.max(this.right, other.right));
  }
}

function parseInput(input: string): [Range[], number[]] {
  const [rangeStrings, idStrings] = input
    .trim()
    .split("\n\n")
    .map((block) => block.split("\n"));
  if (rangeStrings === undefined || idStrings === undefined) throw Error(`can't parse input`);

  const ranges = rangeStrings.map(Range.fromString);
  const ids = idStrings.map((s) => parseInt(s));

  return [ranges, ids];
}

function part1(ranges: Range[], ids: number[]): number {
  const valid = new Set<number>();
  ids.forEach((id) => {
    for (const range of ranges) {
      if (id >= range.left && id <= range.right) {
        valid.add(id);
      }
    }
  });

  return valid.size;
}

function part2(ranges: Range[]): number {
  const fullyMerged: Range[] = [];
  let toMerge = [...ranges];

  while (toMerge.length > 0) {
    let underMerge = toMerge[0];
    const tail = toMerge.slice(1);
    if (underMerge === undefined) throw Error("merged range is undefined");

    const stillToMerge: Range[] = [];
    let didMerge = false;
    for (const r of tail) {
      if (underMerge.canMerge(r)) {
        underMerge = underMerge.merge(r);
        didMerge = true;
      } else stillToMerge.push(r);
    }

    // If a range under merge didn't intersect with any other one, then it's fully merged.
    (didMerge ? stillToMerge : fullyMerged).push(underMerge);
    toMerge = stillToMerge;
  }

  return fullyMerged.map((r) => r.size()).reduce((a, b) => a + b, 0);
}

function main() {
  const [ranges, ids] = parseInput(readFileSync("input/05.txt", "utf-8"));
  console.log("Part 1:", part1(ranges, ids));
  console.log("Part 2:", part2(ranges));
}

main();

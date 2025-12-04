import { readFileSync } from "fs";
class Point {
  id: string;

  constructor(
    public i: number,
    public j: number,
  ) {
    this.id = `${i},${j}`;
  }
}

function* genAdjacent(p: Point): Generator<Point> {
  for (const di of [-1, 0, 1]) {
    for (const dj of [-1, 0, 1]) {
      if (di !== 0 || dj !== 0) {
        yield new Point(p.i + di, p.j + dj);
      }
    }
  }
}

function getAccessibleRolls(rolls: Map<string, Point>): Point[] {
  return rolls
    .values()
    .filter((p) => [...genAdjacent(p).filter((adj) => rolls.has(adj.id))].length < 4)
    .toArray();
}

function part1(rolls: Map<string, Point>): number {
  const accessible = getAccessibleRolls(rolls);
  return accessible.length;
}

function part2(rolls: Map<string, Point>): number {
  rolls = new Map(rolls); // Create a copy to avoid mutating the original
  const removed: Point[] = [];
  let accessible = getAccessibleRolls(rolls);
  while (accessible.length > 0) {
    for (const p of accessible) {
      rolls.delete(p.id);
      removed.push(p);
    }
    accessible = getAccessibleRolls(rolls);
  }
  return removed.length;
}

function main() {
  const rolls = new Map<string, Point>();
  const input = readFileSync("input/04.txt", "utf-8").trim().split("\n");
  for (let i = 0; i < input.length; i++) {
    const line = input.at(i) || "";
    for (let j = 0; j < line.length; j++) {
      if (line[j] === "@") {
        const point = new Point(i, j);
        rolls.set(point.id, point);
      }
    }
  }

  console.log("Part 1:", part1(rolls));
  console.log("Part 2:", part2(rolls));
}

main();

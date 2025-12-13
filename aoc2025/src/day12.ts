import { readFileSync } from "fs";

interface Region {
  dx: number;
  dy: number;
  presentCounts: number[];
}

function main() {
  const blocks = readFileSync("input/12.txt", "utf-8").trim().split("\n\n");
  const regions = blocks.at(6)?.split("\n").map(parseRegion) ?? [];
  console.log("Part 1:", part1(regions));
}

function part1(regions: Region[]): number {
  const needPacking = regions.filter((r) => maybeFit(r) && !triviallyFit(r));
  const definitelyFit = regions.filter(triviallyFit);

  // Turns out the problem's input is such the none of the regions need packing.
  if (needPacking.length === 0) return definitelyFit.length;
  else throw Error(`${needPacking.length} regions need packing`);
}

function sum(numbers: number[]): number {
  return numbers.reduce((a, b) => a + b);
}

function maybeFit(r: Region): boolean {
  // Each present has 7 tiles, so region's area must be at least 7 times the number of presents.
  return r.dx * r.dy >= 7 * sum(r.presentCounts);
}

function triviallyFit(r: Region): boolean {
  // A trivial fit is when the 3x3 blocks of the presents can fit without overlapping.
  // Crop the region's size so that it's side's lengths are multiples of 3, then check
  // if the 3x3 blocks all fit.
  return (r.dx - (r.dx % 3)) * (r.dy - (r.dy % 3)) >= 9 * sum(r.presentCounts);
}

function parseRegion(s: string): Region {
  const match = s.match(/^(\d+)x(\d+): (.*)/);
  if (!match) throw Error(`can't parse region: ${s}`);
  return {
    dx: Number(match[1]),
    dy: Number(match[2]),
    presentCounts: match[3]?.split(" ").map(Number) ?? [],
  };
}

main();

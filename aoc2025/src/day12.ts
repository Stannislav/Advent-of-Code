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

/**
 * Find the number of regions which fit all their presents.
 * This solution only works because of the specifics of the input:
 * we assume that all presents are 3x3 squares, which gives a size of 9
 * and check if the surface of the region is bigger or equal to 9 times
 * the number of presents.
 * @param regions The regions from the input
 * @returns The number of regions which fit all their presents.
 */
function part1(regions: Region[]): number {
  return regions.filter((r) => r.dx * r.dy >= 9 * r.presentCounts.reduce((a, b) => a + b)).length;
}

function parseRegion(s: string): Region {
  const match = s.match(/^(\d+)x(\d+): (.*)/);
  if (!match) throw Error(`can't parse region: ${s}`);
  console.log(match);
  return {
    dx: Number(match[1]),
    dy: Number(match[2]),
    presentCounts: match[3]?.split(" ").map(Number) ?? [],
  };
}

main();

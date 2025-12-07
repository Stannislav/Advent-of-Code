import { readFileSync } from "fs";

function part1(lines: string[], start: number): number {
  let splitCount = 0;
  let currentBeams = new Set<number>();
  currentBeams.add(start);
  for (const line of lines.slice(1)) {
    const newBeams = new Set<number>();
    for (const pos of currentBeams) {
      if (line.at(pos) === "^") {
        newBeams.add(pos - 1);
        newBeams.add(pos + 1);
        splitCount++;
      } else newBeams.add(pos);
    }
    currentBeams = newBeams;
  }
  return splitCount;
}

/**
 * Determine the number of timelines possible from the start.
 *
 * We go through the grid line backwards and for each point determine
 * how many timelines exist starting from it. Since timelines don't go
 * past the last line, the number of timelines is one for each point.
 * For other lines the number of timelines is the same as the previous
 * line if the beam didn't split, or the sum of the timeline counts of
 * the two points where a split beam would go.
 *
 * @param lines The grid lines of the input.
 * @param start The column of the start.
 * @returns Number of timelines possible from the start.
 */
function part2(lines: string[], start: number): number {
  const width = lines[0]?.length || 0;
  let lastRow = Array(width).fill(1);
  for (let idx = lines.length - 1; idx >= 0; idx--) {
    const currentRow: number[] = Array(width);
    const line = lines.at(idx) || [];
    [...line].forEach((c, idx) => {
      if (c === "^") currentRow[idx] = lastRow[idx - 1] + lastRow[idx + 1];
      else currentRow[idx] = lastRow.at(idx);
    });
    lastRow = currentRow;
  }
  return lastRow.at(start);
}

function main() {
  const lines = readFileSync("input/07.txt", "utf-8").trim().split("\n");
  const start = lines.at(0)?.indexOf("S");
  if (start === undefined) throw Error("can't find the starting point");

  console.log("Part 1:", part1(lines, start));
  console.log("Part 2:", part2(lines, start));
}

main();

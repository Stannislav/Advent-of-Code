import { readFileSync } from "fs";

interface Range {
  left: string;
  right: string;
}

function parseRange(s: string): Range {
  const [left, right] = s.split("-");
  if (left === undefined || right === undefined) throw new Error(`Invalid range: ${s}`);
  return { left, right };
}

function solve(ranges: Range[], minBlockLen: (num: string) => number): number {
  function sumInvalid(left: string, right: string): number {
    if (left.length !== right.length)
      return sumInvalid(left, "9".repeat(left.length)) + sumInvalid(1 + "0".repeat(left.length), right);

    const found = new Set<number>();
    for (let blockLen = minBlockLen(left); blockLen <= left.length / 2; blockLen++) {
      if (left.length % blockLen !== 0) {
        continue;
      }
      const repeats = left.length / blockLen;
      const start = Number(left.slice(0, blockLen));
      const end = Number(right.slice(0, blockLen));

      for (let block = start; block <= end; block++) {
        const candidate = block.toString().repeat(repeats);
        if (candidate >= left && candidate <= right) {
          found.add(Number(candidate));
        }
      }
    }

    return Array.from(found).reduce((a, b) => a + b, 0);
  }

  return ranges.reduce((acc, range) => acc + sumInvalid(range.left, range.right), 0);
}

const part1 = (input: Range[]) => solve(input, (num) => Math.ceil(num.length / 2));
const part2 = (input: Range[]) => solve(input, () => 1);

function main() {
  const ranges = readFileSync("input/02.txt", "utf8")
    .trim()
    .split(",")
    .map((s) => parseRange(s));
  console.log("Part 1:", part1(ranges));
  console.log("Part 2:", part2(ranges));
}

main();

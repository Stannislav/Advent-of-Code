import { readFileSync } from "fs";

interface ConnObj {
  from: string;
  to: string[];
}

function main() {
  const fromToObjects = readFileSync("input/11.txt", "utf-8").trim().split("\n").map(parseConnection);
  const conn = new Map(fromToObjects.map((c) => [c.from, c.to]));

  console.log("Part 1:", part1(conn));
  console.log("Part 2:", part2(conn));
}

function parseConnection(s: string): ConnObj {
  const [left, right] = s.split(": ");
  if (!left || !right) throw Error("invalid connection string");
  return { from: left, to: right.split(" ") };
}

function part1(conn: Map<string, string[]>): number {
  function count(from = "you"): number {
    if (from === "out") return 1;
    return (
      conn
        .get(from)
        ?.map(count)
        .reduce((a, b) => a + b) || 0
    );
  }
  return count();
}

function part2(conn: Map<string, string[]>): number {
  class Cache<T> {
    private cache = new Map<string, T>();
    cachedCall(
      countFn: (to: string, seenDAC: boolean, seenFFT: boolean) => T,
      to: string,
      seenDAC: boolean,
      seenFFT: boolean,
    ): T {
      const key = `${to},${seenDAC},${seenFFT}`;
      const val = this.cache.get(key);
      if (val !== undefined) return val;
      const newVal = countFn(to, seenDAC, seenFFT);
      this.cache.set(key, newVal);
      return newVal;
    }
  }

  const cache = new Cache<number>();

  function count(from = "svr", seenDAC = false, seenFFT = false): number {
    if (from === "out") {
      if (seenDAC && seenFFT) return 1;
      else return 0;
    }
    return (
      conn
        .get(from)
        ?.map((to) => cache.cachedCall(count, to, seenDAC || to === "dac", seenFFT || to === "fft"))
        .reduce((a, b) => a + b) || 0
    );
  }

  return count();
}

main();

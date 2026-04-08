import { parseInput, part1, part2 } from "./solution.js";

const input = `
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
`;

describe("parse input", () => {
  it("should parse input correctly", () => {
    const got = parseInput(input);
    const want = [
      { x: 7, y: 1 },
      { x: 11, y: 1 },
      { x: 11, y: 7 },
      { x: 9, y: 7 },
      { x: 9, y: 5 },
      { x: 2, y: 5 },
      { x: 2, y: 3 },
      { x: 7, y: 3 },
    ];
    expect(got).toStrictEqual(want);
  });
});

describe("part 1", () => {
  it("should give the right solution", () => {
    const points = parseInput(input);
    expect(part1(points)).toBe(50);
  });
});

describe("part 2", () => {
  it("should give the right solution", () => {
    const points = parseInput(input);
    expect(part2(points)).toBe(24);
  });
});

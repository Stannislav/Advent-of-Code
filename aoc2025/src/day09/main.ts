import { readFileSync } from "fs";
import { parseInput, part1, part2 } from "./solution.js";

const input = readFileSync("input/09.txt", "utf-8");
const points = parseInput(input);
console.log("Part 1:", part1(points));
console.log("Part 2:", part2(points));

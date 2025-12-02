import { readFileSync } from "fs";

const lines = readFileSync("input/01.txt", "utf8").trim().split("\n")

const cmds = [];
for (const line of lines) {
    let dist = Number(line.slice(1));
    if (line[0] === 'L')
        dist = -dist;
    cmds.push(dist);
}

let pos = 50;
let stopped_at_zero = 0;
let crossed_zero = 0
for (const cmd of cmds) {
    const loops = Math.floor(Math.abs(cmd) / 100);
    const rest = cmd % 100;

    crossed_zero += loops;

    const newPos = pos + rest;
    if (newPos > 100 || (pos != 0 && newPos < 0))
        crossed_zero++;

    pos = (newPos + 100) % 100;
    if (pos === 0)
        stopped_at_zero++;
}
console.log("Part 1:", stopped_at_zero);
console.log("Part 2:", stopped_at_zero + crossed_zero);

'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("../input/input_19.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Solutions
const computer = new IntcodeComputer();

let cache = {};
function checkPoint(x, y) {
    if (!([x, y] in cache)) {
        computer.launch(program, {inputs: [x, y]});
        cache[[x, y]] = computer.getOutput();
    }

    return cache[[x, y]];
}

// Part 1
let part1 = 0;
let [tan1, tan2] = [Infinity, -Infinity];
for (let x  = 0; x < 50; ++x) {
    let previous = 0;
    for (let y = 0; y < 50; ++y) {
        let current = checkPoint(x, y);

        // Estimate the opening of the beam by tracking
        // tan(angle) = y / x at beam edges
        if (current - previous === 1 && x > 20)
            tan1 = Math.min(tan1, y / x);
        else if (current - previous === -1 && x > 20)
            tan2 = Math.max(tan2, y / x);

        part1 += current;
        previous = current;
    }
}
console.log("Part 1:", part1);


// Part 2
let part2;
outer:
for (let x = 100; ;++x) {
    let yMin = Math.floor(tan1 * (x + 100)) - 1;
    let yMax = Math.ceil(tan2 * x) - 100 + 1;
    nextPoint:
    for (let y = yMin; y <= yMax; ++y) {
        for (let dy = 0; dy < 100; ++dy) {
            if (!checkPoint(x, y + dy))
                continue nextPoint;
        }
        for (let dx = 0; dx < 100; ++dx) {
            if (!checkPoint(x + dx, y))
                continue nextPoint;
        }
        part2 = x * 10000 + y;
        break outer;
    }
}

console.log("Part 2:", part2);

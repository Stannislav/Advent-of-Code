'use strict';


// Imports
const assert = require('assert');
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("input/07.txt", "utf-8");
const program = data.trim().split(",").map(Number);


// Preliminaries
function getPermutations(arr) {
    /**
     * Compute all permutations of given array
     */
    if (arr.length <= 1)
        return arr;

    let result = [];
    for (let i = 0; i < arr.length; ++i) {
        let first = arr[i];
        let rest = arr.slice(0, i).concat(arr.slice(i + 1));
        getPermutations(rest).forEach(subPerm => {
            result.push([first].concat(subPerm));
        });
    }

    return result;
}


const amps = [
    new IntcodeComputer(),
    new IntcodeComputer(),
    new IntcodeComputer(),
    new IntcodeComputer(),
    new IntcodeComputer(),
];

// Part 1
let part1 = -1;
getPermutations([0, 1, 2, 3, 4]).forEach(phases => {
    let signal = 0;

    for (let n = 0; n < 5; ++n) {
        amps[n].launch(program, {inputs: [phases[n], signal]});
        signal = amps[n].getOutput()
    }
    part1 = Math.max(part1, signal);
});
console.log("Part 1:", part1);


// Part 2
let part2 = -1;
getPermutations([5, 6, 7, 8, 9]).forEach(phases => {
    amps.forEach((amp, i) => amp.launch(program, {inputs: [phases[i]]}));

    let signal = 0;
    let pos = 0;
    while (true) {
        amps[pos].putInput(signal);
        amps[pos].resume();
        if (!amps[pos].hasOutput())
            break;
        signal = amps[pos].getOutput();
        pos = (pos + 1) % 5;
    }
    part2 = Math.max(part2, signal);
});
console.log("Part 2:", part2);

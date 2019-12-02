'use strict';

// Read data
const fs = require('fs');
const data = fs.readFileSync("../input/input_02.txt", "utf8");
let program = data.trim().split(",").map(Number);


// Helper function
const ops = {
    1: (a, b) => a + b,
    2: (a, b) => a * b,
};

function run_program(program, noun, verb) {
    program = program.slice(0);
    program[1] = noun;
    program[2] = verb;

    let i = 0;
    while (program[i] !== 99) {
        const [opCode, i1, i2, iOut] = program.slice(i, i + 4);
        program[iOut] = ops[opCode](program[i1], program[i2]);
        i += 4;
    }

    return program[0];
}


// Part 1
console.log("Part 1:", run_program(program, 12, 2));


// Part 2
let solution = "unknown";
outer:
for (let noun = 0; noun < 100; ++noun) {
    for (let verb = 0; verb < 100; ++verb) {
        let result = run_program(program, noun, verb);
        if (result === 19690720) {
            solution = 100 * noun + verb;
            break outer;
        }
    }
}
console.log("Part 2:", solution);

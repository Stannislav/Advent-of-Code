'use strict';


// Read data
const fs = require('fs');
const data = fs.readFileSync("../input/input_05.txt", "utf-8");
const program = data.trim().split(",").map(Number);


// Number of instruction steps for each op
const n_inst = {
    1: 4,  // add
    2: 4,  // multiply
    3: 2,  // input
    4: 2,  // output
    5: 3,  // jump-if-true
    6: 3,  // jump-if-false
    7: 4,  // less than
    8: 4,  // equals
    99: 1, // halt
};

let inputs = [];
let outputs = [];


function get_value(program, arg, mode) {
    if (mode === 0)
        return program[arg];
    else if (mode === 1)
        return arg;
    else
        throw "Bad mode";
}


function parse_instruction(instruction) {
    let op = instruction % 100;
    instruction = Math.floor(instruction / 100);
    let modes = [];
    for (let i = 0; i < n_inst[op] - 1; ++i) {
        modes.push(instruction % 10);
        instruction = Math.floor(instruction / 10);
    }

    return [op, modes];
}


function step(program, ptr) {
    // Parse op and mode
    const [op, modes] = parse_instruction(program[ptr]);
    const args = program.slice(ptr + 1, ptr + n_inst[op]);

    // Retrieve values based on modes
    const vals = args.map((arg, i) => get_value(program, arg, modes[i]));

    // Execute op
    switch (op) {
        case 1:
            program[args[2]] = vals[0] + vals[1];
            break;
        case 2:
            program[args[2]] = vals[0] * vals[1];
            break;
        case 3:
            if (inputs.length !== 0)
                program[args[0]] = inputs.pop();
            else
                throw "Not enough inputs!";
            break;
        case 4:
            outputs.push(vals[0]);
            break;
        case 5:
            if (vals[0] !== 0)
                return vals[1];
            break;
        case 6:
            if (vals[0] === 0)
                return vals[1];
            break;
        case 7:
            if (vals[0] < vals[1])
                program[args[2]] = 1;
            else
                program[args[2]] = 0;
            break;
        case 8:
            if (vals[0] === vals[1])
                program[args[2]] = 1;
            else
                program[args[2]] = 0;
            break;
        case 99:
            return -1;
        default:
            throw `Unknown op: ${op}`;
    }

    return ptr + n_inst[program[ptr] % 100];
}


function run_program(program) {
    program = [...program];  // shallow copy

    for (let ptr = 0; ptr >= 0; )
        ptr = step(program, ptr);
}


inputs = [1];
outputs = [];
run_program(program);
console.log("Part 1:", outputs[outputs.length - 1]);

inputs = [5];
outputs = [];
run_program(program);
console.log("Part 2:", outputs[outputs.length - 1]);

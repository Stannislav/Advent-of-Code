'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("input/05.txt", "utf-8");
const program = data.trim().split(",").map(Number);


// Solution
const computer = new IntcodeComputer();

computer.launch(program, {inputs: [1]});
console.log(computer.outputs);
console.log("Part 1:", computer.outputs.pop());

computer.launch(program, {inputs: [5]});
console.log("Part 2:", computer.outputs.pop());

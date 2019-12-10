// 'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read Data
const data = fs.readFileSync("../input/input_09.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Solutions
let computer = new IntcodeComputer();

computer.launch(program, [1]);
console.log("Part 1:", computer.outputs.pop());

computer.launch(program, [2]);
console.log("Part 2:", computer.outputs.pop());

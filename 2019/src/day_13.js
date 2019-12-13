'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("../input/input_13.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Setup
const computer = new IntcodeComputer();
let display = [];
let score = 0;
let ballPos = 0;
let paddlePos = 0;
const [EMPTY, WALL, BLOCK, PADDLE, BALL] = [0, 1, 2, 3, 4];
const COLORS = {
    [EMPTY]:  ' ',
    [WALL]:   '█',
    [BLOCK]:  '░',
    [PADDLE]: '▔',
    [BALL]:   'O',
};


// Helper functions
function setPixel(x, y, type) {
    if (!(y in display))
        display[y] = [];
    display[y][x] = COLORS[type];
}


function drawDisplay() {
    for (let line of display)
        console.log(line.join(""));
    console.log("Score:", score);
}


function updateDisplay(instructions) {
    for (let i = 0; i < instructions.length; i += 3) {
        let [x, y, type] = instructions.slice(i, i + 3);
        if (x === -1 && y === 0)
            score = type;
        else {
            setPixel(x, y, type);
            if (type === BALL)
                ballPos = x;
            else if (type === PADDLE)
                paddlePos = x;
        }

    }
}


// Part 1
computer.launch(program);
display = [];
updateDisplay(computer.getAllOutputs());
// drawDisplay();

let part1 = 0;
for (let line of display)
    part1 += line.map(c => c === COLORS[BLOCK]).reduce((a, b) => a + b, 0);
console.log("Part 1:", part1);


// Part2
let free_program = [...program];
free_program[0] = 2;
display = [];
score = 0;

function gameStep() {
    let direction = 0;

    if (paddlePos < ballPos)
        direction = 1;
    else if (paddlePos > ballPos)
        direction = -1;

    computer.putInput(direction);
    computer.resume();
    updateDisplay(computer.getAllOutputs());
    // drawDisplay();
}

computer.launch(free_program);
while (computer.status !== computer.HALTED)
    gameStep();
console.log("Part 2:", score);

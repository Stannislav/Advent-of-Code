'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');

// Read data
const data = fs.readFileSync("../input/input_11.txt", "utf-8");
const program = data.trim().split(',').map(Number);


function paintPanels(panels) {
    let computer = new IntcodeComputer();
    computer.launch(program);

    let steps = [[0, 1], [1, 0], [0, -1], [-1, 0]];
    let direction = 0; // pointing in the +y direction
    let [x, y] = [0, 0];

    while (computer.status !== computer.HALTED) {
        let id = `${x},${y}`;
        let color = id in panels ? panels[id] : 0;
        computer.putInput(color);
        computer.resume();
        let newColor = computer.getOutput();
        let turn = computer.getOutput();
        panels[id] = newColor;
        direction = turn === 0 ? (direction - 1 + 4) % 4 : (direction + 1) % 4;

        let [dx, dy] = steps[direction];
        x += dx;
        y += dy;
    }

    return panels;
}


function parsePicture(panels) {
    let picture = [];

    // Extract the dimensions of the picture
    // Note the minus sign in the y-coordinate: the computer paints the tiles below the starting point.
    let xs = Object.keys(panels).map(key => key.split(',').map(Number)[0]);
    let ys = Object.keys(panels).map(key => -key.split(',').map(Number)[1]);
    let xMin = Math.min(...xs);
    let xMax = Math.max(...xs);
    let yMin = Math.min(...ys);
    let yMax = Math.max(...ys);

    // Initialise an empty picture
    for (let row = 0; row < (yMax - yMin + 1); ++row) {
        picture.push([]);
        for (let col = 0; col < (xMax - xMin + 1); ++col) {
            picture[row].push(' ');
        }
    }

    // Transfer paint from panels to picture
    for (let key in panels) {
        let [x, y] = key.split(',').map(Number);
        picture[-y][x] = panels[key] === 0 ? ' ' : '█';
    }

    return picture;
}


function printPicture(picture) {
    for (let nRow in picture)
        console.log(picture[nRow].join(""));
}


// Solution
let panels = {};

panels = paintPanels({});
console.log("Part 1:", Object.keys(panels).length);

panels = paintPanels({"0,0": 1});
let picture = parsePicture(panels);
console.log("Part 2:");
printPicture(picture);


'use strict';


// Imports
const fs = require('fs');


// Read Data
const data = fs.readFileSync("../input/input_08.txt", "utf-8");
const pixels = [...data.trim()].map(Number);

const WIDTH = 25;
const HEIGHT = 6;
const DEPTH = pixels.length / (WIDTH * HEIGHT);


// Helper Functions
function constructImage(pixels) {
    let image = [];
    for (let c = 0; c < DEPTH; ++c) {
        let layer = [];
        let pos = WIDTH * HEIGHT * c;
        for (let h = 0; h < HEIGHT; ++h) {
            let row = pixels.slice(pos + WIDTH * h, pos + WIDTH * (h + 1));
            layer.push(row);
        }
        image.push(layer);
    }

    return image;
}


function getPixelCounts(layer) {
    let counts = {0: 0, 1: 0, 2: 0};
    layer.forEach(row => row.forEach(pixel => counts[pixel]++));

    return counts;
}


function printImage(layer) {
    let palette = {0: '█', 1: '░', 2: ' '};
    layer.forEach(row => console.log(row.map(i => palette[i]).join("")));
}


// Solution
let image = constructImage(pixels);


// Part 1
let minCount = Infinity;
let part1 = undefined;
image.forEach(layer => {
    let counts = getPixelCounts(layer);
    if (counts[0] < minCount) {
        minCount = counts[0];
        part1 = counts[1] * counts[2];
    }
});
console.log("Part 1:", part1);


// Part 2
let part2 = image[0];
for (let row = 0; row < HEIGHT; ++row) {
    for (let col = 0; col < WIDTH; ++col) {
        for (let channel = 0; channel < DEPTH; ++channel) {
            let color = image[channel][row][col];
            if (color === 2)
                continue;
            part2[row][col] = color;
            break;
        }
    }
}

console.log("Part 2:");
printImage(part2);
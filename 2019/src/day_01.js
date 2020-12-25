'use strict';

// Read Data
const fs = require('fs');
let data = fs.readFileSync("input/01.txt", "utf8");
data = data.trim().split("\n").map(Number);


function getFuel(x) {
    return Math.floor(x / 3) - 2
}


let total;
// Part 1
total = 0;
for (let i = 0; i < data.length; ++i) {
    total += getFuel(data[i]);
}
console.log("Part 1:", total);


// Part 2;
total = 0;
for (let i = 0; i < data.length; ++i) {
    let fuel = getFuel(data[i]);
    while (fuel > 0) {
        total += fuel;
        fuel = getFuel(fuel);
    }
}
console.log("Part 2:", total);

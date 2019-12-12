'use strict';


// Imports
const fs = require('fs');

// Read data
const data = fs.readFileSync("../input/input_12.txt", "utf-8");


// Solution
function initialSetup() {
    let positions = data.trim().split('\n').map(line => line.match(/-?[0-9]+/g).map(Number));
    let velocities = [];
    for (let position of positions)
        velocities.push([0, 0, 0]);

    return [positions, velocities];
}

function energy(arr) {
    return arr.map(Math.abs).reduce((a, b) => a + b, 0);
}

function totalEnergy() {
    return positions.map((pos, i) => energy(pos) * energy(velocities[i])).reduce((a, b) => a + b, 0)
}

function makeStep() {
    // Update velocities
    for (let i = 0 ; i < 4; ++i) {
        for (let j = i + 1; j < 4; ++j) {
            for (let coord = 0; coord < 3; ++coord) {
                let result = positions[i][coord] - positions[j][coord];
                let dv = result === 0 ? 0 : result / Math.abs(result);
                velocities[i][coord] -= dv;
                velocities[j][coord] += dv;
            }
        }
    }

    // Update positions
    for (let moonId in positions) {
        for (let coord = 0; coord < 3; ++coord) {
            positions[moonId][coord] += velocities[moonId][coord];
        }
    }
}

// Part 1
let [positions, velocities] = initialSetup();
for (let step = 0; step < 1000; ++step) {
    makeStep();
}

console.log("Part 1:", totalEnergy());

// Part 2
// [positions, velocities] = initialSetup();
// let history = new Set();
// while (true) {
//     let id = positions.concat(velocities).join();
//     if (history.has(id))
//         break;
//     else
//         history.add(id);
//     makeStep();
//     if (history.size % 1e6 === 0)
//         console.log(history.size)
// }
// console.log("Part 2:", history.size);
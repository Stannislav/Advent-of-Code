'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("../input/input_12.txt", "utf-8");

function parseData(data) {
    /**
     * Store positions in a matrix of dimensions (nParticles, nCoordinates=3)
     * Initialise a zero matrix for velocities with the same dimensions.
     */
    let positions = data.trim().split('\n').map(line => line.match(/-?[0-9]+/g).map(Number));
    let velocities = [];
    for (let position of positions)
        velocities.push([0, 0, 0]);

    return [positions, velocities];
}

const [initialPositions, initialVelocities] = parseData(data);
const N = initialPositions.length;


// Definitions
function stepCoordinate(c) {
    /**
     * Simulation step for coordinate c
     */
    // Update velocities
    for (let i = 0; i < N; ++i) {
        for (let j = i + 1; j < N; ++j) {
            if (positions[i][c] > positions[j][c]) {
                velocities[i][c] -= 1;
                velocities[j][c] += 1;
            } else if (positions[i][c] < positions[j][c]) {
                velocities[i][c] += 1;
                velocities[j][c] -= 1;
            }
        }
    }

    // Update coordinates
    for (let i = 0; i < N; ++i) {
        positions[i][c] += velocities[i][c];
    }
}

function getEnergy(values) {
    return values.map(Math.abs).reduce((a, b) => a + b, 0);
}

function getTotalEnergy(positions, velocities) {
    let pot = positions.map(getEnergy);
    let kin = velocities.map(getEnergy);
    let totalEnergy = 0;
    for (let i = 0; i < N; ++i)
        totalEnergy += pot[i] * kin[i];
    return totalEnergy;
}

function gcd(a, b) {
    /**
     * Greatest Common Divisor, Euclidean method.
     * Implementation for non-negative numbers only
     */
    while (a > 0 && b > 0) {
        if (a > b)
            a -= b;
        else
            b -= a;
    }

    return a + b;
}

function lcm(a, b) {
    /**
     * Least Common Multiple
     */
    return a * b / gcd(a, b);
}



// Solution
let positions, velocities;

// Part 1
positions = initialPositions.map(p => [...p]);
velocities = initialVelocities.map(v => [...v]);

for (let i = 0; i < 1000; ++i) {
    stepCoordinate(0);
    stepCoordinate(1);
    stepCoordinate(2);
}
console.log("Part 1:", getTotalEnergy(positions, velocities));

// Part 2
positions = initialPositions.map(p => [...p]);
velocities = initialVelocities.map(v => [...v]);

/*
Idea:
All coordinate updates are independent of each other. So simulate each of
the coordinates separately and find its cycle length, i.e. the number
of steps required to come back to the initial position. After that, the
common cycle length is equal to the least common multiple (LCM) of all
coordinate cycles.
 */
let cycles = [];
for (let c = 0; c < 3; c++) {
    let step = 0;
    let done = false;
    while (!done) {
        // Simulation step
        step += 1;
        stepCoordinate(c);

        // Check if we're back where we started
        done = true;
        for (let i = 0; i < N; ++i) {
            if (positions[i][c] !== initialPositions[i][c] || velocities[i][c] !== initialVelocities[i][c]) {
                done = false;
                break;
            }
        }
    }
    cycles.push(step);
}

console.log("Part 2:", cycles.reduce((a, b) => lcm(a, b), 1));

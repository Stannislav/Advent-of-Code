'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("input/15.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Definitions
const [WALL, EMPTY, GOAL] = [0, 1, 2];
const [NORTH, SOUTH, EAST, WEST] = [1, 2, 3, 4];

let steps = {
    [NORTH]: [0, 1],
    [SOUTH]: [0, -1],
    [EAST]: [1, 0],
    [WEST]: [-1, 0],
};

let backtrack = {
    [NORTH]: SOUTH,
    [SOUTH]: NORTH,
    [EAST]: WEST,
    [WEST]: EAST,
};

function stepTo(direction) {
    computer.putInput(direction);
    computer.resume();
    return computer.getOutput();
}

function explorePoint([px, py]) {
    /**
     * This only works of each point can be reached in exactly one way...
     */
    let [type, dist] = map[[px, py]];

    for (let direction of [NORTH, SOUTH, EAST, WEST]) {
        let [dx, dy] = steps[direction];
        let next = [px + dx, py + dy];
        if (next in map)  // already seen that point
            continue;
        type = stepTo(direction);
        map[next] = [type, dist + 1];
        if (type === WALL)
            continue;
        explorePoint(next);
        stepTo(backtrack[direction]);
    }
}

function fillOxygen([px, py]) {
    for (let direction of [NORTH, SOUTH, EAST, WEST]) {
        let [dx, dy] = steps[direction];
        let next = [px + dx, py + dy];
        let [type, _] = map[next];
        if (next in times || type === WALL)  // already visited or hit the wall
            continue;
        times[next] = times[[px, py]] + 1;
        fillOxygen(next);
    }
}


// Part 1
const computer = new IntcodeComputer();
computer.launch(program);
let start = [0, 0];
let goal = undefined;
let map = {[start]: [EMPTY, 0]};

explorePoint([0, 0]);
for (let [key, [type, dist]] of Object.entries(map)) {
    if (type === GOAL) {
        console.log("Part 1:", dist);
        goal = key.match(/-?\d+/g).map(Number);
    }
}

// Part 2
let times = {[goal]: 0};
fillOxygen(goal);
console.log("Part 2:", Math.max(...Object.values(times)));

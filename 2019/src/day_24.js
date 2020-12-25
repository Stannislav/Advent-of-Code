'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("input/24.txt", "utf-8");
const initialMap = data.trim().split('\n').map(s => [...s]);
let H = initialMap.length;
let W = initialMap[0].length;
let HC = Math.floor(H / 2);
let WC = Math.floor(W / 2);


// Definitions
function getDiversity(map) {
    let diversity = 0;
    map.flat().forEach(
        (c, i) => diversity |= (c === '#') << i
    );
    return diversity;
}

function cloneMatrix(matrix) {
    return matrix.map(row => [...row])
}

function step(map) {
    let newMap = cloneMatrix(map);

    for (let i = 0; i < H; ++i) {
        for (let j = 0; j < W; ++j) {
            let score = 0;
            let field = map[i][j];
            for (let [di, dj] of [[1, 0], [-1, 0], [0, 1], [0, -1]]) {
                if (i + di in map)
                    score += map[i + di][j + dj] === '#';
            }
            if (field === '#' && score !== 1)
                field = '.';
            else if (field === '.' && (score === 1 || score === 2))
                field = '#';
            newMap[i][j] = field;
        }
    }

    return newMap;
}


let neighbourCache = {};

function getNeighbours(i, j, level) {
    if (!([i, j, level] in neighbourCache)) {
        let neighbours = [];

        for (let [di, dj] of [[1, 0], [-1, 0], [0, 1], [0, -1]]) {
            if (i + di === -1)  // top outside edge
                neighbours.push([HC - 1, WC, level + 1]);
            else if (i + di === H)  // bottom outside edge
                neighbours.push([HC + 1, WC, level + 1]);
            else if (j + dj === -1)  // left outside edge
                neighbours.push([HC, WC - 1, level + 1]);
            else if (j + dj === W)  // right outside edge
                neighbours.push([HC, WC + 1, level + 1]);
            else if (i + di === HC && j + dj === WC) {  // inside edge
                if (i === HC - 1)  // point left of the center
                    for (let jj = 0; jj < W; ++jj)
                        neighbours.push([0, jj, level - 1]);
                else if (i === HC + 1)  // point right of the center
                    for (let jj = 0; jj < W; ++jj)
                        neighbours.push([H - 1, jj, level - 1]);
                else if (j === WC - 1)  // point above the center
                    for (let ii = 0; ii < H; ++ii)
                        neighbours.push([ii, 0, level - 1]);
                else if (j === WC + 1)  // point below the center
                    for (let ii = 0; ii < H; ++ii)
                        neighbours.push([ii, W - 1, level - 1]);
            } else // regular case
                neighbours.push([i + di, j + dj, level]);
        }

        // Remove entries with levels outside of min/max level.
        neighbourCache[[i, j, level]] = neighbours.filter(
            ([_i, _j, level]) => level >= minLevel && level <= maxLevel);
    }

    return neighbourCache[[i, j, level]];
}


function recursiveStep(map, t) {
    // Clone previous state
    let newMap = {};
    for (let [k, v] of Object.entries(map))
        newMap[k] = cloneMatrix(v);

    // Do the step
    for (let level = -Math.ceil(t / 2) - 1; level <= Math.ceil(t / 2) + 1; ++level) {
        for (let i = 0; i < H; ++i) {
            for (let j = 0; j < W; ++j) {
                // Skip the center tile
                if (i === HC && j === WC) {
                    newMap[level][i][j] = '?';
                    continue;
                }

                // Compute score
                let score = 0;
                for (let [iN, jN, levelN] of getNeighbours(i, j, level))
                    score += map[levelN][iN][jN] === '#';

                // Update map
                if (map[level][i][j] === '#' && score !== 1)
                    newMap[level][i][j] = '.';
                else if (map[level][i][j] === '.' && (score === 1 || score === 2))
                    newMap[level][i][j] = '#';
            }
        }
    }

    return newMap;
}


function countBugs(map) {
    let count = 0;

    for (let levelMap of Object.values(map)) {
        count += levelMap
            .flat()
            .map(c => c === '#' ? 1 : 0)
            .reduce((a, b) => a + b, 0);
    }
    return count;
}


// Part 1
let diversities = new Set();
let diversity;
let map = cloneMatrix(initialMap);

// Run the time loop
while (true) {
    diversity = getDiversity(map);
    if (diversities.has(diversity))
        break;
    diversities.add(diversity);
    map = step(map);
}

console.log("Part 1:", diversity);


// Part 2
/*
A bug takes 2 steps to go form the outer edge to the inner tile, so
after 200 steps the bugs will at most have spread between levels
-100 and +100
 */

let maxTime = 200;
let minLevel = -Math.ceil(maxTime / 2) - 1;
let maxLevel = Math.ceil(maxTime / 2) + 1;

// Initialise map. Level 0 with given input, other levels with empty worlds
let recursiveMap = {};
for (let level = minLevel; level <= maxLevel; ++level)
    recursiveMap[level] = new Array(H)
        .fill(0)
        .map(_ => new Array(H).fill('.'));
recursiveMap[0] = cloneMatrix(initialMap);

// Run the time loop
for (let t = 0; t < maxTime; ++t)
    recursiveMap = recursiveStep(recursiveMap, t);

console.log("Part 2:", countBugs(recursiveMap));

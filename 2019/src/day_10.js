'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("../input/input_10.txt", "utf-8");
const map = data.trim().split('\n').map(line => line.trim().split(""));


// Solution
function gcd(a, b) {
    // Euclidean algorithm for GCD
    if (a === 0)
        return b;
    if (b === 0)
        return a;
    if (a > b)
        return gcd(a - b, b);
    else
        return gcd(a, b - a);
}


function canSee(sRow, sCol, aRow, aCol) {
    /**
     * Can the asteroid at [aRow, aCol] be seen from [sRow, sCol]?
     */

    // Vertical and horizontal distances from station (S) to asteroid (A)
    let distRow = aRow - sRow;
    let distCol = aCol - sCol;

    /*
    To stay on the straight line from (A) to (S) we can move as follows:
        aRow => aRow - dRow
        aCol => aCol - dCol
    where
        dRow = distRow / k
        dCol = distCol / k
    But to stay on the grid dRow and dCol have to be integers. To make the smallest possible
    steps k has to be as big as possible, so it's the gcd of distRow and distCol. In fact,
    k counts in how many steps of length [dRow, dCol] we get from A to S.
     */
    let steps = gcd(Math.abs(distRow), Math.abs(distCol));
    let dRow = distRow / steps;
    let dCol = distCol / steps;

    // Now just iterate over all possible obstruction locations
    for (let i = 0; i < steps - 1; ++i) {
        aRow -= dRow;
        aCol -= dCol;
        if (map[aRow][aCol] === '#')
            return false;
    }

    return true;
}


// Part 1
let bestDetected = 0;  // Highest number of asteroids we can see
let bestPos = undefined;  // Position from where this is the case

// Iterate over positions of the monitoring station
for (let sRow = 0; sRow < map.length; ++sRow) {
    for (let sCol = 0; sCol < map[0].length; ++sCol) {
        if (map[sRow][sCol] !== '#')  // skip empty space
            continue;
        let detected = 0;
        // Iterate over all positions for the asteroids
        for (let aRow = 0; aRow < map.length; ++aRow) {
            for (let aCol = 0; aCol < map[0].length; ++aCol) {
                if (map[aRow][aCol] !== '#')  // skip empty space
                    continue;
                if (sRow === aRow && sCol === aCol)  // don't include own position
                    continue;
                if (canSee(sRow, sCol, aRow, aCol))
                    detected++;
            }
        }
        if (detected > bestDetected) {
            bestDetected = detected;
            bestPos = [sRow, sCol];
        }
    }
}

console.log("Part 1:", bestDetected);


// Part 2
let [sRow, sCol] = bestPos;  // optimal position found in part 1

/*
The logic here is that we just sort all possible locations by the order in which
they appear in the line of sight as the laser rotates clockwise. That means we
need to sort all point coordinates by the angle w.r.t. the station. If two
points are at the same angle, than the one that is closer to the station in terms
of the Manhattan distance should come first.
 */

// Pre-compute all angles for all coordinates
function getAngle(aRow, aCol) {
    return Math.PI - Math.atan2(aCol - sCol, aRow - sRow);
}

function getDistance(aRow, aCol) {
    return Math.abs(aRow - sRow) + Math.abs(aCol - sCol);
}

let coordinates = [];
for (let aRow = 0; aRow < map.length; ++aRow) {
    for (let aCol = 0; aCol < map[0].length; ++aCol) {
        if (sRow === aRow && sCol === aCol)
            continue;
        coordinates.push([aRow, aCol]);
    }
}
coordinates.sort(([r1, c1], [r2, c2]) =>
    getAngle(r1, c1) - getAngle(r2, c2) || getDistance(r1, c1) - getDistance(r2, c2)
);

// Now cycle through the sorted coordinates and eliminate the asteroids that are not obstructed
let count = 0;
let part2 = undefined;

outer:
while (true) {
    // The for-loop below completes a 360 degree rotation of the laser

    // Compute all points that are obstructed during this cycle and so must not be considered
    let invisible = coordinates.map(([row, col]) => !canSee(sRow, sCol, row, col));
    for (let i = 0; i < coordinates.length; ++i) {
        let [row, col] = coordinates[i];
        if (map[row][col] === '.' || invisible[i])
            continue;
        map[row][col] = '.';
        count++;
        if (count === 200) {
            part2 = col * 100 + row;
            break outer;
        }
    }
}
console.log("Part 2:", part2);

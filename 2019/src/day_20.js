'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("../input/input_20.txt", "utf-8");
let map = data.split('\n').filter(line => line.length > 0);


// Definitions
function parsePortals() {
    /**
     * Parse the map and extract all portal connections and their level changes
     * Return all connections, the starting, and the end point.
     */

    let WIDTH = map.length;
    let HEIGHT = map[0].length;

    let portals = {};
    let dLevels = {};
    let connections = {};

    let re1 = /^(\w\w)\./g;
    let re2 = / (\w\w)\./g;
    let re3 = /\.(\w\w) /g;
    let re4 = /\.(\w\w)$/g;
    let match;

    function recordPortal(name, x, y, dLevel=0) {
        if (name in portals)
            portals[name].push([x, y]);
        else
            portals[name] = [[x, y]];
        dLevels[[x, y]] = dLevel;
    }

    // Horizontal text
    for (let i = 0; i < WIDTH; ++i) {
        while ((match = re1.exec(map[i])) !== null)
            recordPortal(match[1], i, match.index + 2, -1);
        while ((match = re2.exec(map[i])) !== null)
            recordPortal(match[1], i, match.index + 3, 1);
        while ((match = re3.exec(map[i])) !== null)
            recordPortal(match[1], i, match.index, 1);
        while ((match = re4.exec(map[i])) !== null)
            recordPortal(match[1], i, match.index, -1);
    }

    // Vertical text - transpose map and do the same
    let mapT = [];
    for (let j = 0; j < HEIGHT; ++j) {
        let line = '';
        for (let i = 0; i < WIDTH; ++i)
            line += map[i][j];
        mapT.push(line);
    }

    for (let i = 0; i < WIDTH; ++i) {
        while ((match = re1.exec(mapT[i])) !== null)
            recordPortal(match[1], match.index + 2, i, -1);
        while ((match = re2.exec(mapT[i])) !== null)
            recordPortal(match[1], match.index + 3, i, 1);
        while ((match = re3.exec(mapT[i])) !== null)
            recordPortal(match[1], match.index, i, 1);
        while ((match = re4.exec(mapT[i])) !== null)
            recordPortal(match[1], match.index, i, -1);
    }

    // Assemble all portal connections and changes of level into a dictionary to be returned
    for (let points of Object.values(portals)) {
        if (points.length === 2) {
            connections[points[0]] = [points[1], dLevels[points[0]]];
            connections[points[1]] = [points[0], dLevels[points[1]]];
        }
    }

    return [connections, portals['AA'].pop(), portals['ZZ'].pop()];
}


function getConnections(pointFrom, level, flatMaze) {
    /**
     * Given a point at a given level find all possible continuation points.
     * The boolean `flatMaze` indicates whether or not we should stay on
     * level 0 (for part 1) or use all levels (part 2).
     */

    let result = [];

    // Given point connects to a portal
    if (pointFrom in connections) {
        let [nextPoint, dLevel] = connections[pointFrom];
        let nextLevel = level + dLevel;
        if (nextLevel >= 0)
            result.push([nextPoint, flatMaze ? 0 : nextLevel]);
    }
    let [x, y] = pointFrom;
    for (let [dx, dy] of [[1, 0], [-1, 0], [0, 1], [0, -1]]) {
        if (map[x + dx][y + dy] === '.') {
            result.push([[x + dx, y + dy], flatMaze ? 0 : level])
        }
    }

    return result;
}


function findDistance(pointFrom, pointTo, flatMaze) {
    /**
     * Given the coordinates of the staring and end points find the
     * shortest path between the two.
     */

    let distances = {};
    let queue = [{point: pointFrom, distance: 0, level: 0}];

    // Use BFS with a level-based priority queue to find the minimal path
    while (queue.length > 0) {
        // Imitate priority queue based on minimal level
        let minLevel = Infinity;
        let minIndex = undefined;
        for (let i = 0; i < queue.length; ++i) {
            if (queue[i].level < minLevel) {
                minLevel = queue[i].level;
                minIndex = i;
            }
        }
        let {point, distance, level} = queue.splice(minIndex, 1)[0];

        // Already found a shorter path to this point
        if ([point, level] in distances && distance >= distances[[point, level]])
            continue;

        // We already know a shorter distance to the final point than the current distance
        if (distance >= distances[[pointTo, 0]])
            continue;

        // Record current distance and put neighbouring points into the queue
        distances[[point, level]] = distance;
        getConnections(point, level, flatMaze).forEach(
            ([nextPoint, nextLevel]) => queue.push({
                point: nextPoint,
                distance: distance + 1,
                level: nextLevel})
        );
    }

    return distances[[pointTo, 0]];
}


// Solution
let [connections, pointAA, pointZZ] = parsePortals();
console.log("Part 1:", findDistance(pointAA, pointZZ, true));
console.log("Part 2:", findDistance(pointAA, pointZZ, false));

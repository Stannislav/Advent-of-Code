'use strict';


// Read Data
const fs = require('fs');
const data = fs.readFileSync("input/06.txt", "utf-8");
const lines = data.trim().split("\n");
let parents = {};
for (let i = 0; i < lines.length; ++i) {
    const [parent, child] = lines[i].split(")");
    parents[child] = parent;
}


// Part 1
let distances = {'COM': 0};


function get_distance(node) {
    /**
     *  Calculate the distance of given node to 'COM'
     */
    if (!(node in distances))
        distances[node] = get_distance(parents[node]) + 1;
    return distances[node];
}


let total_distances = 0;
for (let key in parents)
    total_distances += get_distance(key);
console.log("Part 1:", total_distances);


// Part 2
function get_path(node) {
    /**
     * Construct a list of nodes representing the path from node to 'COM'
     */
    let path = [node];
    while (node !== 'COM') {
        node = parents[node];
        path.push(node);
    }
    return path;
}


let path_you = get_path('YOU');
let path_san = get_path('SAN');
while (path_you.pop() === path_san.pop()) {}
console.log("Part 2:", path_you.length + path_san.length);

'use strict';

// Read data
const fs = require('fs');
const data = fs.readFileSync("input/03.txt", "utf-8");
let wires = data.trim().split('\n').map(s => s.trim().split(','));


function trace_wire(wire) {
    /*
    For each point of the wire record its (x, y) coordinates and the number of steps
    it takes to get there.

    The entries in the trace are of the form:

        trace["(x, y)"] = [x, y, n_steps]

    If a point has been visited before, then the entry is not updated so that if a point
    was visited multiple times, only the minimal number of steps is recorded.
     */

    let pos = [0, 0];
    let total_steps = 0;
    let trace = {};

    wire.forEach(item => {
        let dir = item[0];
        let steps = Number(item.substr(1));
        for (let i = 0; i < steps; ++i) {
            switch (dir) {
                case 'R':
                    pos[0] += 1;
                    break;
                case 'L':
                    pos[0] -= 1;
                    break;
                case 'U':
                    pos[1] += 1;
                    break;
                case 'D':
                    pos[1] -= 1;
                    break;
            }
            total_steps += 1;
            let key = pos.toString();
            if (!(key in trace)) {
                trace[key] = [...pos, total_steps];
            }
        }
    });

    return trace;
}

// Solve
let trace0 = trace_wire(wires[0]);
let trace1 = trace_wire(wires[1]);
let intersection = Object.keys(trace0).filter(x => x in trace1);
let x_dist = intersection.map(x => Math.abs(trace0[x][0]) + Math.abs(trace0[x][1]));
let x_steps = intersection.map(x => trace0[x][2] + trace1[x][2]);

console.log("Part 1:", Math.min(...x_dist));
console.log("Part 2:", Math.min(...x_steps));

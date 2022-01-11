"use strict";

main()

function main() {
    // Read input data
    const fs = require("fs");
    const data = fs.readFileSync("input/17.txt", {encoding: "utf-8"});
    const program = data.split(",").map(Number);

    // Get an intcode compute instance
    const IntcodeComputer = require("./intcode_computer");
    const computer = new IntcodeComputer();

    // Part 1
    computer.launch(program);
    let scaffolds = computer.getAllOutputs(true).trim().split("\n");
    let part1 = 0;
    for (let i = 1; i < scaffolds.length - 1; ++i) {
        for (let j = 1; j < scaffolds[0].length - 1; ++j) {
            if (scaffolds[i][j] !== "#")
                continue;
            let neighbours = [[i + 1, j], [i, j + 1], [i - 1, j], [i, j - 1]];
            if (neighbours.every(([x, y]) => scaffolds[x][y] === "#"))
                part1 += i * j;
        }
    }
    console.log("Part 1:", part1);

    // Part 2
    let route = find_route(scaffolds);
    let [main_routine, A, B, C] = find_inputs(route);
    let video = "n";
    let inputs = [
        main_routine.join(","),
        A.join(","),
        B.join(","),
        C.join(","),
        video,
    ].join("\n") + "\n";
    computer.launch(program, {inputs, patch: {0: 2}, ascii: true});
    let last_output = "";
    while (computer.hasOutput())
        last_output = computer.getOutput();
    console.log("Part 2:", last_output);
}

/**
 * Find the shortest route through the scaffolds.
 * @param {string[]} scaffolds - The 2D scaffold map as returned by the robot.
 * @returns {string[]} - The sequence of instructions of the form
 *   `["L", "10", "R", "12", ...]` that describe the route.
 */
function find_route(scaffolds) {
    // Naming:
    // * ni: the i-dimension of the scaffolding array
    // * pi: the ith coordinate of the robot
    // * i: a temporary ith coordinate
    // * dir: the robot direction
    // (nj, pj, j) analogously
    const [ni, nj] = [scaffolds.length, scaffolds[0].length];
    const dirs = [[1, 0], [0, 1], [-1, 0], [0, -1]];
    const init_dir = {"v": 0, ">": 1, "^": 2, "<": 3};

    // Find the starting point and direction.
    let [pi, pj, dir] = [-1, -1, -1];
    outer:
    for (let i = 0; i < ni; ++i) {
        for (let j = 0; j < nj; ++j) {
            let c = scaffolds[i][j]
            if (init_dir.hasOwnProperty(c)) {
                [pi, pj] = [i, j];
                dir = init_dir[c];
                break outer;
            }
        }
    }
    let route = [];  // The route commands we'll find and return.

    // Helper functions
    /**
     * Check if the coordinates (i, j) are within bounds of the scaffold map.
     */
    function in_bounds(i, j) {
        return (0 <= i) && (i < ni) && (0 <= j) && (j < nj);
    }

    /**
     * From (pi, pj) take one step in the given direction.
     * @param dir - The direction in which to go.
     * @returns {Number[]} - The new coordinates.
     */
    function step(dir) {
        let [di, dj] = dirs[dir];
        return [pi + di, pj + dj];
    }

    /**
     * Assuming we can't keep going straight on, try to turn left or right.
     *
     * This function has to side effects in case of a successful turn:
     * 1. Update `dir` to the new direction.
     * 2. Append the turning instruction to `route`.
     *
     * @returns {boolean} - Whether we were able to successfully turn. If
     *   false then this effectively means that we've reached the end of the
     *   path.
     */
    function turn() {
        let new_dirs = {
            L: (dir + 1) % 4,
            R: (dir - 1 + 4) % 4,
        };
        for (let [cmd, new_dir] of Object.entries(new_dirs)) {
            let [i, j] = step(new_dir);
            if (in_bounds(i, j) && (scaffolds[i][j] === "#")) {
                route.push(cmd);
                dir = new_dir
                return true
            }
        }
        return false
    }

    // Find the route by alternating between turning left or right to align
    // with the scaffolding and going straight on until another turn is
    // required.
    while (turn()) {
        // just turned, now go straight on until have to turn again.
        let dist = 0;
        let [i, j] = step(dir);
        while (in_bounds(i, j) && (scaffolds[i][j] === "#")) {
            dist += 1;
            [pi, pj] = [i, j];
            [i, j] = step(dir);
        }
        route.push(dist.toString());
    }
    return route;
}

/**
 * Given the complete route find the intcode computer inputs.
 *
 * The route has to be formulated in terms of three chunks A, B, and C, which
 * have to be determined first.
 *
 * The lengths of the main routine and the chunks is limited to 20, which
 * includes the commas separating the instruction. So the number of instructions
 * without counting the commas is at most 10.
 *
 * We realise that the route will start with an A-chunk and end with a C-chunk.
 * So we already know the start of the A-chunk and the end of the C-chunk
 * sequence. This allows us to try all lengths of the A and C chunks up to
 * length 10 and to determine where in the route sequence they can be found.
 *
 * For each choice of the lengths of A and C we find the placement of the
 * chunks in the route and check that they don't overlap. The parts of the route
 * that are not covered must then be the B-chunks. If they are all equal then
 * we have the solution. In theory two consecutive B-chunks could form a
 * chunk twice as long, so normally we'd have to try and split all B-chunk
 * candidates to a common length, but fortunately this doesn't happen in the
 * current puzzle.
 *
 * @param {string[]} route - The route found by `find_route`.
 * @returns {string[][]} - An array containing 4 arrays: the main routine, and
 *   the A, B, and C chunks. The main routine is a sequence of the letters
 *   representing the chunks, e.g. `["A", "C", "B", "A", ...]`, while each
 *   chunk is a subsequence of route, i.e. a sequence of robot driving
 *   instructions.
 */
function find_inputs(route) {
    // Find where A with lengths between 1 and 10 could fit;
    let A_len_pos = [];
    for (let len = 1; len <= 10; len++) {
        let A = route.slice(0, len);
        // Find all occurrences of this A in route
        let positions = [];
        for (let pos = 0; pos < route.length; ) {
            positions.push(pos)
            pos += len;
            // Advance to the next A-chunk
            while ((pos < route.length) && !arrayEqual(route.slice(pos, pos + len), A))
                pos += 1;
        }
        A_len_pos.push([len, positions]);
    }

    // Find where C with lengths between 1 and 10 could fit;
    let C_len_pos = [];
    for (let len = 1; len <= 10; len++) {
        let C = route.slice(-len);
        // Find all occurrences of this C in route
        let positions = [];
        for (let pos = route.length - len; pos >= 0; ) {
            positions.push(pos);
            pos -= len;
            // Advance to the next C-chunk
            while ((pos >= 0) && !arrayEqual(route.slice(pos, pos + len), C)) {
                pos -= 1;
            }
        }
        C_len_pos.push([len, positions.reverse()]);
    }

    // For all combinations of A and C lengths found above check if they
    // form a valid solution.
    for (let [len_A, pos_A] of A_len_pos) {
        c_loop:
        for (let [len_C, pos_C] of C_len_pos) {
            // Construct an array of sorted intervals for A and C chunks. The
            // interval array is of the form [{start: 0, end: 5, name: "A"},
            // {start: 5, end: 7, name: "B"}, ...],
            let intervals = [
                ...pos_A.map(x => ({start: x, end: x + len_A, name: "A"})),
                ...pos_C.map(x => ({start: x, end: x + len_C, name: "C"})),
            ]
            intervals.sort((e1, e2) => e1.start - e2.start);

            // If the intervals overlap then it's not a good solution
            for (let i = 0; i < intervals.length - 1; ++i) {
                if (intervals[i].end > intervals[i + 1].start)
                    continue c_loop;
            }

            // The gaps between A and C intervals must be Bs. Find all of them
            // and check if they're all the same.
            let Bs = [];
            let main_routine = ["A"];
            let pos = intervals[0].end;
            for (let next_interval of intervals.slice(1)) {
                let next_start = next_interval.start;
                if (pos < next_start) {  // gap found, insert B
                    let B = route.slice(pos, next_start);
                    if ((Bs.length > 0) && !arrayEqual(B, Bs[Bs.length - 1]))
                        continue c_loop;
                    Bs.push(B);
                    main_routine.push("B");
                }
                main_routine.push(next_interval.name)
                pos = next_interval.end;
            }

            // Check any Bs were found and other solution constraints
            if ((Bs.length === 0) || (Bs[0].length > 10) || (main_routine.length > 10))
                continue

            // Found the solution!
            let A = route.slice(0, len_A)
            let B = Bs[0];
            let C = route.slice(-len_C);
            return [main_routine, A, B, C];
        }
    }
    // No solution found
    return [[], [], [], []];
}

/**
 * Check if two arrays have the same content.
 * @param a - The first array.
 * @param b - The second array.
 * @returns {boolean} - Whether the arrays have the same content.
 */
function arrayEqual(a, b) {
    if (a.length !== b.length)
        return false;
    return a.every((value, i) => value === b[i]);
}
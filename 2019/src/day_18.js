"use strict";

// Define a meaningful hash function for sets of characters: a sorted
// concatenation of all elements. E.g. hash({"c", "1", "a", "5"}) = "15ac"
Set.prototype.toString = function() {return [...this].sort().join("")}

// Run the main entry point.
main()

// Definitions
/**
 * The main entry point.
 *
 * The only points we need to visit are the keys (a, b, c, ...), we DON'T
 * need to explicitly visit doors - we collect them on the way.
 */
function main() {
    // Read data
    const fs = require("fs");
    const data = fs.readFileSync("input/18.txt", {encoding: "utf-8"});
    let map = data.trim().split("\n").map(line => [...line])

    // Now we don't actually need the map anymore, just the distance graph
    // we constructed above.
    // Use this graph to solve the pathfinding problem through the state space
    // using dynamic programming
    let part1 = solve(map, ["@"]);
    console.log("Part 1:", part1);

    // Prepare the map for part 2
    map = patch_map(map);
    let part2 = solve(map, ["1", "2", "3", "4"]);
    console.log("Part 2:", part2);
}

/**
 * Find the minimal number of steps it takes to collect all keys.
 * @param {Array<Array<string>>} map - The puzzle map as a 2D array of characters.
 * @param {Array<string>} starts - The characters that are the starting points.
 * For part one it should be `["@"]`, for part 2 `["1", "2", "3", "4"]`. (For part 2
 * we replaced the four points marked with `"@"` by `"1"`, `"2"`, `"3"`, and
 * `"4"` to make them distinguishable. See `patch_map`.
 * @returns {number} - The solution.
 */
function solve(map, starts) {
    // Convert the 2D map to a graph of distances.
    // distances[a][b] = distances from key "a" to key "b".
    // doors[a][b] = the doors that are on the way from "a" to "b"
    let [distances, doors] = collect_keys_doors(map)

    // Solve using dynamic programming. The keys uniquely specify the state.
    // The state is the set of keys collected so far as well as the positions
    // of all robots. For example, if at a given point the set of collected
    // keys are {"a", "b", "c", "d"} and the two robots are at positions
    // "d" and "a", the corresponding key in "dp" will be "d,a,abcd".
    // The values are the distance from the given state to the solution state.
    let dp = {};

    // Initialise the DP with all possible final positions. The distances from
    // any of the final position to the solution is 0 because it's already the
    // solution.
    let final_positions = product(starts.map(start => Object.keys(distances[start])))
    let all_keys = new Set(Object.keys(distances));
    for (let pos of final_positions) {
        dp[[pos, all_keys].toString()] = 0;
    }

    /**
     * Compute the shortest path to solution using dynamic programming.
     *
     * This function recursively fills the `dp` array. Because of recursion
     * and DP when computing the distance to solution after n steps, the
     * solution after n+1 steps is already known. That is, the DP array
     * is filled backwards, starting with the final position after some N
     * steps, then the optimal solution for N-1 steps, then N-2, etc. Given
     * that the solution for n+1 steps is known, the solution for n steps is
     * found by trying all possible steps and computing the resulting distance
     * as the sum of the distances of the step taken plus the distances from
     * the n+1 solution.
     *
     * @param {Array<string>}pos - The current locations of the robots.
     * @param {Set<string>} keys_collected - The keys collected so far
     * (including all keys in `pos`).
     * @returns {number} - The number of robot steps it takes to collect all
     * keys from the starting position provided.
     */
    function shortest_path(pos, keys_collected) {
        let entry = [pos, keys_collected].toString();
        if (dp.hasOwnProperty(entry))
            return dp[entry];

        // Try to move each of the 4 robots by one step
        let dists = []
        for (let i = 0; i < starts.length; ++i) {
            let from = pos[i];
            // Make a copy of the current position. This will be altered to
            // represent the new position.
            let new_pos = [...pos];

            for (let to of next_keys(from, distances[from], doors[from], keys_collected)) {
                // Update the position of the robot we're moving
                new_pos[i] = to
                let new_keys_collected = new Set([to, ...keys_collected]);
                let dist = distances[from][to] + shortest_path(new_pos, new_keys_collected);
                dists.push(dist);
            }
        }
        dp[entry] =  Math.min(...dists);

        return dp[entry];
    }

    return shortest_path(starts, new Set(starts));
}

/**
 * Modify the map for part 2 by inserting a given 3-by-3 patch.
 * @param {Array<Array<string>>} map - A 2D array of characters from the
 * original puzzle input map.
 * @returns {Array<Array<string>>} - The modified map.
 */
function patch_map(map) {
    // First find where the original start is
    let keys = find_keys(map);
    let [i, j] = keys["@"];
    // We replace the four "@" markers by the digits 1-4 to distinguish the
    // robots.
    let patch = [
        ["1", "#", "2"],
        ["#", "#", "#"],
        ["3", "#", "4"],
    ];
    patch.forEach((line, di) => {
        line.forEach((c, dj) => {
            map[i + di - 1][j + dj - 1] = c;
        })
    })

    return map;
}

/**
 * Given a 2D map find the starting point and all keys.
 * @param {Array<Array<string>>} map - A 2D array representing the map.
 * @returns {Object<Array<number>>} - An object with property names being the
 * symbols on the map, and the values being the 2D coordinates on the map.
 */
function find_keys(map) {
    let keys = {}
    map.forEach((line, i) => {
        line.forEach((c, j) => {
            if (c === "@" || c.match("[a-z0-9]"))
                keys[c] = [i, j]
        });
    });

    return keys;
}

/**
 *
 * @param {Array<Array<string>>} map - The puzzle map
 * @returns - An array with two elements: `distances` and `doors`. The
 * `distances` array specifies the distance between any two keys, i.e.
 * `distances[from][to]` is the distances. The `doors` array contains the
 * doors that need to be unlocked to be able to go from one point to another.
 * For example, if `doors[from][to] = Set(["a", "c", "f"])`, then in order to
 * go from `from` to `to` the doors `A`, `C`, and `F` need to be unlocked. Note
 * that the door names are lower-cased, which makes it more convenient for
 * matching with the keys that need to be collected.
 */
function collect_keys_doors(map) {
    // for all key points in the map compute the mutual distance. This will
    // give us the graph that we have to navigate.
    // First find all points of interest
    let keys = find_keys(map);

    // For each given key compute the distances to all other keys
    let distances = {};
    let doors = {};
    Object.entries(keys).forEach(([key, coord]) => {
        [distances[key], doors[key]] = get_distances(coord, map);
    });

    return [distances, doors];
}

/**
 * Get all keys that can be reached from a given key.
 *
 * This considers the state of the doors that have to be open to reach any
 * other key. All keys that have already been visited won't be considered.
 * @param key - The starting point.
 * @param distances - The pairwise distances between keys. See `collect_keys_doors`.
 * @param doors - The doors to unlock for pairwise key trajectories. See `collect_keys_doors`.
 * @param seen_keys - The set of keys already visited that shall not be
 * considered as new destinations.
 * @returns {string[]} - The keys that can be reached from the given position.
 */
function next_keys(key, distances, doors, seen_keys) {
    // Look through the distance graph and determine which other keys
    // can be visited because all doors on their way are open.
    let targets = [];
    for (let target of Object.keys(distances)) {
        // We're assuming that the current key ("key") is already in "seen_keys"
        // so that we don't go there again.
        if (!seen_keys.has(key))
            throw Error("POI is not in seen_keys");
        // If we've been to this point before then don't go there.
        if (seen_keys.has(target))
            continue;
        // We've found a POI that hasn't been visited before. Are all doors
        // to it open?
        if (is_subset(doors[target], seen_keys)) {
            targets.push(target)
        }
    }

    return targets
}

/**
 * Compute the distances to all keys from a given point on the map.
 *
 * This also records the doors that need to be passed on the way to each key.
 * @param start - The coordinates of the starting point.
 * @param {Array<Array<string>>} map - The puzzle map.
 * @returns - An array with two elements: `distances` and `doors`. The
 * `distances` object has distances to keys as values, the `doors` array has
 * sets of doors that need to be passed as values.
 */
function get_distances(start, map) {
    // We'll use a BFS search.
    let q = [[0, ...start, []]];  // distance, i, j, doors seen on the way
    let seen = new Set();
    let distances = {};
    let doors = {};

    while (q.length > 0) {
        let [dist, i, j, current_doors] = q.shift();

        // Check if already seen
        if (seen.has([i, j].toString())) {
            continue;
        } else {
            seen.add([i, j].toString())
        }

        // If we find a key then update the global distances and doors.
        if (map[i][j].match("[a-z]")) {
            distances[map[i][j]] = dist;
            doors[map[i][j]] = new Set(current_doors.map(x => x.toLowerCase()));
        } else if (map[i][j].match("[A-Z]")) {
            current_doors.push(map[i][j]);
        }

        // Push neighbour points to the queue.
        for (let [x, y] of [[i + 1, j], [i, j + 1], [i - 1, j], [i, j - 1]]) {
            if (map[x][y] !== "#") {
                q.push([dist + 1, x, y, [...current_doors]]);
            }
        }
    }
    return [distances, doors];
}

/**
 * Check if the set `s1` is a subset of `s2`.
 * @param s1 - The first set.
 * @param s2 - The second set.
 * @returns {boolean} - Whether `s1` is a subset of `s2`.
 */
function is_subset(s1, s2) {
    for (let e of s1)
        if (!s2.has(e))
            return false;
    return true;
}

/**
 * Compute a cartesian product of any number of arrays.
 * @param {Array<Array<*>>} arrays - An array of arrays of any length.
 * @returns {Array<Array<*>>} - A sequence (array) of the results of the product.
 */
function product(arrays) {
    let results = []

    if (arrays.length === 1) {
        // Recursion end
        for (let x of arrays[0])
            results.push([x]);
    } else {
        // Recursion step
        for (let arr of product(arrays.slice(0, arrays.length - 1))) {
            for (let nxt of arrays[arrays.length - 1]) {
                results.push([...arr, nxt]);
            }
        }
    }

    return results;
}
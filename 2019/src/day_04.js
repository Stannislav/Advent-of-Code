'use strict';


// Read data
const fs = require('fs');
const data = fs.readFileSync("input/04.txt", "utf-8").trim();
const [n_from, n_to] = data.split("-").map(Number);


function check_arr(arr) {
    let part1 = 0;
    let part2 = 0;

    // Is the number in the range?
    let num = Number(arr.map(String).join(""));
    if (num < n_from || num > n_to)
        return [part1, part2];

    // Check the rules
    for (let i = 0; i < arr.length - 1; ++i) {
        let di = 1; // offset w.r.t "i"
        while (i + di < arr.length && arr[i + di] === arr[i])
            ++di;
        if (di === 2)
            part2 = 1;
        if (di >= 2)
            part1 = 1;
        if (part1 && part2)
            break;
        i += di - 1;
    }

    return [part1, part2];
}


// Solve
let cnt1 = 0;
let cnt2 = 0;
for (let i1 = 1; i1 < 7; ++i1) {
    for (let i2 = i1; i2 < 10; ++i2) {
        for (let i3 = i2; i3 < 10; ++i3) {
            for (let i4 = i3; i4 < 10; ++i4) {
                for (let i5 = i4; i5 < 10; ++i5) {
                    for (let i6 = i5; i6 < 10; ++i6) {
                        const [part1, part2] = check_arr([i1, i2, i3, i4, i5, i6]);
                        cnt1 += part1;
                        cnt2 += part2;
                    }
                }
            }
        }
    }
}

console.log("Part 1:", cnt1);
console.log("Part 2:", cnt2);
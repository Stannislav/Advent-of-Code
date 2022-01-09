"use strict";

main();


function main() {
    // Imports
    const assert = require("assert");
    const fs = require("fs");

    // Read data
    const DATA = fs.readFileSync("input/16.txt", {encoding: "utf-8"});
    const SIGNAL = DATA.trim().split("").map(c => parseInt(c));

    // Part 1
    let signal = [...SIGNAL];
    for (let i = 0; i < 100; ++i)
        signal = fft(signal);
    console.log("Part 1:", signal.slice(0, 8).join(""));

    // Part 2
    let N = SIGNAL.length;
    let offset = Number.parseInt(SIGNAL.slice(0, 7).join(""));
    // Because the FFT map is upper-triangular, to compute X_k one only needs
    // to know x_n for n >= k. That remains true no matter how many times one
    // applies. So we optimize by only considering the signal values with
    // index >= offset.
    // Because the first 7 digits determine the offset, it holds that k > N/2
    // for all x_k that we have to compute.
    assert(offset > N / 2);  // The simplification only works for k > N/2
    // This simplifies the computation a lot because for k > N/2 the FFT is an
    // upper triangular matrix with all values in the upper triangle being "1".
    // So it holds that:
    // X_N = x_n
    // X_{N-1} = x_n + x_{n-1}
    //         = X_N + x_{n-1}
    // etc.
    // =>
    // X_k = X_{k+1} + x_k
    // So the easiest is to compute the values starting from the end. Therefore,
    // we reverse the signal array.

    // Compute the tail of the signal repeated 10000 times starting from offset.
    let start = offset % N;  // Which signal value will be the first one?
    let n_repeat = (10000 * N - offset - (N - start)) / N;
    let tail = [SIGNAL.slice(start), ...new Array(n_repeat).fill(SIGNAL)].flat().reverse();
    assert(offset + tail.length === 10000 * N);

    // Compute the actual FFT
    for (let i = 0; i < 100; ++i) {
        // A neat way of computing a rolling sum.
        let acc = 0;
        tail = tail.map(x => Math.abs(acc += x) % 10);
    }
    console.log("Part 2:", tail.slice(-8).reverse().join(""));
}

/**
 * Compute the FFT transform of the signal
 * @param {Number[]} signal - The signal.
 * @returns {Number[]} - The transformed signal.
 */
function fft(signal) {
    const sum = arr => arr.reduce((a, b) => a + b, 0);

    let result = [];
    for (let i = 0; i < signal.length; ++i) {  // Compute the x_i
        let k = i + 1;
        let x_k = 0;
        for (let j = i; j < signal.length; j += 4 * k) {  // phases
            x_k += sum(signal.slice(j, j + k));
            x_k -= sum(signal.slice(j + 2 * k, j + 3 * k));
        }
        result.push(Math.abs(x_k) % 10)
    }
    return result;
}

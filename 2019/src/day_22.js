'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("../input/input_22.txt", "utf-8");


// Parse instructions
let instructions = [];
for (let line of data.trim().split('\n')) {
    if (line.startsWith('deal into'))
        instructions.push(['reverse']);
    else if (line.startsWith('cut'))
        instructions.push(['cut', Number(line.split(' ').pop())]);
    else if (line.startsWith('deal with'))
        instructions.push(['increment', Number(line.split(' ').pop())]);
    else
        throw Error;
}


// Definitions
function shuffleOnce(deckSize) {
    let [a, b] = [1, 0];
    let op;

    for (let i = 0; i < instructions.length; ++i) {
        op = instructions[i][0];
        if (op === 'reverse') {
            a = - a;
            b = - b - 1;
        } else if (op === 'cut') {
            let n = instructions[i][1];
            b -= n
        } else if (op === 'increment') {
            let n = instructions[i][1];
            a *= n;
            b *= n;
        }
        while (b < 0)
            b += deckSize;
        a = (a + deckSize) % deckSize;
        b = (b + deckSize) % deckSize;
    }
    
    return [a, b];
}


function repeatShuffle(a, b, n, deckSize) {
    let aNew = modPow(a, n, deckSize);
    let bNew = modTimes(modGeometricSum(a, n, deckSize), b, deckSize);

    return [aNew, bNew];
}


function modSum(x, y, k) {
    let result = x + y;
    while (result < 0)
        result += k;
    return result % k;
}


function modTimes(x, y, k) {
    /** Compute (x * y) % k for large x and y
     *
     */
    let result = 0;
    x = x % k;
    y = y % k;

    while (y > 0) {
        if (y % 2 === 1)
            result = (result + x) % k;
        x = (x * 2) % k;
        y = Math.floor(y / 2);
    }

    return result;
}


function modInverse(a, k) {
    /**
     * Compute a^(-1) so that (a * a(-1)) mod k = 1
     *
     * This uses the Euclidean algorithm for the modular
     * multiplicative inverse.
     */
    a = a % k;

    function euclid(a, b) {
        // write a = b * k + r
        let r = a % b;
        let k = (a - r) / b;

        if (r === 0)  // end of recursion
            return [0, 1];

        let [x, y] = euclid(b, r);
        return [y, x - y * k];
    }
    return (euclid(k, a)[1] + k) % k;
}


function modPow(x, n, k) {
    /**
     * Compute (x^n) % k that works for very large n and k
     *
     * Write n = 2 * n' + r, then x^n = (x^2)^n' * x^r.
     * Now x' = x^2 and x^r are computable since r is either 0 or 1.
     * Then compute recursively x'^n' using the same method and accumulate.
     * To keep the numbers small we can compute mod k after each multiplication / power.
     */
    let acc = 1;
    x = x % k;

    while (n > 0) {
        let r = n % 2;
        n = (n - r) / 2;
        acc = modTimes(acc, x**r, k);
        x = modTimes(x, x, k);
    }

    return acc;
}


function modGeometricSum(x, n, k) {
    /**
     * Compute g(x, n, k) = [sum_{i = 0 ... n - 1} x^i] % k for very large n and k
     *
     * Write n = 2 * n' + r, then g = [g' + g' * x^n' + r * x^(n-1)] % k
     * where g' = g(x, n', k), which can be calculated recursively.
     */

    if (n === 1)
        return 1;

    let r = n % 2;
    let n_ = (n - r) / 2;
    let g_ = modGeometricSum(x, n_, k);

    return (g_ + modTimes(g_, modPow(x, n_, k), k) + r * modPow(x, n - 1, k)) % k;
}


/*
Solutions
---------

Idea: all shuffling operations can be thought of as modular
additions / multiplications (all operations are mod deckSize):

deal into new stack:
x -> - x - 1

cut N cards:
x -> x - N

deal with increment N:
x -> x * N

Because all additions and multiplications the position of any
card x after shuffling can be written as
x -> a * x + b

where a and b are some integers that are the same for all cards. So
it's sufficient to track how a and b get transformed by shuffling.

For part 2 we need to repeat the shuffle M times, so that
x -> a * x + b
  -> a * (a * x + b) + b
  -> etc

In total we get
a -> a^M
b -> (a^(M-1) + ... + a^0) * b  (geometric sum)

The challenge is to implement all operations in a modular way so
that they work for very large numbers without causing an overflow.

For the 2nd part one also needs to solve a * x + b = 2020 for x,
which requires the modular multiplicative inverse of a, which can
be computed using for example the Euclidean algorithm.
 */

let a, b;
let deckSize;
let repeats;

// Part 1
deckSize = 10007;
[a, b] = shuffleOnce(deckSize);
console.log("Part 1:", (a * 2019 + b) % deckSize);

// Part 2
deckSize = 119315717514047;
repeats = 101741582076661;
[a, b] = shuffleOnce(deckSize);
[a, b] = repeatShuffle(a, b, repeats, deckSize);
// Find x so that (a * x + b) % deckSize = 2020
console.log("Part 2:", modTimes(modSum(2020, - b, deckSize) , modInverse(a, deckSize), deckSize));

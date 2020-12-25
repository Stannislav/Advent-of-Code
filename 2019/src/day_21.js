'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("input/21.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Definitions
function decode(output) {
    return output.map(i => String.fromCharCode(i)).join('');
}

function encode(input) {
    return input.split('').map(c => c.charCodeAt(0));
}

function executeSpringScript(script, quiet=false) {
    droid.launch(program);
    let prompt = droid.getAllOutputs();  // droid asks for input
    droid.putInput(encode(script));  // give droid input
    droid.resume();
    let output = droid.getAllOutputs();  // read output message
    let damage = output.pop();  // in case of success last number is the damage count

    if (!quiet)
        console.log(decode(prompt) + script + decode(output));

    return damage;
}


// Solutions
const droid = new IntcodeComputer();

/*
PART 1:

Logic:
- The holes can be at most 3 steps wide
- There are 1 + 4 + 6 + 4 = 15 possible cases
- The following actions should be taken for each of those cases (W = walk, J = jump)
    ####  W

    ###.  W
    ##.#  J
    #.##  J
    .###  J

    ##..  W
    #..#  J
    ..##  J

    #.#.  W
    .#.#  J

    .##.  fall no matter what

    #...  W
    ...#  J

    ..#.  fall no matter what
    .#..  fall no matter what

- group all the jump cases together
    ABCD
    ##.#  J
    #.##  J
    #..#  J
    .###  J
    .#.#  J
    ..##  J
    ...#  J

    J = D & [(A & ((B & ^C) | ^B)) | ^A]
 */

let walkScript = [
    'NOT C T',
    'AND B T',
    'NOT B J',
    'OR T J',
    'AND A J',
    'NOT A T',
    'OR T J',
    'AND D J',
    'WALK\n',
].join('\n');

/*
PART 2:

Debug the fail case:

    ##.#  J

This fails for

    ##.#.##.

because after jumping the first four squares we end up with

    .##.

which is always a fail case because the first square is a hole,
so we have to jump it, but the last square, where we land,
is a hole too, so we fall anyway. So, in total, for ##.#.##.
we must not jump if the 5th and the 8th squares are holes.
In other words, jump if either of them is not a hole:

    ABCDEFGH
    ##.#.##. => J if (E | H)

So we refine the previous script to take this into account:

    ABCDEFGH
    ##.#xxxx  J if (E | H)
    #.##      J
    #..#      J
    .###      J
    .#.#      J
    ..##      J
    ...#      J

    J = D & [(A & ((B & ^C & (E | H)) | ^B)) | ^A]
 */

let runScript = [
    'OR E J',
    'OR H J',
    'NOT C T',
    'AND J T',
    'AND B T',
    'NOT B J',
    'OR T J',
    'AND A J',
    'NOT A T',
    'OR T J',
    'AND D J',
    'RUN\n',
].join('\n');

console.log("Part 1:", executeSpringScript(walkScript, true));
console.log("Part 2:", executeSpringScript(runScript, true));

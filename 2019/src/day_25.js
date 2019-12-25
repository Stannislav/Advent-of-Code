'use strict';


// Imports
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("../input/input_25.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Definitions
function runCmd(cmd) {
    computer.putInput(cmd.join('\n') + '\n', true);
    computer.resume();
    return computer.getAllOutputs(true);
}

function CLI(computer) {
    const readline = require('readline');
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout,
        prompt: "> ",
    });

    process.stdout.write(computer.getAllOutputs(true));
    rl.prompt();
    rl.on('line', line => {
        if (line === 'q')
            rl.close();
        process.stdout.write(runCmd([line]));
        if (computer.status === computer.HALTED)
            rl.close();
        rl.prompt();
    });
    rl.on('close', () => process.exit(0));
}

// Solutions
const interactive = false;
const computer = new IntcodeComputer();
computer.launch(program);

if (interactive)
    CLI(computer);
else {
    let collectAllItems = [
        "south", "south", "take tambourine", "north", "north",
        "west", "south", "take polygon", "north", "east",
        "north",
        "west", "take boulder", "east",
        "north", "take manifold",
        "north",  "take hologram", "south",
        "west", "take fuel cell",
        "south", "east", "south", "take fixed point", "north", "west", "north",
        "north", "take wreath",
        "east", "east",
    ];

    let inventory = [
        "tambourine", "hologram", "fuel cell", "wreath",
        "boulder", "fixed point", "manifold", "polygon",
    ];

    runCmd(collectAllItems);

    // Take items one by one and check if we're still too light
    runCmd(inventory.map(item => "drop " + item));

    let solution;
    let queue = inventory.map(item => [item]);
    while (!solution && queue.length) {
        let items = queue.shift();

        // Take items
        let outputs = runCmd(items.map(item => "take " + item).concat("north"));

        // Check if can pass
        if (outputs.indexOf("heavier") >= 0)  // too light, try taking more items
            inventory.filter(x => items.indexOf(x) === -1)
                     .forEach(x => queue.push(items.concat(x)));
        else if (outputs.indexOf("lighter") === -1)  // solution found
            solution = outputs.match(/[0-9]+/).shift();

        // Drop items
        runCmd(items.map(item => "drop " + item));
    }

    console.log("Solution:", solution);
}

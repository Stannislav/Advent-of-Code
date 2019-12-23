'use strict';


// Import
const fs = require('fs');
const IntcodeComputer = require('./intcode_computer');


// Read data
const data = fs.readFileSync("../input/input_23.txt", "utf-8");
const program = data.trim().split(',').map(Number);


// Definitions
function runNetwork(nat, networkSize=50) {
    let computers = new Array(networkSize);
    let queue = new Array(networkSize);

    //Initialise computers and queue
    for (let i = 0; i < networkSize; ++i) {
        computers[i] = new IntcodeComputer();
        computers[i].launch(program, {inputs: [i]});
        queue[i] = [];
    }

    // Run event loop
    while(!nat.finished) {
        // Process outputs
        for (let i = 0; i < networkSize; ++i) {
            let outputs = computers[i].getAllOutputs();

            while (outputs.length > 0) {
                let [address, x, y] = outputs.splice(0, 3);
                if (address === 255)
                    nat.receive(x, y);
                else
                    queue[address].push(x, y);
            }
        }

        // Process message queue
        for (let i = 0; i < networkSize; ++i) {
            while (queue[i].length > 0) {
                computers[i].putInput(queue[i].shift());  // x
                computers[i].putInput(queue[i].shift());  // y
            }
            computers[i].putInput(-1);
            computers[i].resume();
        }

        // NAT action
        nat.callback(computers, queue);
    }
}


// Solutions
class NAT1 {
    receive(x, y) {
        this.y = y;
        this.finished = true;
    }

    callback(computers, queue) {}
}

class NAT2 {
    receive(x, y) {
        this.x = x;
        this.y = y;
    }

    callback(computers, queue) {
        if (!computers.some(computer => computer.hasOutput())) {
            queue[0].push(this.x, this.y);
            if (this.y === this.lastY)
                this.finished = true;
            this.lastY = this.y;
        }
    }
}

// Part 1
const nat1 = new NAT1();
runNetwork(nat1);
console.log("Part 1:", nat1.y);

// Part 2
const nat2 = new NAT2();
runNetwork(nat2);
console.log("Part 2:", nat2.y);

class IntcodeComputer {
    n_inst = {
        1: 4,  // add
        2: 4,  // multiply
        3: 2,  // input
        4: 2,  // output
        5: 3,  // jump-if-true
        6: 3,  // jump-if-false
        7: 4,  // less than
        8: 4,  // equals
        99: 1, // halt
    };

    HALTED = 0;
    RUNNING = 1;
    PAUSED = 2;


    constructor() {
        this.inputs = [];
        this.outputs = [];
        this.ptr = null;
        this.program = null;
        this.status = this.HALTED;
    }

    receiveInput(input) {
        this.inputs.push(input);
    }

    receiveInputs(arr) {
        this.inputs = this.inputs.concat(arr);
    }

    getValue(arg, mode) {
        if (mode === 0)
            return this.program[arg];
        else if (mode === 1)
            return arg;
        else
            throw "Bad mode";
    }

    parseInstruction(instruction) {
        let op = instruction % 100;
        instruction = Math.floor(instruction / 100);
        let modes = [];
        for (let i = 0; i < this.n_inst[op] - 1; ++i) {
            modes.push(instruction % 10);
            instruction = Math.floor(instruction / 10);
        }

        return [op, modes];
    }

    step() {
        // Parse op and mode
        const [op, modes] = this.parseInstruction(this.program[this.ptr]);
        const args = this.program.slice(this.ptr + 1, this.ptr + this.n_inst[op]);

        // Retrieve values based on modes
        const vals = args.map((arg, i) => this.getValue(arg, modes[i]));

        // Execute op
        switch (op) {
            case 1:
                this.program[args[2]] = vals[0] + vals[1];
                break;
            case 2:
                this.program[args[2]] = vals[0] * vals[1];
                break;
            case 3:
                if (this.inputs.length === 0) {
                    this.status = this.PAUSED;
                    return;
                } else
                    this.program[args[0]] = this.inputs.shift();
                break;
            case 4:
                this.outputs.push(vals[0]);
                break;
            case 5:
                if (vals[0] !== 0) {
                    this.ptr = vals[1];
                    return;
                }
                break;
            case 6:
                if (vals[0] === 0) {
                    this.ptr = vals[1];
                    return;
                }
                break;
            case 7:
                if (vals[0] < vals[1])
                    this.program[args[2]] = 1;
                else
                    this.program[args[2]] = 0;
                break;
            case 8:
                if (vals[0] === vals[1])
                    this.program[args[2]] = 1;
                else
                    this.program[args[2]] = 0;
                break;
            case 99:
                this.status = this.HALTED;
                return;
            default: throw `Unknown op: ${op}`;
        }

        this.ptr += this.n_inst[this.program[this.ptr] % 100];
    }

    launch(program, inputs=[]) {
        this.inputs = inputs;
        this.outputs = [];
        this.program = [...program];  // shallow copy
        this.ptr = 0;

        return this.resume();
    }

    resume() {
        this.status = this.RUNNING;
        do {
            this.step();
        } while(this.status === this.RUNNING);

        return this.status;
    }
}

module.exports = IntcodeComputer;

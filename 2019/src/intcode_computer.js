'use strict';


class IntcodeComputer {

    constructor() {
        this.arity = {
            1: 3,  // add
            2: 3,  // multiply
            3: 1,  // input
            4: 1,  // output
            5: 2,  // jump-if-true
            6: 2,  // jump-if-false
            7: 3,  // less than
            8: 3,  // equals
            9: 1,  // relative base
            99: 0, // halt
        };

        this.HALTED = 0;
        this.RUNNING = 1;
        this.WAITING = 2;

        this.statusText = {
            [this.HALTED]: "HALTED",
            [this.RUNNING]: "RUNNING",
            [this.WAITING]: "WAITING"
        };

        this.inputs = [];
        this.outputs = [];
        this.ptr = null;
        this.base = null;
        this.program = null;
        this.status = this.HALTED;
    }

    putInput(input) {
        this.inputs.push(input);
    }

    setInputs(inputs) {
        this.inputs = [...inputs];
    }

    hasOutput() {
        return this.outputs.length > 0;
    }

    getOutput() {
        return this.outputs.shift();
    }

    getAllOutputs() {
        let outputs = this.outputs;
        this.outputs = [];
        return outputs;
    }

    getStatusText() {
        return this.statusText[this.status];
    }

    getValue(arg, mode) {
        if (mode === 0) {
            if (this.program[arg] === undefined)
                this.program[arg] = 0;
            return this.program[arg];
        } else if (mode === 1)
            return arg;
        else if (mode === 2) {
            if (this.program[this.base + arg] === undefined)
                this.program[this.base + arg] = 0;
            return this.program[this.base + arg];
        } else
            throw `Bad mode in getValue: ${mode}`;
    }

    setValue(arg, mode, value) {
        if (mode === 0)
            this.program[arg] = value;
        else if (mode === 2)
            this.program[this.base + arg] = value;
        else
            throw `Bad mode in setValue: ${mode}`
    }

    parseInstruction(instruction) {
        let op = instruction % 100;
        instruction = Math.floor(instruction / 100);
        let modes = [];
        for (let i = 0; i < this.arity[op]; ++i) {
            modes.push(instruction % 10);
            instruction = Math.floor(instruction / 10);
        }

        return [op, modes];
    }

    step() {
        // Parse op and mode
        const [op, modes] = this.parseInstruction(this.program[this.ptr]);
        const args = this.program.slice(this.ptr + 1, this.ptr + this.arity[op] + 1);

        // Retrieve values based on modes
        const values = args.map((arg, i) => this.getValue(arg, modes[i]));

        // Execute op
        switch (op) {
            case 1:
                this.setValue(args[2], modes[2], values[0] + values[1]);
                break;
            case 2:
                this.setValue(args[2], modes[2], values[0] * values[1]);
                break;
            case 3:
                if (this.inputs.length === 0) {
                    this.status = this.WAITING;
                    return;
                } else
                    this.setValue(args[0], modes[0], this.inputs.shift());
                break;
            case 4:
                this.outputs.push(values[0]);
                break;
            case 5:
                if (values[0] !== 0) {
                    this.ptr = values[1];
                    return;
                }
                break;
            case 6:
                if (values[0] === 0) {
                    this.ptr = values[1];
                    return;
                }
                break;
            case 7:
                this.setValue(args[2], modes[2], values[0] < values[1] ? 1 : 0);
                break;
            case 8:
                this.setValue(args[2], modes[2], values[0] === values[1] ? 1 : 0);
                break;
            case 9:
                this.base += values[0];
                break;
            case 99:
                this.status = this.HALTED;
                return;
            default: throw `Unknown op: ${op}`;
        }

        this.ptr += this.arity[this.program[this.ptr] % 100] + 1;
    }

    launch(program, {inputs=[], patch={}}={}) {
        this.inputs = inputs;
        this.outputs = [];
        this.program = [...program];  // shallow copy
        this.ptr = 0;
        this.base = 0;

        for (let [idx, value] of Object.entries(patch))
            this.program[idx] = value;

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

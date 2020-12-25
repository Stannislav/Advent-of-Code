'use strict';


// Imports
const fs = require('fs');


// Read data
const data = fs.readFileSync("input/14.txt", "utf-8");
/*
Save data in a dictionary using the following format:
    n A, m B => k C
becomes
    reactions[C] = [k, [[n, A], [m, B]]
Note that according to the problem statement each chemical is produced
by exactly one reaction except for ORE, which is not produced by anything,
this is why this representation works.
 */

let reactions = {};
for (let line of data.trim().split('\n')) {
    let children = [];
    for (let match of line.matchAll(/(?<num>\d+) (?<name>\w+)/g))
        children.push([Number(match.groups.num), match.groups.name]);
    let [rate, name] = children.pop();  // the r.h.s of the reaction
    reactions[name] = [rate, children];
}


// Definitions
function countOreForFuel(howMuchFuel) {
    let stash = {};  // save unused superfluous chemicals

    function countOreForChemical(chemical, howMuch) {
        // Recursion end
        if (chemical === 'ORE')
            return howMuch;

        // Get chemical from stash
        if (!(chemical in stash))
            stash[chemical] = 0;
        else {
            howMuch -= stash[chemical];
            stash[chemical] = 0;
        }
        if (howMuch <= 0) {  // stash has more than we need
            stash[chemical] = -howMuch;
            return 0;
        }

        // Recursively sum ore from child chemicals
        let [productionRate, constituents] = reactions[chemical];
        let multiplicity = Math.ceil(howMuch / productionRate);
        stash[chemical] += productionRate * multiplicity - howMuch;
        let oreFromChildren = constituents.map(([rate, name]) => {
            return countOreForChemical(name, rate * multiplicity)
        });

        return oreFromChildren.reduce((a, b) => a + b, 0);
    }

    return countOreForChemical('FUEL', howMuchFuel);
}


// Solutions
// Part 1
let oreForOneFuel = countOreForFuel(1);
console.log("Part 1:", oreForOneFuel);

// Part 2
let hasOre = 1e12;
let producedFuelEstimate = Math.floor(hasOre / oreForOneFuel);
let needOre;
while ((needOre = countOreForFuel(producedFuelEstimate)) < hasOre)
    producedFuelEstimate += Math.ceil((hasOre - needOre) / oreForOneFuel);
producedFuelEstimate--; // last iteration needed more ore than we have, so undo it
console.log("Part 2:", producedFuelEstimate);

import { readFileSync } from "fs";

type Bank = number[];

function maxJoltage(bank: Bank, numBatteries: number): number {
  if (numBatteries === 0) return 0;

  const firstDigit = Math.max(...bank.slice(0, bank.length - numBatteries + 1));
  const firstDigitIdx = bank.indexOf(firstDigit);
  const rest = maxJoltage(bank.slice(firstDigitIdx + 1), numBatteries - 1);

  return firstDigit * 10 ** (numBatteries - 1) + rest;
}

const solve = (banks: Bank[], numBatteries: number) =>
  banks.map((bank) => maxJoltage(bank, numBatteries)).reduce((a, b) => a + b, 0);
const part1 = (banks: Bank[]) => solve(banks, 2);
const part2 = (banks: Bank[]) => solve(banks, 12);

function main() {
  const banks = readFileSync("input/03.txt", "utf8")
    .trim()
    .split("\n")
    .map((line) => line.split("").map((c) => Number(c)));

  console.log("Part 1:", part1(banks));
  console.log("Part 2:", part2(banks));
}

main();

import { readFileSync } from "fs";

function apply(ops: string[], stacks: number[][]): number {
  let acc = 0;
  for (let c = 0; c < stacks.length; c++) {
    if (ops.at(c) === "+") {
      const result = stacks.at(c)?.reduce((a, b) => a + b, 0);
      if (result === undefined) throw Error("can't reduce with +");
      acc += result;
    } else if (ops.at(c) === "*") {
      const result = stacks.at(c)?.reduce((a, b) => a * b, 1);
      if (result === undefined) throw Error("can't reduce with *");
      acc += result;
    } else {
      throw Error(`unknown op: ${ops.at(c)}`);
    }
  }
  return acc;
}

function transpose<T>(matrix: T[][]): T[][] {
  const transposed: T[][] = [];
  const nCols = matrix.at(0)?.length;
  if (nCols === undefined) throw Error("can't determine the number of columns");
  const nRows = matrix.length;
  for (let c = 0; c < nCols; c++) {
    const col: T[] = [];
    for (let r = 0; r < nRows; r++) {
      const el = matrix.at(r)?.at(c);
      if (el === undefined) throw Error(`can't access matrix[${r}][${c}]`);
      col.push(el);
    }
    transposed.push(col);
  }
  return transposed;
}

function part1(numberGrid: string[], ops: string[]): number {
  const rows = numberGrid.map((line) => line.split(/\s+/).map(Number));
  return apply(ops, transpose(rows));
}

function part2(numberGrid: string[], ops: string[]): number {
  const columns = transpose(numberGrid.map((row) => row.split(""))).map((col) => col.join("").trim());

  const groups: number[][] = [];
  let currentGroup = [];
  for (const col of columns) {
    if (col.length === 0) {
      groups.push(currentGroup);
      currentGroup = [];
      continue;
    }
    currentGroup.push(Number(col));
  }
  groups.push(currentGroup);

  return apply(ops, groups);
}

function main() {
  const lines = readFileSync("input/06.txt", "utf-8").trim().split("\n");
  const numberGrid = lines.slice(0, -1);
  const ops = lines.at(-1)?.split(/\s+/);
  if (ops === undefined) throw Error("can't parse ops");

  console.log("Part 1:", part1(numberGrid, ops));
  console.log("Part 2:", part2(numberGrid, ops));
}

main();

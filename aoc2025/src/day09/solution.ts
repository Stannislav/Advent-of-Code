import { type Vec, type Edge, type Rect } from "./definitions.js";

export function parseInput(input: string): Vec[] {
  return input.trim().split("\n").map(parseVec);
}

export function part1(points: Vec[]): number {
  return maxArea(points);
}

export function part2(points: Vec[]): number {
  const edges = findEdges(points);
  return maxArea(points, edges);
}

function parseVec(s: string): Vec {
  const [left, right] = s.split(",");
  if (left === undefined || right === undefined) throw Error(`can't parse vector from ${s}`);
  return { x: Number(left), y: Number(right) };
}

/**
 * Find the biggest area of a rectangle formed by any two points
 * from the given point set. If a set of edges is given, then the
 * formed rectangles must not be crossed by any of the edges.
 * @param points - points for forming rectangles
 * @param edges - edges which must not intersect rectangles.
 * @returns the biggest rectangle area formed from points.
 */
function maxArea(points: Vec[], edges: Edge[] | null = null) {
  const areas = [];
  for (const p1 of points) {
    for (const p2 of points) {
      if (edges && edges.some((e) => doIntersect({ p1, p2 }, e as Rect))) continue;
      areas.push(area({ p1, p2 }));
    }
  }
  areas.sort((a, b) => a - b);
  return areas.at(-1) ?? 0;
}

function area({ p1, p2 }: Rect): number {
  return (Math.abs(p1.x - p2.x) + 1) * (Math.abs(p1.y - p2.y) + 1);
}

function doIntersect({ p1: a, p2: b }: Rect, { p1: c, p2: d }: Rect): boolean {
  const minX1 = Math.min(a.x, b.x);
  const maxX1 = Math.max(a.x, b.x);
  const minY1 = Math.min(a.y, b.y);
  const maxY1 = Math.max(a.y, b.y);

  const minX2 = Math.min(c.x, d.x);
  const maxX2 = Math.max(c.x, d.x);
  const minY2 = Math.min(c.y, d.y);
  const maxY2 = Math.max(c.y, d.y);

  if (maxX2 > minX1 && minX2 < maxX1 && maxY2 > minY1 && minY2 < maxY1) return true;

  return false;
}

function getCoord(v: Vec, isX: boolean): number {
  if (isX) return v.x;
  else return v.y;
}

function popAt<T>(arr: T[], idx: number): T {
  const [e] = arr.splice(idx, 1);
  if (!e) throw new Error(`can't pop from ${arr} at index ${idx}`);
  return e;
}

function findEdges(points: Vec[]): Edge[] {
  const pts = [...points];
  const n = points.length;
  const firstPoint = popAt(pts, 0);
  const orderedPoints = [firstPoint];

  let isX = true;
  let coord = getCoord(firstPoint, isX);
  while (orderedPoints.length < n) {
    for (const [idx, pt] of pts.entries()) {
      if (coord === getCoord(pt, isX)) {
        orderedPoints.push(pt);
        isX = !isX;
        coord = getCoord(pt, isX);
        popAt(pts, idx);
        break;
      }
    }
  }

  const edges: Edge[] = [];
  for (const [idx, p1] of orderedPoints.entries()) {
    const p2 = orderedPoints.at((idx + 1) % n);
    if (!p2) throw new Error(`can't get p2 from ordered points`);
    edges.push({ p1, p2 });
  }

  return edges;
}

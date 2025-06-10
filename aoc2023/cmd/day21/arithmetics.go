package main

import "image"

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func dist(pt1, pt2 image.Point) int {
	return abs(pt1.X-pt2.X) + abs(pt1.Y-pt2.Y)
}

func mod(pt, lim image.Point) image.Point {
	return image.Point{
		X: (pt.X%lim.X + lim.X) % lim.X,
		Y: (pt.Y%lim.Y + lim.Y) % lim.Y,
	}
}

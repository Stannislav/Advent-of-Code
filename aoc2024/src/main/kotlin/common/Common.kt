package common

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second
operator fun Vec.plus(other: Vec) = Vec(this.i + other.i, this.j + other.j)
operator fun Vec.minus(other: Vec) = Vec(this.i - other.i, this.j - other.j)
operator fun Vec.times(n: Int) = Vec(this.i * n, this.j * n)
fun Vec.mod(other: Vec) = Vec((this.i.mod(other.i)), this.j.mod(other.j))

val UP = Vec(-1, 0)
val DOWN = Vec(1, 0)
val LEFT = Vec(0, -1)
val RIGHT = Vec(0, 1)

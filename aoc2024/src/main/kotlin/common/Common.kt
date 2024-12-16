package common

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second
operator fun Vec.plus(other: Vec) = Vec(this.i + other.i, this.j + other.j)
operator fun Vec.minus(other: Vec) = Vec(this.i - other.i, this.j - other.j)
operator fun Vec.unaryMinus() = Vec(-this.i, -this.j)
operator fun Vec.times(n: Int) = Vec(this.i * n, this.j * n)
fun Vec.mod(other: Vec) = Vec((this.i.mod(other.i)), this.j.mod(other.j))
fun Vec.turn(n: Int): Vec {
    return when (n.mod(4)) {
        0 -> this
        1 -> Vec(-j, i)
        2 -> -this
        3 -> Vec(j, -i)
        else -> error("Invalid n: $n")
    }
}

val UP = Vec(-1, 0)
val DOWN = Vec(1, 0)
val LEFT = Vec(0, -1)
val RIGHT = Vec(0, 1)

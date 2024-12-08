package common

typealias Vec = Pair<Int, Int>
val Vec.i: Int get() = this.first
val Vec.j: Int get() = this.second
operator fun Vec.plus(other: Vec) = Vec(this.i + other.i, this.j + other.j)
operator fun Vec.minus(other: Vec) = Vec(this.i - other.i, this.j - other.j)

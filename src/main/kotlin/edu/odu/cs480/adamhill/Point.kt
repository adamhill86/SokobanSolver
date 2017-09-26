package edu.odu.cs480.adamhill

/**
 * A 2D point in space. Used to hold robot, box, etc. positions
 */
data class Point(var x: Int, var y: Int) {
    constructor(): this(0, 0) // empty constructor makes the point 0, 0

    override fun toString(): String = "($x, $y)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point
        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var hash = 397
        hash = 397 * hash + x.hashCode()
        hash = 397 * hash + y.hashCode()
        return hash
    }
}
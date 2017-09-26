package edu.odu.cs480.adamhill

/**
 * Contains a set of all obstacle positions in the map
 */
data class Barriers(val obstaclePositions: HashSet<Point>) {
    constructor(): this(hashSetOf<Point>())

    fun contains(element: Point): Boolean {
        return obstaclePositions.contains(element)
    }
}
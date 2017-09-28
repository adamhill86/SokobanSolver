package edu.odu.cs480.adamhill

class Robot(var position: Point) {
    constructor(): this(Point()) // initialize the robot to (0, 0) if no Point is given

    enum class Direction {NORTH, SOUTH, EAST, WEST}

    /**
     * Moves the robot 1 unit up
     */
    fun moveNorth() {
        position.y--
    }

    /**
     * Moves the robot 1 unit down
     */
    fun moveSouth() {
        position.y++
    }

    /**
     * Moves the robot 1 unit right
     */
    fun moveEast() {
        position.x++
    }

    /**
     * Moves the robot 1 unit left
     */
    fun moveWest() {
        position.x--
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Robot

        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

    override fun toString(): String = "Robot is at $position"
}
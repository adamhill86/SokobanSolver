package edu.odu.cs480.adamhill

/**
 * Maintains a record of where the robot, blocks, storage positions, and obstacles are
 */
data class State(val robot: Robot, val blockPositions: HashSet<Point>,
                 val storagePositions: HashSet<Point>) {
    constructor(): this(Robot(), hashSetOf<Point>(), hashSetOf<Point>())

    /**
     * Calculates whether the current state is the goal state.
     * For this to be true, the box positions must match the storage positions
     */
    fun isGoalState(): Boolean {
        blockPositions.forEach {
            if (!storagePositions.contains(it)) return false
        }
        return true
    }

    /**
     * Make a deep copy of all block positions
     */
    fun copyBlockPositions(): HashSet<Point> {
        val newPositions = hashSetOf<Point>()
        blockPositions.forEach { newPositions.add(it) }
        return newPositions
    }

    /**
     * Make a deep copy of all storage positions
     */
    fun copyStoragePositions(): HashSet<Point> {
        val newPositions = hashSetOf<Point>()
        storagePositions.forEach { newPositions.add(it) }
        return newPositions
    }

    /**
     * Compare this state to the goal state but only take into account block positions. Ignore the robot
     */
    fun compareToGoal(goal: State): Boolean {
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Robot: ").append(robot.position).append("\n").append("Blocks: ")
        blockPositions.forEach { sb.append(it).append(" ") }
        sb.append("\n").append("Storage: ")
        storagePositions.forEach { sb.append(it).append(" ") }
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (robot.position != other.robot.position) return false
        if (blockPositions != other.blockPositions) return false
        if (storagePositions != other.storagePositions) return false

        return true
    }

    override fun hashCode(): Int {
        var hash = 397
        hash = 397 * hash + robot.hashCode()
        hash = 397 * hash + blockPositions.hashCode()
        hash = 397 * hash + storagePositions.hashCode()
        return hash
    }
}
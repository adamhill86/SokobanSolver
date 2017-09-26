package edu.odu.cs480.adamhill
// TODO: Experiment with changing blockPositions and storagePositions to HashSets
/**
 * Maintains a record of where the robot, blocks, storage positions, and obstacles are
 */
data class State(val robot: Robot, val blockPositions: MutableList<Point>,
                 val storagePositions: MutableList<Point>) {
    constructor(): this(Robot(), mutableListOf<Point>(), mutableListOf<Point>())

    /**
     * Calculates whether the current state is the goal state.
     * For this to be true, the box positions must match the storage positions
     */
    fun isGoalState(): Boolean {
        blockPositions.forEach {
            if (!storagePositions.contains(it)) return false
        }
        // TODO: Write unit test for this. Check for reference vs value
        return true
    }

    /**
     * Make a deep copy of all block positions
     */
    fun copyBlockPositions(): MutableList<Point> {
        val newPositions = mutableListOf<Point>()
        blockPositions.forEach { newPositions.add(it) }
        return newPositions
    }

    /**
     * Make a deep copy of all storage positions
     */
    fun copyStoragePositions(): MutableList<Point> {
        val newPositions = mutableListOf<Point>()
        storagePositions.forEach { newPositions.add(it) }
        return newPositions
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
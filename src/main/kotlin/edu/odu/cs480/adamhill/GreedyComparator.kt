package edu.odu.cs480.adamhill

/**
 * This class represents the heuristic function and is implemented as a Comparator for the PriorityQueue.
 * It calculates two things:
 * First, it calculates the Manhattan distances between all blocks and storage spaces.
 * Second, it checks to see if a node is in a corner (that is, surrounded by obstacles on at least 2 adjacent sides.
 * If it is, it adds a very large arbitrary number to the score so the node gets pushed to the back of the queue.
 */
class GreedyComparator(private val barriers: Barriers):
        Comparator<Node<State>> {
    /**
     * This function determines the priority of states in the PriorityQueue.
     * First, the Manhattan distance is calculated for both nodes.
     * Then, we check to see if either node is in a corner.
     * If it is, add a very large number to it to force it to the back of the queue.
     * Otherwise, add 0.
     * @return 1 if the right-hand side node is greater than the left-hand side,
     * -1 if lhs is greater than rhs, 0 if they're equal
     */
    override fun compare(lhs: Node<State>, rhs: Node<State>): Int {
        val lhsManhattanDistance = manhattanDistance(lhs.value.blockPositions, lhs.value.storagePositions)
        val rhsManhattanDistance = manhattanDistance(rhs.value.blockPositions, rhs.value.storagePositions)
        val lhsTotal = lhsManhattanDistance + eliminateCornerStates(lhs.value)
        val rhsTotal = rhsManhattanDistance + eliminateCornerStates(rhs.value)

        if (lhsTotal < rhsTotal) return -1
        if (lhsTotal > rhsTotal) return 1

        return 0
    }

    /**
     * Calculates the Manhattan distance between all blocks and storage positions.
     */
    private fun manhattanDistance(blockPositions: HashSet<Point>, storagePositions: HashSet<Point>): Int {
        var sumDistance = 0

        for (blockPosition in blockPositions) {
            for (storagePosition in storagePositions) {
                sumDistance += Math.abs(blockPosition.x - storagePosition.x)
                sumDistance += Math.abs(blockPosition.y - storagePosition.y)
            }
        }

        return sumDistance
    }

    /**
     * If a block is found to be in a corner position, return a large number to ensure the state is not explored
     * @return An arbitrary large number if a block is in a corner, 0 otherwise
     */
    private fun eliminateCornerStates(state: State): Long {
        if (isBlockInCorner(state)) return Long.MAX_VALUE / 5  // arbitrary large number
        return 0
    }

    /**
     * Determine if a block is in a corner (surrounded on at least 2 adjacent sides by barriers).
     * Takes into account storage positions.
     */
    private fun isBlockInCorner(state: State): Boolean {
        state.blockPositions.forEach {
            // Check to make sure the block isn't in a storage position
            if (!state.storagePositions.contains(it)) {
                // check west and north
                if (barriers.contains(Point(it.x - 1, it.y)) && barriers.contains(Point(it.x, it.y - 1))) return true

                // check west and south
                if (barriers.contains(Point(it.x - 1, it.y)) && barriers.contains(Point(it.x, it.y + 1))) return true

                // check east and north
                if (barriers.contains(Point(it.x + 1, it.y)) && barriers.contains(Point(it.x, it.y - 1))) return true

                // check east and south
                if (barriers.contains(Point(it.x + 1, it.y)) && barriers.contains(Point(it.x, it.y + 1))) return true
            }
        }
        return false
    }
}
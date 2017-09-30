package edu.odu.cs480.adamhill

class AStarStateComparator(private val distance: HashMap<Node<State>, Int>, val barriers: Barriers):
        Comparator<Node<State>> {
    /**
     * Make sure you set pathCost before adding to the queue
     */
    override fun compare(lhs: Node<State>, rhs: Node<State>): Int {
        //println("LHS distance: ${distance[lhs]}")
        //println("RHS distance: ${distance[rhs]}")
        val lhsManhattanDistance = manhattanDistance(lhs.value.blockPositions, lhs.value.storagePositions)
        val rhsManhattanDistance = manhattanDistance(rhs.value.blockPositions, rhs.value.storagePositions)
        val lhsTotal = distance[lhs] as Int + lhsManhattanDistance + eliminateCornerStates(lhs.value)
        val rhsTotal = distance[rhs] as Int + rhsManhattanDistance + eliminateCornerStates(rhs.value)

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
    private fun eliminateCornerStates(state: State): Int {
        if (isBlockInCorner(state)) return Int.MAX_VALUE / 5  // arbitrary large number
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
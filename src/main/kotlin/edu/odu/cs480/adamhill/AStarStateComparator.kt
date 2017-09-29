package edu.odu.cs480.adamhill

class AStarStateComparator(private val distance: HashMap<Node<State>, Int>): Comparator<Node<State>> {
    /**
     * Make sure you set pathCost before adding to the queue
     */
    override fun compare(lhs: Node<State>, rhs: Node<State>): Int {
        //println("LHS distance: ${distance[lhs]}")
        //println("RHS distance: ${distance[rhs]}")
        val lhsManhattanDistance = manhattanDistance(lhs.value.blockPositions, lhs.value.storagePositions)
        val rhsManhattanDistance = manhattanDistance(rhs.value.blockPositions, rhs.value.storagePositions)
        val lhsTotal = distance[lhs] as Int + lhsManhattanDistance
        val rhsTotal = distance[rhs] as Int + rhsManhattanDistance

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
}
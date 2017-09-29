package edu.odu.cs480.adamhill

class ManhattanStateComparator: Comparator<Node<State>> {
    override fun compare(lhs: Node<State>, rhs: Node<State>): Int {
        val lhsManhattanDistance = manhattanDistance(lhs.value.blockPositions, lhs.value.storagePositions)
        val rhsManhattanDistance = manhattanDistance(rhs.value.blockPositions, rhs.value.storagePositions)

        if (lhsManhattanDistance < rhsManhattanDistance) return -1
        if (lhsManhattanDistance > rhsManhattanDistance) return 1

        return 0 // the distances are the same
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
package edu.odu.cs480.adamhill

import org.junit.Test
import org.junit.Assert.*

class GraphTest {
    @Test
    fun testManhattenDistance() {
        val blockPositions = hashSetOf(Point(0, 1))
        val storagePositions = hashSetOf(Point(0, 2))
        val robot = Robot(Point(0, 0))
        val state = State(robot, blockPositions, storagePositions)

        val graph = Graph(Node(state), Barriers(), Pair(10, 10))

        assertTrue(graph.manhattanDistance(blockPositions, storagePositions) == 1)
    }
}
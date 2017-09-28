package edu.odu.cs480.adamhill

import org.junit.Test
import org.junit.Assert.*

class StateTest {
    @Test
    fun testIsGoalState() {
        val blockPositions = hashSetOf(Point(0, 1))
        val storagePositions = hashSetOf(Point(0, 1))
        val robot = Robot(Point(0, 0))
        val state = State(robot, blockPositions, storagePositions)
        assertTrue(state.isGoalState())

        blockPositions.remove(Point(0, 1))
        blockPositions.add(Point(1, 5))
        assertFalse(state.isGoalState())
    }
}
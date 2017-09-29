package edu.odu.cs480.adamhill

import org.junit.Test
import org.junit.Assert.*

class PuzzleTest {
    @Test
    fun testSimple2() {
        val file = "data/simple2.txt"
        val puzzleFileReader = PuzzleFileReader(file)
        val puzzleInfo = puzzleFileReader.readFile()
        println(puzzleInfo.state.toString())

        val graph = Graph(Node(puzzleInfo.state), puzzleInfo.barriers, puzzleInfo.dimensions)
        val robot = graph.root.value.robot
        val newState = State(robot, graph.root.value.blockPositions, graph.root.value.storagePositions)
        assertTrue(graph.isLegalMove(Node(newState), puzzleInfo.barriers, Robot.Direction.EAST, Point(2, 1), puzzleInfo.dimensions))
        robot.moveEast()
        assertTrue(newState.robot.position == Point(2, 1))

        assertTrue(graph.isLegalMove(Node(newState), puzzleInfo.barriers, Robot.Direction.EAST, Point(3, 1), puzzleInfo.dimensions))
        robot.moveEast()
        assertTrue(newState.robot.position == Point(3, 1))

        assertTrue(graph.isLegalMove(Node(newState), puzzleInfo.barriers, Robot.Direction.EAST, Point(4, 1), puzzleInfo.dimensions))
        robot.moveEast()
        assertTrue(newState.robot.position == Point(4, 1))

        assertTrue(graph.isLegalMove(Node(newState), puzzleInfo.barriers, Robot.Direction.EAST, Point(5, 1), puzzleInfo.dimensions))
        robot.moveEast()
        assertTrue(newState.robot.position == Point(5, 1))

        assertTrue(graph.isLegalMove(Node(newState), puzzleInfo.barriers, Robot.Direction.SOUTH, Point(5, 2), puzzleInfo.dimensions))
        robot.moveSouth()
        assertTrue(newState.robot.position == Point(5, 2))
    }
}
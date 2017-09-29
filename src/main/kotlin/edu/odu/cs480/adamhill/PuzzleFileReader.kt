package edu.odu.cs480.adamhill

import java.io.File

/**
 * Reads the input puzzle map and generates the initial state
 * @return The initial state and a set of obstacle positions
 */
class PuzzleFileReader(private val file: String) {
    fun readFile(): PuzzleInfo {
        val inputStream = File(file).inputStream()
        val input = mutableListOf<String>() // collect all lines from the file to be processed later
        inputStream.bufferedReader().useLines { lines -> lines.forEach { input.add(it) } }
        input.forEach { println(it) }
        val rows = input.size
        val cols = input[0].length
        println("Rows: $rows, Cols: $cols")

        val state = State()
        val barriers = Barriers()

        // Find robot, boxes, storage, and obstacles
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                // Found the robot
                when {
                    // Found the robot
                    input[i][j] == 'R' -> {
                        println("Robot found at $j, $i")
                        state.robot.position = Point(j, i)
                    }

                    // Found a box
                    input[i][j] == 'B' -> {
                        println("Box found at $j, $i")
                        state.blockPositions.add(Point(j, i))
                    }

                    // Found a storage space
                    input[i][j] == 'S' -> {
                        println("Storage space found at $j, $i")
                        state.storagePositions.add(Point(j, i))
                    }

                    // Found an obstacle
                    input[i][j] == 'O' -> {
                        println("Found an obstacle at $j, $i")
                        barriers.obstaclePositions.add(Point(j, i))
                    }
                }
            }
        }
        println(state)
        return PuzzleInfo(state, barriers, Pair(rows, cols))
    }
}
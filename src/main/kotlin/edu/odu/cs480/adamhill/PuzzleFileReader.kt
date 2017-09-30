package edu.odu.cs480.adamhill

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Reads the input puzzle map and generates the initial state
 * @return The initial state and a set of obstacle positions
 */
class PuzzleFileReader(private val filename: String) {
    fun readFile(): PuzzleInfo {
        val input = mutableListOf<String>() // collect all lines from the filename to be processed later
        var reader: BufferedReader? = null

        try {
            val file = File(filename)
            reader = BufferedReader(FileReader(file))

            var line = reader.readLine()
            while (line != null) {
                input.add(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        input.forEach { println(it) }

        val state = State()
        val barriers = Barriers()
        val rows = input.size

        // Find robot, boxes, storage, and obstacles
        for (y in 0 until rows) {
            val cols = input[y].length
            for (x in 0 until cols) {
                when {
                    // Found the robot
                    input[y][x] == 'R' -> {
                        state.robot.position = Point(x, y)
                    }

                    // Found a box
                    input[y][x] == 'B' -> {
                        state.blockPositions.add(Point(x, y))
                    }

                    // Found a storage space
                    input[y][x] == 'S' -> {
                        state.storagePositions.add(Point(x, y))
                    }

                    // Found an obstacle
                    input[y][x] == 'O' -> {
                        barriers.obstaclePositions.add(Point(x, y))
                    }
                }
            }
        }
        val maxCols = findMaxCols(input)
        return PuzzleInfo(state, barriers, Pair(rows, maxCols))
    }

    private fun findMaxCols(input: MutableList<String>): Int {
        var max = 0
        input.forEach {
            if (it.length > max) max = it.length
        }
        return max
    }
}
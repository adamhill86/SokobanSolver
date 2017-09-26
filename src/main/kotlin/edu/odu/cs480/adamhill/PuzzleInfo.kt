package edu.odu.cs480.adamhill

/**
 * This class holds the relevant information about a puzzle map that has been read from a file
 */
data class PuzzleInfo(val state: State, val barriers: Barriers, val dimensions: Pair<Int, Int>)
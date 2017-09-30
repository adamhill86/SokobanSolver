@file:JvmName("Main")

package edu.odu.cs480.adamhill

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        val puzzles = mutableListOf<PuzzleInfo>()

        // read in each puzzle and store its info
        args.forEach {
            val puzzleFileReader = PuzzleFileReader(it)
            val puzzleInfo = puzzleFileReader.readFile()
            puzzles.add(puzzleInfo)
        }

        puzzles.forEach {
            val graph = Graph(Node(it.state), it.barriers, it.dimensions)
            println("Initial state: \n${graph.root}")
            val path = graph.breadthFirstSearch()
            displayPath(path, "Breadth-first search")
            println()

            val dfs = graph.depthFirstSearch()
            displayPath(dfs, "Iterative Deepening Depth-first search")
            println()

            val gbfs = graph.greedyBestFirst()
            displayPath(gbfs, "Greedy best-first search")
            println()

            val aStar = graph.aStarSearch()
            displayPath(aStar, "A*")
        }

    }
    /*
    val file = "data/test2.txt"
    val puzzleFileReader = PuzzleFileReader(file)
    val puzzleInfo = puzzleFileReader.readFile()

    val graph = Graph(Node(puzzleInfo.state), puzzleInfo.barriers, puzzleInfo.dimensions)
    println("Initial state: \n${graph.root}")
    val path = graph.breadthFirstSearch()
    displayPath(path, "Breadth-first search")

    val dfs = graph.depthFirstSearch()
    displayPath(dfs, "Depth-first search")

    val gbfs = graph.greedyBestFirst()
    displayPath(gbfs, "Greedy best-first search")
    val aStar = graph.aStarSearch()
    displayPath(aStar, "A*")*/
}

private fun displayPath(path: MutableList<Node<State>>?, solutionType: String) {
    if (path == null) println("No solution was found")
    else {
        println("\n$solutionType solution took ${path.size - 1} steps.")
        for (i in 1 until path.size) {
            print("[$i] ${path[i].value}")
            if (i != path.size - 1) println(",")
        }
    }
}
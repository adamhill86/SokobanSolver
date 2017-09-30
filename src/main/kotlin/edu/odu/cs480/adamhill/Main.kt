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
            println("Initial state: \n${it.state} \n")

            var startTime: Long = System.nanoTime()
            var aStar = aStar(it.state, it.barriers, it.dimensions)
            var endTime:Long = System.nanoTime()
            var duration = (endTime - startTime)/1000000 //divide by 1000000 to get milliseconds
            displayPath(aStar, "A*", duration)
            println("\n")
            aStar = null
            System.gc()

            startTime = System.nanoTime()
            var gbfs = greedyBest(it.state, it.barriers, it.dimensions)
            endTime = System.nanoTime()
            duration = (endTime - startTime)/1000000 //divide by 1000000 to get milliseconds
            displayPath(gbfs, "Greedy best-first search", duration)
            println("\n")
            gbfs = null
            System.gc()

            startTime = System.nanoTime()
            var path = bfs(it.state, it.barriers, it.dimensions)
            endTime = System.nanoTime()
            duration = (endTime - startTime)/1000000 //divide by 1000000 to get milliseconds
            displayPath(path, "Breadth-first search", duration)
            println("\n")
            path = null
            System.gc()

            startTime = System.nanoTime()
            var dfs = dfs(it.state, it.barriers, it.dimensions)
            endTime = System.nanoTime()
            duration = (endTime - startTime)/1000000 //divide by 1000000 to get milliseconds
            displayPath(dfs, "Depth-first search", duration)
            println("\n")
            dfs = null
            System.gc()
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

private fun displayPath(path: MutableList<Node<State>>?, solutionType: String, time: Long) {
    if (path == null) println("No solution was found")
    else {
        println("\n$solutionType solution took ${path.size - 1} steps and $time ms.")
        for (i in 1 until path.size) {
            print("[$i] ${path[i].value}")
            if (i != path.size - 1) println(",")
        }
    }
}

private fun aStar(state: State, barriers: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>>? {
    val graph = Graph(Node(state), barriers, dimensions)
    return graph.aStarSearch()
}

private fun greedyBest(state: State, barriers: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>>? {
    val graph = Graph(Node(state), barriers, dimensions)
    return graph.greedyBestFirst()
}

private fun bfs(state: State, barriers: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>>? {
    val graph = Graph(Node(state), barriers, dimensions)
    return graph.breadthFirstSearch()
}

private fun dfs(state: State, barriers: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>>? {
    val graph = Graph(Node(state), barriers, dimensions)
    return graph.depthFirstSearch()
}
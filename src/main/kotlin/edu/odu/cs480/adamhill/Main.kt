package edu.odu.cs480.adamhill

fun main(args: Array<String>) {
    val file = "data/simple2.txt"
    val puzzleFileReader = PuzzleFileReader(file)
    val puzzleInfo = puzzleFileReader.readFile()
    println(puzzleInfo.state.toString())

    val graph = Graph(Node(puzzleInfo.state), puzzleInfo.barriers, puzzleInfo.dimensions)
    println(graph.root)
    val path = graph.breadthFirstSearch()
    println(path)
    println("\n\n")

    val dfs = graph.depthFirstSearch()
    println(dfs)

    val gbfs = graph.greedyBestFirst()
    val astar = graph.aStarSearch()
    println(astar)
}
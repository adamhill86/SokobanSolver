package edu.odu.cs480.adamhill

fun main(args: Array<String>) {
    val a: Node<Char> = Node('A')
    val b: Node<Char> = Node('B')
    val c: Node<Char> = Node('C')
    val d: Node<Char> = Node('D')
    val e: Node<Char> = Node('E')
    val f: Node<Char> = Node('F')

    val file = "data/test.txt"
    val puzzleFileReader = PuzzleFileReader(file)
    val puzzleInfo = puzzleFileReader.readFile()
    println(puzzleInfo.state.toString())

    a.addChildren(b, c)
    b.addChildren(a, d, e)
    c.addChildren(a, f)
    d.addChild(b)
    e.addChildren(b, f)
    f.addChildren(c, e)
    val graph = Graph(a)
    graph.addNodes(b, c, d, e, f)

    val path = graph.bfs(e)
    path?.forEach { println(it.value) }

    val dfs = graph.dfs(d)
    dfs?.forEach { println(it.value) }

    val gameController = GameController(Node(puzzleInfo.state), puzzleInfo.barriers,
            puzzleInfo.dimensions.first, puzzleInfo.dimensions.second)
    println(gameController.graph.root.toString())
}
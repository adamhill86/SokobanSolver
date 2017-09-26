package edu.odu.cs480.adamhill

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Graph(val root: Node<*>) {
    val nodes: MutableList<Node<*>> = mutableListOf()

    fun addNode(node: Node<*>) {
        nodes.add(node)
    }

    fun addNodes(vararg nodes: Node<*>) {
        nodes.forEach { addNode(it) }
    }

    /**
     * Breadth-first search with shortest path algorithm
     * @param goal The node we're looking for
     * @return A list of nodes representing the shortest path to the goal or null if none is found
     */
    fun bfs(goal: Node<*>): MutableList<Node<*>>? {
        val visited: HashSet<Node<*>> = HashSet()
        val queue: Queue<Node<*>> = LinkedList<Node<*>>()
        val distance = HashMap<Node<*>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<*>, Node<*>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        queue.add(root)

        while (!queue.isEmpty() && distance[goal] == Int.MAX_VALUE) {
            val current = queue.poll()
            val dist = distance[current]

            for (node in current.childNodes) {
                // only add new nodes that we haven't visited before
                if (!visited.contains(node)) {
                    // casts here are required for null safety
                    if (distance[node] as Int > (dist as Int + 1)) {
                        distance[node] = dist + 1
                        predecessor[node] = current
                        visited.add(node)
                        queue.add(node)
                    }
                }
            }
        }

        if (distance[goal] != Int.MAX_VALUE) {
            return extractPath(goal, predecessor)
        }
        return null // return null if no path to the goal was not found
    }

    fun dfs(goal: Node<*>): MutableList<Node<*>>? {
        val visited: HashSet<Node<*>> = HashSet()
        val stack: Stack<Node<*>> = Stack()
        val distance = HashMap<Node<*>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<*>, Node<*>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        stack.push(root)

        while (!stack.isEmpty() && distance[goal] == Int.MAX_VALUE) {
            val current = stack.pop()

            if (!visited.contains(current)) {
                visited.add(current)
                current.childNodes.forEach {
                    if (distance[it] as Int > (distance[current] as Int + 1)) {
                        distance[it] = distance[current] as Int + 1
                        predecessor[it] = current
                        stack.push(it)
                    }
                }
            }
        }

        if (distance[goal] != Int.MAX_VALUE) {
            return extractPath(goal, predecessor)
        }
        return null // return null if no path to the goal was not found
    }

    /**
     * Returns a list of Nodes representing the shortest path from the root to the goal
     * @param goal The Node we're trying to get to
     * @param predecessor Maps each Node to its previous Node
     * @return A list of Nodes representing the shortest path from the root to the goal
     */
    private fun extractPath(goal: Node<*>, predecessor: HashMap<Node<*>, Node<*>>)
            : MutableList<Node<*>> {
        var node = goal
        val path = mutableListOf<Node<*>>()

        while (node != root) {
            path.add(0, node)
            node = predecessor[node] as Node<*> // cast is required due to null safety
        }

        path.add(0, root)
        return path
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Graph

        if (root != other.root) return false
        if (nodes != other.nodes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = root?.hashCode() ?: 0
        result = 31 * result + nodes.hashCode()
        return result
    }

    override fun toString(): String {
        return "Graph(root=$root, nodes=$nodes)"
    }
}
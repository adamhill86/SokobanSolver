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

        if (distance[goal] != Int.MAX_VALUE) return extractPath(goal, predecessor)
        return null // no path to the goal was found
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

    /**
     * Calculates all robot moves from the current state
     * @param state The current state of the map
     * @param obstacles The set of obstacle positions
     * @return The list of all possible moves (or null if none exist)
     */
    fun calculateMoves(state: Node<State>, obstacles: Barriers): MutableList<Node<State>>? {
        val possibleMoves = mutableListOf<Node<State>>()
        val robot = state.value.robot
        val northPoint = Point(robot.position.x, robot.position.y - 1)
        val southPoint = Point(robot.position.x, robot.position.y + 1)
        val eastPoint = Point(robot.position.x + 1, robot.position.y)
        val westPoint = Point(robot.position.x - 1, robot.position.y)

        // TODO: Refactor State creation code. Move to function
        // check moving north
        if (isLegalMove(state, obstacles, northPoint)) {
            val robotCopy = Robot(northPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()
            val northState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(northState)
        }

        // check moving south
        if (isLegalMove(state, obstacles, southPoint)) {
            val robotCopy = Robot(southPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()
            val southState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(southState)
        }

        // check moving east
        if (isLegalMove(state, obstacles, eastPoint)) {
            val robotCopy = Robot(eastPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()
            val eastState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(eastState)
        }

        // check moving west
        if (isLegalMove(state, obstacles, westPoint)) {
            val robotCopy = Robot(westPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()
            val westState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(westState)
        }
        return possibleMoves
    }

    /**
     * Calculates whether a robot move is legal
     * @param state The current state of the map
     * @param obstacles The set of obstacle positions
     * @param nextPosition The position of the potential move
     * @return True if the move is legal, false otherwise
     */
    fun isLegalMove(state: Node<State>, obstacles: Barriers, nextPosition: Point): Boolean {
        // for now, only check to see if the position matches an obstacle's position
        if (obstacles.contains(nextPosition)) return false
        // TODO: also make sure the point isn't out of bounds
        if (nextPosition.x < 0 || nextPosition.y < 0) return false
        return true
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
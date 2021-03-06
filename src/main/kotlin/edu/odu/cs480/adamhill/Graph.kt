package edu.odu.cs480.adamhill

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Graph(val root: Node<State>, private val barriers: Barriers, private val dimensions: Pair<Int, Int>) {
    private val nodes: MutableList<Node<State>> = mutableListOf()

    fun addNode(node: Node<State>) {
        nodes.add(node)
    }

    fun addNodes(vararg nodes: Node<State>) {
        nodes.forEach { addNode(it) }
    }

    /**
     * This version does not take in a goal state and instead tries to determine it during the while loop
     */
    fun breadthFirstSearch(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val queue: Queue<Node<State>> = LinkedList<Node<State>>()
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        queue.add(root)

        val startTime: Long = System.nanoTime() // keep track of when we started

        while (!queue.isEmpty()) {
            val currentTime: Long = System.nanoTime()
            if (((currentTime - startTime) / 1000000) >= 600000) { // we haven't found a solution in 10 minutes
                println("No solution found after 10 minutes.")
                return null
            }

            val current = queue.poll()

            if (current.value.isGoalState()) {
                visited.clear()
                queue.clear()
                return extractPath(current, predecessor)
            }

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            //moves.forEach { current.addChild(it) }

            for (node in moves) {
                // only add new nodes that we haven't visited before
                if (!visited.contains(node)) {
                    predecessor[node] = current
                    visited.add(node)
                    queue.add(node)
                }
            }
        }

        // if we made it out here, the goal wasn't found
        return null
    }

    fun depthFirstSearch(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val stack: Stack<Node<State>> = Stack()
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        stack.push(root)

        val startTime: Long = System.nanoTime() // keep track of when we started

        while (!stack.isEmpty()) {
            val currentTime: Long = System.nanoTime()
            if (((currentTime - startTime) / 1000000) >= 600000) { // we haven't found a solution in 10 minutes
                println("No solution found after 10 minutes.")
                return null
            }

            val current = stack.pop()

            if (current.value.isGoalState()) {
                visited.clear()
                stack.clear()
                return extractPath(current, predecessor)
            }

            if (!visited.contains(current)) {
                visited.add(current)

                val moves = calculateMoves(current, barriers, dimensions)
                //moves.forEach { current.addChild(it) }

                moves.forEach {
                    if (!visited.contains(it)) {
                        predecessor[it] = current
                        stack.push(it)
                    }
                }
            }
        }

        // if we make it here, there was no path to the goal
        return null
    }

    /*
    fun iterativeDeepeningDFS(): MutableList<Node<State>>? {
        val visited = hashSetOf<Node<State>>()
        val stack = Stack<Node<State>>()
        val predecessor = hashMapOf<Node<State>, Node<State>>()
        var maxDepth = 1 // initial depth
        val goalFound = false

        stack.push(root)

        while (!goalFound) {
            var depth = 0
            while (!stack.isEmpty()) {
                val current = stack.pop()

                if (current.value.isGoalState()) {
                    return extractPath(current, predecessor)
                }

                if (!visited.contains(current)) {
                    visited.add(current)

                    if (depth < maxDepth) {
                        val moves = calculateMoves(current, barriers, dimensions)
                        moves.forEach { current.addChild(it) }

                        current.childNodes.forEach {
                            if (!visited.contains(it)) {
                                predecessor[it] = current
                                stack.push(it)
                            }
                        }
                        depth++
                    }
                }
            }
            maxDepth++
            visited.clear()
        }
        return null
    }*/

    /**
     * Returns a list of Nodes representing the shortest path from the root to the goal
     * @param goal The Node we're trying to get to
     * @param predecessor Maps each Node to its previous Node
     * @return A list of Nodes representing the shortest path from the root to the goal
     */
    private fun extractPath(goal: Node<State>, predecessor: HashMap<Node<State>, Node<State>>)
            : MutableList<Node<State>> {
        var node = goal
        val path = mutableListOf<Node<State>>()

        while (node != root) {
            path.add(0, node)
            node = predecessor[node] as Node<State> // cast is required due to null safety
        }

        path.add(0, root)
        return path
    }

    fun greedyBestFirst(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val comparator = GreedyComparator(barriers)
        val priorityQueue = PriorityQueue<Node<State>>(2000, comparator)
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        priorityQueue.add(root)
        visited.add(root)

        while (!priorityQueue.isEmpty()) {
            val current = priorityQueue.poll()

            if (current.value.isGoalState()) {
                // try to trigger gc
                visited.clear()
                priorityQueue.clear()
                System.gc()
                return extractPath(current, predecessor)
            }

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            //moves.forEach { current.addChild(it) }

            for (node in moves) {
                // only add new nodes that we haven't visited before
                if (!visited.contains(node)) {
                    predecessor[node] = current
                    visited.add(node)
                    priorityQueue.add(node)
                }
            }
        }

        return null
    }

    fun aStarSearch(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val distance = HashMap<Node<State>, Int>() // the distance between the root and each node
        val comparator = AStarStateComparator(distance, barriers)
        val priorityQueue = PriorityQueue<Node<State>>(2000, comparator)
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        priorityQueue.add(root)

        while (!priorityQueue.isEmpty()) {
            val current = priorityQueue.poll()
            if (current.value.isGoalState()) {
                // try to trigger garbage collection
                visited.clear()
                distance.clear()
                priorityQueue.clear()
                System.gc()
                return extractPath(current, predecessor)
            }

            val dist = distance[current]

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            /*
            moves.forEach {
                current.addChild(it)
            }*/

            for (node in moves) {
                if (!visited.contains(node)) {
                    visited.add(node)
                    val pathCost = dist as Int + 1
                    if (!distance.contains(node) || pathCost < distance[node] as Int) {
                        distance[node] = pathCost
                        priorityQueue.add(node)
                        predecessor[node] = current
                    }
                }
            }
        }
        return null
    }

    /**
     * Calculates all robot moves from the current state
     * @param state The current state of the map
     * @param obstacles The set of obstacle positions
     * @param dimensions The number of rows and columns in the map
     * @return The list of all possible moves (or an empty list if none exist)
     */
    private fun calculateMoves(state: Node<State>, obstacles: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>> {
        val possibleMoves = mutableListOf<Node<State>>()
        val robot = state.value.robot
        val northPoint = Point(robot.position.x, robot.position.y - 1)
        val southPoint = Point(robot.position.x, robot.position.y + 1)
        val eastPoint = Point(robot.position.x + 1, robot.position.y)
        val westPoint = Point(robot.position.x - 1, robot.position.y)

        // TODO: Refactor State creation code. Move to function
        // check moving north
        if (isLegalMove(state, obstacles, Robot.Direction.NORTH, northPoint, dimensions)) {
            val northState = Node(createNewState(state, Robot.Direction.NORTH, northPoint))
            possibleMoves.add(northState)
        }

        // check moving south
        if (isLegalMove(state, obstacles, Robot.Direction.SOUTH, southPoint, dimensions)) {
            val southState = Node(createNewState(state, Robot.Direction.SOUTH, southPoint))
            possibleMoves.add(southState)
        }

        // check moving east
        if (isLegalMove(state, obstacles, Robot.Direction.EAST, eastPoint, dimensions)) {
            val eastState = Node(createNewState(state, Robot.Direction.EAST, eastPoint))
            possibleMoves.add(eastState)
        }

        // check moving west
        if (isLegalMove(state, obstacles, Robot.Direction.WEST, westPoint, dimensions)) {
            val westState = Node(createNewState(state, Robot.Direction.WEST, westPoint))
            possibleMoves.add(westState)
        }
        return possibleMoves
    }

    private fun createNewState(state: Node<State>, direction: Robot.Direction, point: Point): State {
        val robotCopy = Robot(point)
        val blockPositions = state.value.copyBlockPositions()
        val storagePositions = state.value.copyStoragePositions()

        if (isBlockMove(state, point)) {
            moveBlock(blockPositions, direction, point)
        }

        return State(robotCopy, blockPositions, storagePositions)
    }

    /**
     * Calculates whether a robot move is legal
     * @param state The current state of the map
     * @param obstacles The set of obstacle positions
     * @param nextPosition The position of the potential move
     * @param dimensions The number of rows and columns in the map. first is rows, second is cols
     * @return True if the move is legal, false otherwise
     */
    fun isLegalMove(state: Node<State>, obstacles: Barriers, direction: Robot.Direction, nextPosition: Point,
                    dimensions: Pair<Int, Int>): Boolean {
        // for now, only check to see if the position matches an obstacle's position
        if (obstacles.contains(nextPosition)) return false

        // check if a block is nearby
        if (state.value.blockPositions.contains(nextPosition)) {
            // check direction to see if there's another block next to the block
            // if there are two blocks in a row, the move is illegal
            if (direction == Robot.Direction.NORTH
                    && state.value.blockPositions.contains(Point(nextPosition.x, nextPosition.y-1))) return false
            if (direction == Robot.Direction.SOUTH
                    && state.value.blockPositions.contains(Point(nextPosition.x, nextPosition.y+1))) return false
            if (direction == Robot.Direction.EAST
                    && state.value.blockPositions.contains(Point(nextPosition.x+1, nextPosition.y))) return false
            if (direction == Robot.Direction.WEST
                     && state.value.blockPositions.contains(Point(nextPosition.x-1, nextPosition.y))) return false

            // if the block will go into a barrier, the move is illegal
            if (direction == Robot.Direction.NORTH
                    && obstacles.contains(Point(nextPosition.x, nextPosition.y-1))) return false
            if (direction == Robot.Direction.SOUTH
                    && obstacles.contains(Point(nextPosition.x, nextPosition.y+1))) return false
            if (direction == Robot.Direction.EAST
                    && obstacles.contains(Point(nextPosition.x+1, nextPosition.y))) return false
            if (direction == Robot.Direction.WEST
                    && obstacles.contains(Point(nextPosition.x-1, nextPosition.y))) return false
        }

        // make sure we're staying in bounds
        if (nextPosition.x < 0 || nextPosition.y < 0) return false
        if (nextPosition.x >= dimensions.second || nextPosition.y >= dimensions.first) return false

        // return true if none of the above conditions trigger a return
        return true
    }

    /**
     * Returns true if a block should be moved. False otherwise
     */
    private fun isBlockMove(state: Node<State>, nextPosition: Point): Boolean {
        if (state.value.blockPositions.contains(nextPosition)) {
            return true
        }
        return false
    }

    /**
     * Moves a block one unit in a given direction
     */
    private fun moveBlock(blockPositions: HashSet<Point>, direction: Robot.Direction, nextPosition: Point) {
        blockPositions.remove(nextPosition)

        when (direction) {
            Robot.Direction.NORTH -> blockPositions.add(Point(nextPosition.x, nextPosition.y-1))
            Robot.Direction.SOUTH -> blockPositions.add(Point(nextPosition.x, nextPosition.y+1))
            Robot.Direction.EAST -> blockPositions.add(Point(nextPosition.x+1, nextPosition.y))
            Robot.Direction.WEST -> blockPositions.add(Point(nextPosition.x-1, nextPosition.y))
        }
    }

    /**
     * Calculates the Manhattan distance between all blocks and storage positions.
     */
    fun manhattanDistance(blockPositions: HashSet<Point>, storagePositions: HashSet<Point>): Int {
        var sumDistance = 0

        for (blockPosition in blockPositions) {
            for (storagePosition in storagePositions) {
                sumDistance += Math.abs(blockPosition.x - storagePosition.x)
                sumDistance += Math.abs(blockPosition.y - storagePosition.y)
            }
        }

        return sumDistance
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
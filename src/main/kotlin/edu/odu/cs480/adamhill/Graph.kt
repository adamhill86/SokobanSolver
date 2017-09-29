package edu.odu.cs480.adamhill

import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Graph(val root: Node<State>, private val barriers: Barriers, private val dimensions: Pair<Int, Int>) {
    val nodes: MutableList<Node<State>> = mutableListOf()

    fun addNode(node: Node<State>) {
        nodes.add(node)
    }

    fun addNodes(vararg nodes: Node<State>) {
        nodes.forEach { addNode(it) }
    }

    /**
     * Breadth-first search with shortest path algorithm
     * @param goal The node we're looking for
     * @return A list of nodes representing the shortest path to the goal or null if none is found
     */
    fun bfs(goal: Node<State>): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val queue: Queue<Node<State>> = LinkedList<Node<State>>()
        val distance = HashMap<Node<State>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        queue.add(root)

        while (!queue.isEmpty() && distance[goal] == Int.MAX_VALUE) {
            val current = queue.poll()
            val dist = distance[current]
            
            // determine all possible moves from this state
            val moves = calculateMoves(current, barriers, dimensions)
            moves.forEach {
                current.addChild(it)
                distance[it] = Int.MAX_VALUE
            }

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


    /**
     * This version does not take in a goal state and instead tries to determine it during the while loop
     */
    fun breadthFirstSearch(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val queue: Queue<Node<State>> = LinkedList<Node<State>>()
        val distance = HashMap<Node<State>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        queue.add(root)

        while (!queue.isEmpty()) {
            val current = queue.poll()
            //println(current)
            val dist = distance[current]

            if (current.value.isGoalState()) {
                return extractPath(current, predecessor)
            }

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            moves.forEach {
                current.addChild(it)
                distance[it] = Int.MAX_VALUE
            }

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

        // if we made it out here, the goal wasn't found
        return null
    }

    fun depthFirstSearch(): Stack<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val stack: Stack<Node<State>> = Stack()
        val distance = HashMap<Node<State>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
        }

        distance[root] = 0 // distance from the root to itself
        stack.push(root)

        while (!stack.isEmpty()) {
            val current = stack.pop()

            if (current.value.isGoalState()) {
                return dfsPath(current, predecessor, distance)
            }

            if (!visited.contains(current)) {
                visited.add(current)

                val moves = calculateMoves(current, barriers, dimensions)
                moves.forEach {
                    current.addChild(it)
                    distance[it] = Int.MAX_VALUE
                }

                current.childNodes.forEach {
                    if (distance[it] as Int > (distance[current] as Int + 1) && (!visited.contains(it))) {
                        distance[it] = distance[current] as Int + 1
                        predecessor[it] = current
                        stack.push(it)
                    }
                }
            }
        }

        // if we make it here, there was no path to the goal
        return null
    }

    fun dfs(goal: Node<State>): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val stack: Stack<Node<State>> = Stack()
        val distance = HashMap<Node<State>, Int>() // the distance between the root and each node
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

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
    private fun extractPath(goal: Node<State>, predecessor: HashMap<Node<State>, Node<State>>)
            : MutableList<Node<State>> {
        var node = goal
        val path = mutableListOf<Node<State>>()
        var numNodes = 0

        while (node != root) {
            numNodes++
            println(numNodes)
            path.add(0, node)
            node = predecessor[node] as Node<State> // cast is required due to null safety
        }

        path.add(0, root)
        return path
    }

    // TODO: Remove this
    private fun dfsPath(goal: Node<State>, predecessor: HashMap<Node<State>, Node<State>>,
                        distance: HashMap<Node<State>, Int>): Stack<Node<State>> {
        var node = goal
        val stack = Stack<Node<State>>()
        var numNodes = 0

        while (node != root) {
            numNodes++
            println(numNodes)
            stack.push(node)
            node = predecessor[node] as Node<State>
        }
        stack.push(root)
        return stack
    }

    fun greedyBestFirst(): MutableList<Node<State>>? {
        val visited: HashSet<Node<State>> = HashSet()
        val comparator = ManhattanStateComparator()
        val priorityQueue = PriorityQueue<Node<State>>(10, comparator)
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        priorityQueue.add(root)
        visited.add(root)

        while (!priorityQueue.isEmpty()) {
            val current = priorityQueue.poll()

            if (current.value.isGoalState()) return extractPath(current, predecessor)

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            moves.forEach { current.addChild(it) }

            for (node in current.childNodes) {
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
        val fScore = HashMap<Node<State>, Int>() // the total cost of getting from the root to the goal by passing that node. h(n) + distance
        val comparator = AStarStateComparator(distance)
        val priorityQueue = PriorityQueue<Node<State>>(10, comparator)
        val predecessor = HashMap<Node<State>, Node<State>>() // Previous node in optimal path from root

        nodes.forEach {
            distance[it] = Int.MAX_VALUE // set the initial distance to a number larger than any legitimate path could be
            fScore[it] = Int.MAX_VALUE
        }

        distance[root] = 0 // distance from the root to itself
        fScore[root] = manhattanDistance(root.value.blockPositions, root.value.storagePositions)
        visited.add(root) // use this to make sure we don't get stuck in an infinite cycle
        priorityQueue.add(root)

        while (!priorityQueue.isEmpty()) {
            val current = priorityQueue.poll()
            if (current.value.isGoalState()) return extractPath(current, predecessor)

            val dist = distance[current]

            // determine all possible moves from this state if it isn't the goal
            val moves = calculateMoves(current, barriers, dimensions)
            moves.forEach {
                current.addChild(it)
                //distance[it] = Int.MAX_VALUE
                //fScore[it] = Int.MAX_VALUE
            }

            for (node in current.childNodes) {
                if (!visited.contains(node)) {
                    visited.add(node)
                    val pathCost = dist as Int + 1
                    if (!distance.contains(node) || pathCost < distance[node] as Int) {
                        distance[node] = pathCost
                        fScore[node] = pathCost + manhattanDistance(node.value.blockPositions, node.value.storagePositions)
                        println("${node.value} score: ${fScore[node]}")
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
    fun calculateMoves(state: Node<State>, obstacles: Barriers, dimensions: Pair<Int, Int>): MutableList<Node<State>> {
        val possibleMoves = mutableListOf<Node<State>>()
        val robot = state.value.robot
        val northPoint = Point(robot.position.x, robot.position.y - 1)
        val southPoint = Point(robot.position.x, robot.position.y + 1)
        val eastPoint = Point(robot.position.x + 1, robot.position.y)
        val westPoint = Point(robot.position.x - 1, robot.position.y)

        // TODO: Refactor State creation code. Move to function
        // check moving north
        if (isLegalMove(state, obstacles, Robot.Direction.NORTH, northPoint, dimensions)) {
            val robotCopy = Robot(northPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()

            if (isBlockMove(state, northPoint)) {
                moveBlock(blockPositions, Robot.Direction.NORTH, northPoint)
            }

            val northState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(northState)
        }

        // check moving south
        if (isLegalMove(state, obstacles, Robot.Direction.SOUTH, southPoint, dimensions)) {
            val robotCopy = Robot(southPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()

            if (isBlockMove(state, southPoint)) {
                moveBlock(blockPositions, Robot.Direction.SOUTH, southPoint)
            }

            val southState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(southState)
        }

        // check moving east
        if (isLegalMove(state, obstacles, Robot.Direction.EAST, eastPoint, dimensions)) {
            val robotCopy = Robot(eastPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()

            if (isBlockMove(state, eastPoint)) {
                moveBlock(blockPositions, Robot.Direction.EAST, eastPoint)
            }

            val eastState = Node(State(robotCopy, blockPositions, storagePositions))
            possibleMoves.add(eastState)
        }

        // check moving west
        if (isLegalMove(state, obstacles, Robot.Direction.WEST, westPoint, dimensions)) {
            val robotCopy = Robot(westPoint)
            val blockPositions = state.value.copyBlockPositions()
            val storagePositions = state.value.copyStoragePositions()

            if (isBlockMove(state, westPoint)) {
                moveBlock(blockPositions, Robot.Direction.WEST, westPoint)
            }

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
    fun isBlockMove(state: Node<State>, nextPosition: Point): Boolean {
        if (state.value.blockPositions.contains(nextPosition)) {
            return true
        }
        return false
    }

    /**
     * Moves a block one unit in a given direction
     */
    fun moveBlock(blockPositions: HashSet<Point>, direction: Robot.Direction, nextPosition: Point) {
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
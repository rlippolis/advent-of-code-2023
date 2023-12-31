package com.riccardo.adventofcode

import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max

object Utils {
    fun readFile(day: Int, name: String, trimStart: Boolean = true, trimEnd: Boolean = true): String =
        Utils::class.java.getResourceAsStream("edition2023/day$day/$name.txt")!!
            .bufferedReader()
            .readText()
            .let { if (trimStart) it.trimStart() else it }
            .let { if (trimEnd) it.trimEnd() else it }

    fun IntRange.overlapsWith(other: IntRange) = (last >= other.first) && (other.last >= first)
    fun LongRange.overlapsWith(other: LongRange) = (last >= other.first) && (other.last >= first)
    fun IntRange.contains(other: IntRange) = other.first in this && other.last in this

    fun <T> Sequence<T>.repeat() = generateSequence(this) { it }.flatten()

    fun lcm(a: Long, b: Long): Long = max(a, b).let { minLcm ->
        (minLcm..(a * b) step minLcm).first { lcm -> lcm % a == 0L && lcm % b == 0L }
    }

    fun Sequence<Long>.lcm() = reduce { a, b -> lcm(a, b) }
    fun List<Long>.lcm() = asSequence().lcm()
}

fun <T> String.toTypedList(separator: String = " ", converter: (String) -> T) = split(separator).map(converter)
fun String.toIntList(separator: String = " ") = toTypedList(separator, String::toInt)
fun String.toLongList(separator: String = " ") = toTypedList(separator, String::toLong)

fun <T> List<List<T>>.transpose(): List<List<T>> = List(first().size) { colIdx ->
    List(size) { rowIdx ->
        this[rowIdx][colIdx]
    }
}

fun <T> List<List<T>>.mirror(): List<List<T>> = map { it.reversed() }

data class Point2D(val x: Int, val y: Int) {

    fun up(n: Int = 1) = copy(y = y - n)
    fun down(n: Int = 1) = copy(y = y + n)
    fun left(n: Int = 1) = copy(x = x - n)
    fun right(n: Int = 1) = copy(x = x + n)
    fun leftUp(n: Int = 1) = Point2D(x = x - n, y = y - n)
    fun rightUp(n: Int = 1) = Point2D(x = x + n, y = y - n)
    fun leftDown(n: Int = 1) = Point2D(x = x - n, y = y + n)
    fun rightDown(n: Int = 1) = Point2D(x = x + n, y = y + n)

    fun move(direction: Direction, n: Int = 1): Point2D = when (direction) {
        Direction.Up    -> up(n)
        Direction.Down  -> down(n)
        Direction.Left  -> left(n)
        Direction.Right -> right(n)
    }

    fun getNeighbours(includingDiagonal: Boolean = false) = listOfNotNull(
        left(),
        right(),
        up(),
        down(),
        if (includingDiagonal) leftUp() else null,
        if (includingDiagonal) rightUp() else null,
        if (includingDiagonal) leftDown() else null,
        if (includingDiagonal) rightDown() else null,
    )

    fun manhattanDistance(other: Point2D): Int = (x - other.x).absoluteValue + (y - other.y).absoluteValue

    override fun toString(): String = "[x=$x, y=$y]"

    companion object {
        val origin = Point2D(x = 0, y = 0)
    }
}

enum class Direction {
    Up, Down, Left, Right;

    fun opposite(): Direction = when (this) {
        Up -> Down
        Down -> Up
        Left -> Right
        Right -> Left
    }
}

object Dijkstra {
    fun <T> findShortestRoute(nodes: Collection<T>, start: T, end: T, neighbourGetter: (T) -> Collection<Pair<T, Int>>, initialWeight: (T) -> Int = { 0 }): List<T>? {
        val nodeMapping = mutableMapOf<T, ShortestPathNode<T>>()
        nodes.forEach { node ->
            val pathNode = nodeMapping.getOrCreate(node, initialWeight(node))
            neighbourGetter(node).forEach { (neighbour, weight) ->
                val neighbourNode = nodeMapping.getOrCreate(neighbour, weight)
                pathNode.addDestination(neighbourNode, neighbourNode.value)
            }
        }

        val startNode = nodeMapping.getValue(start)
        calculateShortestDistanceFromSource(startNode)

        val shortestPath = LinkedList<ShortestPathNode<T>>()

        var node: ShortestPathNode<T>? = nodeMapping.getValue(end)
        do {
            shortestPath.push(node!!)
            node = node.previousNode
        } while (node != null && node.distance != Int.MAX_VALUE)

        return shortestPath.map { it.graphNode }.takeIf { start in it }
    }

    private fun <T> MutableMap<T, ShortestPathNode<T>>.getOrCreate(point: T, weight: Int) =
        computeIfAbsent(point) { ShortestPathNode(graphNode = point, value = weight) }

    private fun <T> calculateShortestDistanceFromSource(source: ShortestPathNode<T>) {
        source.distance = 0

        val settledNodes = hashSetOf<ShortestPathNode<T>>()
        val unsettledNodes = hashSetOf<ShortestPathNode<T>>()

        unsettledNodes.add(source)

        while (unsettledNodes.isNotEmpty()) {
            val currentNode = unsettledNodes.minByOrNull { it.distance }!!
            unsettledNodes.remove(currentNode)
            currentNode.adjacentNodes.forEach { (adjacentNode, edgeWeight) ->
                if (adjacentNode !in settledNodes) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
    }

    private fun <T> calculateMinimumDistance(evaluationNode: ShortestPathNode<T>, edgeWeight: Int, sourceNode: ShortestPathNode<T>) {
        val sourceDistance = sourceNode.distance
        if (sourceDistance + edgeWeight < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeight
            evaluationNode.previousNode = sourceNode
        }
    }

    data class ShortestPathNode<T>(
        val graphNode: T,
        val value: Int,
    ) {
        var previousNode: ShortestPathNode<T>? = null
        var distance = Int.MAX_VALUE
        val adjacentNodes = hashMapOf<ShortestPathNode<T>, Int>()

        fun addDestination(destination: ShortestPathNode<T>, distance: Int) {
            adjacentNodes[destination] = distance
        }
    }
}

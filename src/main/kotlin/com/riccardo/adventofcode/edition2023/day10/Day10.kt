package com.riccardo.adventofcode.edition2023.day10

import com.riccardo.adventofcode.Point2D
import java.awt.Polygon

fun solvePart1(input: String): Int = input.lines().getClosedLoop().size / 2

fun solvePart2(input: String): Int = input.lines().let { lines ->
    val closedLoop = lines.getClosedLoop()
    val polygon = Polygon().apply { closedLoop.forEach { addPoint(it.x, it.y) } }
    return lines.withIndex().sumOf { (y, line) ->
        line.indices.count { x -> Point2D(x = x, y = y) !in closedLoop && polygon.contains(x, y) }
    }
}

fun List<String>.getClosedLoop(): Set<Point2D> {
    val start = findStart()

    val visited = mutableSetOf(start)
    var next = findNext(start).firstOrNull()
    while (next != null) {
        visited.add(next)
        next = findNext(next).firstOrNull { it !in visited }
    }

    return visited
}

fun List<String>.findStart(): Point2D = withIndex()
    .first { (_, line) -> "S" in line }
    .let { (y, line) -> Point2D(x = line.indexOf("S"), y = y) }

fun List<String>.findNext(current: Point2D): List<Point2D> {
    val currVal = get(current)!!

    return listOfNotNull(
        current.up().takeIf { up -> get(up)?.let { it in "|7F" && currVal in "|LJS" } ?: false },
        current.down().takeIf { down -> get(down)?.let { it in "|LJ" && currVal in "|7FS" } ?: false },
        current.left().takeIf { left -> get(left)?.let { it in "-LF" && currVal in "-J7S" } ?: false },
        current.right().takeIf { right -> get(right)?.let { it in "-J7" && currVal in "-LFS" } ?: false },
    )
}

operator fun List<String>.get(point: Point2D): Char? =
    point.y.takeIf { it in indices }
        ?.let { get(it) }
        ?.let { line -> point.x.takeIf { it in line.indices }?.let { line[it] } }

package com.riccardo.adventofcode.edition2023.day17

import com.riccardo.adventofcode.Direction
import com.riccardo.adventofcode.Point2D
import java.util.*

fun solvePart1(input: String): Int = input.lines().map { it.toList().map(Char::digitToInt) }.let { grid ->
    grid.minimalHeatLoss(
        start = Point2D(x = 0, y = 0),
        end = grid.endPoint(),
        maxStraights = 3,
    )
}

fun solvePart2(input: String): Int = input.lines().map { it.toList().map(Char::digitToInt) }.let { grid ->
    grid.minimalHeatLoss(
        start = Point2D(x = 0, y = 0),
        end = grid.endPoint(),
        minStraights = 4,
        maxStraights = 10,
    )
}

fun List<List<Int>>.endPoint() = Point2D(x = this[this.lastIndex].lastIndex, y = this.lastIndex)

fun List<List<Int>>.minimalHeatLoss(start: Point2D, end: Point2D, minStraights: Int = 1, maxStraights: Int): Int {
    val startingPoint = State(start)

    val visited = mutableSetOf<State>()
    val states = PriorityQueue<Pair<State, Int>> { (s1, h1), (s2, h2) ->
        when {
            h1 == h2 -> when {
                s1.distance < s2.distance -> -1
                s1.distance > s2.distance -> 1
                else -> 0
            }
            h1 < h2 -> -1
            else -> 1
        }
    }

    states.add(startingPoint to 0)

    while (states.isNotEmpty()) {
        val (state, heat) = states.poll()
        if (state.pos == end) return heat
        if (!visited.add(state)) continue

        Direction.entries.forEach { dir ->
            if (dir == state.dir || dir == state.dir?.opposite()) return@forEach

            var h = heat
            var p = state.pos
            (1..maxStraights).forEach inner@ { straights ->
                p = p.move(dir).takeIf(::isValid) ?: return@inner
                h += this[p.y][p.x]
                if (straights >= minStraights) {
                    states.add((State(
                        pos = p,
                        dir = dir,
                    ) to h))
                }
            }
        }
    }
    error("No result found")
}

fun List<List<Int>>.isValid(point: Point2D) = point.y in indices && point.x in this[point.y].indices

data class State(val pos: Point2D, val dir: Direction? = null) {
    val distance = pos.manhattanDistance(Point2D.origin)
}

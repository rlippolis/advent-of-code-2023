package com.riccardo.adventofcode.edition2023.day11

import com.riccardo.adventofcode.Point2D
import kotlin.math.max
import kotlin.math.min

fun solvePart1(input: String): Long = sumDistances(input = input, emptySpaceSize = 2)
fun solvePart2(input: String, emptySpaceSize: Long): Long = sumDistances(input = input, emptySpaceSize = emptySpaceSize)

fun sumDistances(input: String, emptySpaceSize: Long): Long = input.lines().let { lines ->
    val rowsWithoutGalaxies = lines.indices.toMutableSet()
    val columnsWithoutGalaxies = lines.first().indices.toMutableSet()
    val galaxies = mutableListOf<Point2D>()

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == '#') {
                columnsWithoutGalaxies.remove(x)
                rowsWithoutGalaxies.remove(y)
                galaxies.add(Point2D(x = x, y = y))
            }
        }
    }

    galaxies
        .flatMapIndexed { idx, g1 -> galaxies.subList(idx + 1, galaxies.size).map { g2 -> g1 to g2 } }
        .sumOf { (from, to) ->
            val x1 = min(from.x, to.x) + 1
            val x2 = max(from.x, to.x)
            val y1 = min(from.y, to.y) + 1
            val y2 = max(from.y, to.y)

            val xSum = (x1..x2).sumOf { x -> (if (x in columnsWithoutGalaxies) emptySpaceSize else 1L) }
            val ySum = (y1..y2).sumOf { y -> (if (y in rowsWithoutGalaxies) emptySpaceSize else 1L) }
            xSum + ySum
        }
}

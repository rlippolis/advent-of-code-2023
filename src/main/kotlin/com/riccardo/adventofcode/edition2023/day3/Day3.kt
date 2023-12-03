package com.riccardo.adventofcode.edition2023.day3

import com.riccardo.adventofcode.Point2D

private val numberRegex = Regex("""\d+""")

fun solvePart1(input: String): Int {
    val lines = input.lines()
    return lines.mapIndexed { y, line ->
        numberRegex.findAll(line).mapNotNull { match ->
            match.value.takeIf { lines.find(xRange = match.range, y = y) { it != '.' && !it.isDigit() }.any() }?.toInt()
        }.sum()
    }.sum()
}

fun solvePart2(input: String): Int {
    val lines = input.lines()
    val gears = mutableMapOf<Point2D, MutableMap<Point2D, Int>>()

    lines.forEachIndexed { y, line ->
        numberRegex.findAll(line).forEach { match ->
            val number = match.value.toInt()
            val numberStartingPoint = Point2D(x = match.range.first, y = y)
            lines.find(xRange = match.range, y = y) { it == '*' }.forEach { gearPoint ->
                gears.computeIfAbsent(gearPoint) { mutableMapOf() }[numberStartingPoint] = number
            }
        }
    }

    return gears.values.filter { it.size == 2 }.sumOf { it.values.reduce(Int::times) }
}

private inline fun List<String>.find(xRange: IntRange, y: Int, crossinline charPredicate: (Char) -> Boolean): Sequence<Point2D> =
    xRange.asSequence()
        .map { x -> Point2D(x = x, y = y) }
        .flatMap { it.getNeighbours(includingDiagonal = true) }
        .filter { p -> this[p]?.let(charPredicate) ?: false }

private operator fun List<String>.get(p: Point2D): Char? {
    if (p.y < 0 || p.y > size - 1 || p.x < 0) return null
    return this[p.y].takeIf { p.x < it.length }?.let { it[p.x] }
}

package com.riccardo.adventofcode.edition2023.day18

import com.riccardo.adventofcode.Point2D
import kotlin.math.absoluteValue

fun solvePart1(input: String): Long = input.toDirections1().calculateArea()
fun solvePart2(input: String): Long = input.toDirections2().calculateArea()

fun String.toDirections1() = lines().map { it.split(' ') }.map { (dir, n) -> dir.first() to n.toInt() }
fun String.toDirections2() = lines().map { it.substringAfterLast(' ') }.map { hex ->
    val dir = when (hex[hex.lastIndex - 1]) {
        '0' -> 'R'
        '1' -> 'D'
        '2' -> 'L'
        '3' -> 'U'
        else -> error("Unknown direction: ${hex[hex.lastIndex - 1]}")
    }
    val n = hex.substring(2, hex.lastIndex - 1).toInt(radix = 16)
    dir to n
}

fun List<Pair<Char, Int>>.calculateArea(): Long {
    val points = runningFold(Point2D(x = 0, y = 0)) { p, (dir, n) ->
        when (dir) {
            'U' -> p.up(n)
            'D' -> p.down(n)
            'L' -> p.left(n)
            'R' -> p.right(n)
            else -> error("Unknown direction: $dir")
        }
    }
    return calculateInnerAreaWithShoelaceFormula(points) + calculateEdgeAreaWithPicksTheorem()
}

private fun calculateInnerAreaWithShoelaceFormula(points: List<Point2D>) =
    points.zipWithNext { p1, p2 -> p1.x.toLong() * p2.y.toLong() - p2.x.toLong() * p1.y.toLong() }
        .sum()
        .absoluteValue
        .div(2.0)
        .toLong()

private fun List<Pair<Char, Int>>.calculateEdgeAreaWithPicksTheorem() = sumOf { (_, l) -> l } / 2 + 1

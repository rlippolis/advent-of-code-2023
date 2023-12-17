package com.riccardo.adventofcode.edition2023.day16

import com.riccardo.adventofcode.Direction
import com.riccardo.adventofcode.Point2D
import java.util.*

fun solvePart1(input: String): Int = fireBeam(input.lines(), Point2D(x = 0, y = 0), Direction.Right)

fun solvePart2(input: String): Int = input.lines().let { grid ->
    listOf(
        grid.indices.flatMap { y ->
            listOf(
                fireBeam(grid, Point2D(x = 0, y = y), Direction.Right),
                fireBeam(grid, Point2D(x = grid[y].lastIndex, y = y), Direction.Left),
            )
        },
        grid.first().indices.flatMap { x ->
            listOf(
                fireBeam(grid, Point2D(x = x, y = 0), Direction.Down),
                fireBeam(grid, Point2D(x = x, y = grid.lastIndex), Direction.Up),
            )
        }
    ).flatten().max()
}

fun fireBeam(grid: List<String>, startingPoint: Point2D, startingDirection: Direction): Int {
    val visited = Array(grid.size) { Array(grid.first().length) { EnumSet.noneOf(Direction::class.java) } }
    val beams = mutableListOf(startingPoint to startingDirection)

    while (beams.isNotEmpty()) {
        val (point, dir) = beams.removeLast()
        if (!visited[point.y][point.x].add(dir)) continue // Skip when we've already visited this point from the same direction

        beamTraversals[dir]?.get(grid[point.y][point.x])?.forEach { d ->
            point.move(d).takeIf { it in grid }?.let { p -> beams.add(p to d) }
        }
    }

    return visited.sumOf { row -> row.sumOf { it.energyLevel() } }
}

operator fun List<String>.contains(p: Point2D): Boolean = p.y in indices && p.x in get(p.y).indices
fun EnumSet<Direction>.energyLevel(): Int = if (isEmpty()) 0 else 1

val beamTraversals = mapOf(
    Direction.Up to mapOf(
        '.'  to EnumSet.of(Direction.Up),
        '/'  to EnumSet.of(Direction.Right),
        '\\' to EnumSet.of(Direction.Left),
        '|'  to EnumSet.of(Direction.Up),
        '-'  to EnumSet.of(Direction.Left, Direction.Right),
    ),
    Direction.Down to mapOf(
        '.'  to EnumSet.of(Direction.Down),
        '/'  to EnumSet.of(Direction.Left),
        '\\' to EnumSet.of(Direction.Right),
        '|'  to EnumSet.of(Direction.Down),
        '-'  to EnumSet.of(Direction.Left, Direction.Right),
    ),
    Direction.Left to mapOf(
        '.'  to EnumSet.of(Direction.Left),
        '/'  to EnumSet.of(Direction.Down),
        '\\' to EnumSet.of(Direction.Up),
        '|'  to EnumSet.of(Direction.Up, Direction.Down),
        '-'  to EnumSet.of(Direction.Left),
    ),
    Direction.Right to mapOf(
        '.'  to EnumSet.of(Direction.Right),
        '/'  to EnumSet.of(Direction.Up),
        '\\' to EnumSet.of(Direction.Down),
        '|'  to EnumSet.of(Direction.Up, Direction.Down),
        '-'  to EnumSet.of(Direction.Right),
    ),
)

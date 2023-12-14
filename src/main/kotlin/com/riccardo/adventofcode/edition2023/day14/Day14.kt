package com.riccardo.adventofcode.edition2023.day14

import com.riccardo.adventofcode.mirror
import com.riccardo.adventofcode.transpose

typealias Dish = List<List<Char>>

fun solvePart1(input: String): Int = input.lines().map { it.toList() }.tilt(Direction.North).totalLoad()
fun solvePart2(input: String): Int = input.lines().map { it.toList() }.cycles(1000000000).totalLoad()

fun Dish.tilt(direction: Direction): Dish = when (direction) {
    Direction.North -> transpose().map(List<Char>::shiftRocksLeft).transpose()
    Direction.West -> map(List<Char>::shiftRocksLeft)
    Direction.South -> reversed().tilt(Direction.North).reversed()
    Direction.East -> mirror().tilt(Direction.West).mirror()
}

tailrec fun Dish.cycles(limit: Int, current: Int = 0, cache: MutableMap<Dish, Int> = mutableMapOf()): Dish {
    if (current == limit) return this
    val previous = cache.getOrPut(this) { current }
    val next = if (previous == current) {
        current + 1
    } else {
        val loop = current - previous
        val remainder = limit - current
        (current + 1) + ((remainder / loop) * loop)
    }
    return cycle().cycles(limit, next, cache)
}

fun Dish.cycle(): Dish {
    return Direction.entries.fold(this) { dish, direction -> dish.tilt(direction) }
}

fun List<Char>.shiftRocksLeft(): List<Char> = toMutableList().also { row ->
    row.forEachIndexed { idx, c ->
        if (c == '.') {
            for (i in (idx + 1)..lastIndex) {
                when (row[i]) {
                    '.' -> continue
                    'O' -> {
                        row[idx] = 'O'
                        row[i] = '.'
                    }
                }
                return@forEachIndexed
            }
        }
    }
}

fun Dish.totalLoad() = asReversed().withIndex().sumOf { (idx, row) -> row.count { it == 'O' } * (idx + 1) }

enum class Direction { North, West, South, East }

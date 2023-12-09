package com.riccardo.adventofcode.edition2023.day9

import com.riccardo.adventofcode.toLongList

fun solvePart1(input: String): Long = input.lines().sumOf { it.toLongList().extrapolate() }
fun solvePart2(input: String): Long = input.lines().sumOf { it.toLongList().reversed().extrapolate() }

fun List<Long>.extrapolate(): Long = lastOrNull()?.let { it + diffs().extrapolate() } ?: 0
fun List<Long>.diffs() = zipWithNext { a, b -> b - a }

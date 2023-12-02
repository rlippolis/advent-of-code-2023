package com.riccardo.adventofcode.edition2023.day2

typealias Subset = Map<String, Int>
typealias Game = List<Subset>

fun solvePart1(input: String): Int {
    val constraints: Subset = mapOf("red" to 12, "green" to 13, "blue" to 14)
    return input.parseGames().filterValues { it.satisfies(constraints) }.keys.sum()
}

fun solvePart2(input: String) = input.parseGames().values.sumOf { it.minimumSubset().power() }

private fun String.parseGames() = lineSequence().associate { line ->
    val (gameText, subsetsText) = line.split(':')
    val gameId = gameText.split(' ')[1].toInt()
    val subsets = subsetsText.splitToSequence(';')
        .map { it.trim().split(", ") }
        .map { occurrences ->
            occurrences.associate {
                val (amount, color) = it.split(' ')
                color to amount.toInt()
            }
        }.toList()
    gameId to subsets
}

private fun Game.satisfies(constraints: Subset): Boolean = all { it.satisfies(constraints) }
private fun Subset.satisfies(constraints: Subset): Boolean = all { (color, amount) -> constraints[color]?.let { amount <= it } ?: false }

private fun Game.minimumSubset(): Subset = fold(mutableMapOf()) { acc, subset ->
    subset.forEach { (color, amount) -> acc.merge(color, amount, Math::max) }
    acc
}
private fun Subset.power() = values.reduce(Int::times)

package com.riccardo.adventofcode.edition2023.day4

import kotlin.math.pow

fun solvePart1(input: String): Int = input.lineSequence()
    .map { line -> line.toCard().matches }
    .filter { it > 0 }
    .map { 2.0.pow(it - 1).toInt() }
    .sum()

fun solvePart2(input: String): Int {
    val cards = input.lines().map { it.toCard() }
    val cardCounts = MutableList(cards.size) { 1 }
    cards.forEachIndexed { idx, card -> IntRange(idx + 1, idx + card.matches).forEach { cardCounts[it] += cardCounts[idx] } }
    return cardCounts.sum()
}

private val lineRegex = Regex("""Card +(\d+): +((?:\d+ *)+) +\| +((?:\d+ *)+)""")

private fun String.toCard() = lineRegex.matchEntire(this)?.destructured?.let { (id, winningNrs, ownNrs) ->
    Card(id = id.toInt(), winningNrs = winningNrs.toIntSet(), ownNrs = ownNrs.toIntSet())
} ?: error("Unmatched input: '$this'")

private fun String.toIntSet(): Set<Int> = split(Regex("\\W+")).mapTo(hashSetOf()) { it.toInt() }

data class Card(val id: Int, val winningNrs: Set<Int>, val ownNrs: Set<Int>) {
    val matches = ownNrs.count(winningNrs::contains)
}

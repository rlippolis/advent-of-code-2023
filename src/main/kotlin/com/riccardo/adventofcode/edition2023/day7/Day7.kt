package com.riccardo.adventofcode.edition2023.day7

import kotlin.math.pow

fun solvePart1(input: String): Long = input.getRanking(isPart1 = true)
fun solvePart2(input: String): Long = input.getRanking(isPart1 = false)

private fun String.getRanking(isPart1: Boolean) = lineSequence()
    .map { it.split(' ') }
    .map { (cards, bid) -> Hand(cards, bid.toInt(), isPart1) }
    .sorted()
    .withIndex()
    .map { (idx, hand) -> (idx + 1).toLong() * hand.bid.toLong() }
    .sum()

class Hand(cards: String, val bid: Int, isPart1: Boolean) : Comparable<Hand> {
    private val handType = if (isPart1) {
        getHandType(cards)
    } else {
        getCardPermutations(cards).map { getHandType(it) }.minBy { it.ordinal }
    }

    private val handValue = run {
        val typeValue = (HandType.entries.size - handType.ordinal).toLong()
        val cardsValue = cards.map { (if (isPart1) cardTypesPart1 else cardTypesPart2).indexOf(it).toString() }
            .joinToString(separator = "") { it.padStart(2, '0') }.toLong()
        (10.0.pow(cards.length * 2).toLong() * typeValue) + cardsValue
    }

    private fun getHandType(cards: String): HandType {
        val groupedCards = cards.groupingBy { it }.eachCount().values
        return HandType.entries.first { type -> type.checker(groupedCards) }
    }

    private fun getCardPermutations(cards: String): List<String> = when {
        !cards.contains('J') -> listOf(cards)
        else -> cards.indexOf('J').let { idx ->
            cardTypesWithoutJoker.flatMap { c -> getCardPermutations(cards.replaceCharAt(idx, c)) }
        }
    }

    override fun compareTo(other: Hand): Int = this.handValue.compareTo(other.handValue)
}

enum class HandType(val checker: (Collection<Int>) -> Boolean) {
    FiveOfAKind({ cards -> cards.size == 1 }),
    FourOfAKind({ cards -> cards.size == 2 && 4 in cards }),
    FullHouse({ cards -> cards.size == 2 }),
    ThreeOfAKind({ cards -> 3 in cards }),
    TwoPair({ cards -> cards.size == 3 }),
    OnePair({ cards -> cards.size == 4 }),
    HighCard({ cards -> cards.size == 5 }),
}

private val cardTypesPart1 = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
private val cardTypesPart2 = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
private val cardTypesWithoutJoker = cardTypesPart2.filterNot { it == 'J' }

private fun String.replaceCharAt(idx: Int, c: Char) = take(idx) + c + drop(idx + 1)

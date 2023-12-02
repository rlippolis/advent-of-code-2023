package com.riccardo.adventofcode.edition2023.day1

fun solvePart1(input: String): Int = input.lineSequence()
    .map { line -> line.filter { it.isDigit() } }
    .map { it.first().digitToInt() * 10 + it.last().digitToInt() }
    .sum()

fun solvePart2(input: String): Int = input.lineSequence()
    .map { line ->
        val occurrences = digits.map { (text, value) ->
            Triple(line.indexOfOrMax(text), line.lastIndexOfOrMin(text), value)
        }
        val firstDigit = occurrences.minBy { it.first }.third
        val secondDigit = occurrences.maxBy { it.second }.third
        firstDigit * 10 + secondDigit
    }
    .sum()

private fun String.indexOfOrMax(string: String) = indexOf(string).takeIf { it >= 0 } ?: Int.MAX_VALUE
private fun String.lastIndexOfOrMin(string: String) = lastIndexOf(string).takeIf { it >= 0 } ?: Int.MIN_VALUE

private val digits = (1..9).associateBy { it.toString() } + mapOf(
    "one"   to 1,
    "two"   to 2,
    "three" to 3,
    "four"  to 4,
    "five"  to 5,
    "six"   to 6,
    "seven" to 7,
    "eight" to 8,
    "nine"  to 9,
)

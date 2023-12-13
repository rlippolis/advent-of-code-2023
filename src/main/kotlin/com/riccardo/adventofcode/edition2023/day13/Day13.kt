package com.riccardo.adventofcode.edition2023.day13

import kotlin.math.min

fun solvePart1(input: String): Int = input.summarize(fuzzy = false)
fun solvePart2(input: String): Int = input.summarize(fuzzy = true)

fun String.summarize(fuzzy: Boolean): Int = patterns()
    .mapNotNull { pattern -> pattern.findVerticalLine(fuzzy) ?: (pattern.findHorizontalLine(fuzzy)?.let { it * 100 }) }
    .sum()

fun String.patterns(): List<String> = lineSequence().fold(mutableListOf(StringBuilder())) { acc, line -> acc.also {
    if (line.isEmpty()) acc.add(StringBuilder()) else acc.last().also { if (it.isNotEmpty()) it.appendLine() }.append(line)
}}.map { it.toString() }

fun String.findHorizontalLine(fuzzy: Boolean): Int? = lines()
    .findLines(fuzzy, ::diffString)
    .maxOrNull()
    ?.let { it + 1 }

fun String.findVerticalLine(fuzzy: Boolean) = if (fuzzy) {
    lines().indices.maxOfOrNull { findVerticalLine(it) ?: -1 }?.takeUnless { it == -1 }
} else findVerticalLine(-1)

fun String.findVerticalLine(fuzzyIdx: Int): Int? = lines()
    .mapIndexed { idx, line -> line.toList().findLines(idx == fuzzyIdx, ::diffChar).toSet() }
    .reduce { a, b -> a.intersect(b) }
    .maxOrNull()
    ?.let { it + 1 }

fun <T> List<T>.findLines(fuzzy: Boolean, diff: (T, T) -> Int): List<Int> = (0..<lastIndex).filter { idx ->
    (1..min(idx + 1, size - idx - 1)).sumOf { x -> diff(get(idx - x + 1), get(idx + x)) } == (if (fuzzy) 1 else 0)
}

fun diffChar(a: Char, b: Char): Int = if (a == b) 0 else 1
fun diffString(a: String, b: String): Int = a.zip(b, ::diffChar).sum()

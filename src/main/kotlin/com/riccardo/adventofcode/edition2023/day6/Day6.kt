package com.riccardo.adventofcode.edition2023.day6

fun solvePart1(input: String): Int = input.lines()
    .let { (t, d) -> t.parseNumbers().zip(d.parseNumbers()) }
    .map { (t, d) -> countWaysToWin(t, d) }
    .reduce(Int::times)

fun solvePart2(input: String): Int = input.lines()
    .let { (t, d) -> t.parseNumber() to d.parseNumber() }
    .let { (t, d) -> countWaysToWin(t, d) }

private fun countWaysToWin(t: Long, d: Long) =
    (d / t..t - (d / t)).count { x -> (t - x) * x > d }

private val numberRegex = Regex("""\d+""")
private fun String.parseNumbers() = numberRegex.findAll(this).map { it.value.toLong() }
private fun String.parseNumber() = numberRegex.findAll(this).joinToString("") { it.value }.toLong()

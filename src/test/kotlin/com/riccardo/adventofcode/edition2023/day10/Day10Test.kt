package com.riccardo.adventofcode.edition2023.day10

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    private val example1Part1 = Utils.readFile(day = 10, name = "example1part1")
    private val example2Part1 = Utils.readFile(day = 10, name = "example2part1")

    private val example1Part2 = Utils.readFile(day = 10, name = "example1part2")
    private val example2Part2 = Utils.readFile(day = 10, name = "example2part2")
    private val example3Part2 = Utils.readFile(day = 10, name = "example3part2")

    private val input = Utils.readFile(day = 10, name = "input")

    @Test
    fun shouldSolveExample1Part1() {
        val result = solvePart1(example1Part1)
        assertEquals(4, result)
    }

    @Test
    fun shouldSolveExample2Part1() {
        val result = solvePart1(example2Part1)
        assertEquals(8, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(6786, result)
    }

    @Test
    fun shouldSolveExample1Part2() {
        val result = solvePart2(example1Part2)
        assertEquals(4, result)
    }

    @Test
    fun shouldSolveExample2Part2() {
        val result = solvePart2(example2Part2)
        assertEquals(8, result)
    }

    @Test
    fun shouldSolveExample3Part2() {
        val result = solvePart2(example3Part2)
        assertEquals(10, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(495, result)
    }
}

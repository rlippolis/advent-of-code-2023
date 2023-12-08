package com.riccardo.adventofcode.edition2023.day8

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {

    private val example1part1 = Utils.readFile(day = 8, name = "example1part1")
    private val example2part1 = Utils.readFile(day = 8, name = "example2part1")
    private val examplepart2 = Utils.readFile(day = 8, name = "examplepart2")
    private val input = Utils.readFile(day = 8, name = "input")

    @Test
    fun shouldSolveExample1Part1() {
        val result = solvePart1(example1part1)
        assertEquals(2, result)
    }

    @Test
    fun shouldSolveExample2Part1() {
        val result = solvePart1(example2part1)
        assertEquals(6, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(17263, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(examplepart2)
        assertEquals(6, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(14631604759649, result)
    }
}

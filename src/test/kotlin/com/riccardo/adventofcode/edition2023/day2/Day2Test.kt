package com.riccardo.adventofcode.edition2023.day2

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    private val example = Utils.readFile(day = 2, name = "example")
    private val input = Utils.readFile(day = 2, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(8, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(2551, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(2286, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(62811, result)
    }
}

package com.riccardo.adventofcode.edition2023.day7

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {

    private val example = Utils.readFile(day = 7, name = "example")
    private val input = Utils.readFile(day = 7, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(6440, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(250058342, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(5905, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(250506580, result)
    }
}

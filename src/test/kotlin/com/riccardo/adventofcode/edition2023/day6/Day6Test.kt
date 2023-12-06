package com.riccardo.adventofcode.edition2023.day6

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    private val example = Utils.readFile(day = 6, name = "example")
    private val input = Utils.readFile(day = 6, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(288, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(3317888, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(71503, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(24655068, result)
    }
}

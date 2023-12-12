package com.riccardo.adventofcode.edition2023.day12

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    private val example = Utils.readFile(day = 12, name = "example")
    private val input = Utils.readFile(day = 12, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(21, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(8193, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(525152, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(45322533163795, result)
    }
}

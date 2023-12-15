package com.riccardo.adventofcode.edition2023.day15

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    private val example = Utils.readFile(day = 15, name = "example")
    private val input = Utils.readFile(day = 15, name = "input")

    @Test
    fun shouldSolveExampleHash() {
        val result = solvePart1("HASH")
        assertEquals(52, result)
    }

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(1320, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(513172, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(145, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(237806, result)
    }
}

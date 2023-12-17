package com.riccardo.adventofcode.edition2023.day17

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Day17Test {

    private val example = Utils.readFile(day = 17, name = "example")
    private val input = Utils.readFile(day = 17, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(102, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(1065, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example)
        assertEquals(94, result)
    }

    @Test
    fun shouldSolveExtraExamplePart2() {
        val x = """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
        """.trimIndent()
        assertEquals(71, solvePart2(x))
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertTrue(result < 1256)
        assertEquals(1249, result)
    }
}

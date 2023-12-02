package com.riccardo.adventofcode.edition2023.day1

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {

    private val example1 = Utils.readFile(day = 1, name = "example1")
    private val example2 = Utils.readFile(day = 1, name = "example2")
    private val input = Utils.readFile(day = 1, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example1)
        assertEquals(142, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(54968, result)
    }

    @Test
    fun shouldSolveExamplePart2() {
        val result = solvePart2(example2)
        assertEquals(281, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(54094, result)
    }
}

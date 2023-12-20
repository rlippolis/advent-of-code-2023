package com.riccardo.adventofcode.edition2023.day20

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    private val example1 = Utils.readFile(day = 20, name = "example1")
    private val example2 = Utils.readFile(day = 20, name = "example2")
    private val input = Utils.readFile(day = 20, name = "input")

    @Test
    fun shouldSolveExample1Part1() {
        val result = solvePart1(example1)
        assertEquals(32000000, result)
    }

    @Test
    fun shouldSolveExample2Part1() {
        val result = solvePart1(example2)
        assertEquals(11687500, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(898557000, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input)
        println(result)
        assertEquals(238420328103151, result)
    }
}

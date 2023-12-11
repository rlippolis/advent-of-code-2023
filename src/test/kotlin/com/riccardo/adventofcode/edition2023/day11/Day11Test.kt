package com.riccardo.adventofcode.edition2023.day11

import com.riccardo.adventofcode.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Day11Test {

    private val example = Utils.readFile(day = 11, name = "example")
    private val input = Utils.readFile(day = 11, name = "input")

    @Test
    fun shouldSolveExamplePart1() {
        val result = solvePart1(example)
        assertEquals(374, result)
    }

    @Test
    fun shouldSolveInputPart1() {
        val result = solvePart1(input)
        println(result)
        assertEquals(9965032, result)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "10,1030",
        "100,8410",
    ])
    fun shouldSolveExamplePart2(emptySpaceSize: Long, expectedTotalSize: Long) {
        val result = solvePart2(example, emptySpaceSize)
        assertEquals(expectedTotalSize, result)
    }

    @Test
    fun shouldSolveInputPart2() {
        val result = solvePart2(input, 1_000_000)
        println(result)
        assertEquals(550358864332, result)
    }
}

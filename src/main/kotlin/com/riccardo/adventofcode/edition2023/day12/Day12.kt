package com.riccardo.adventofcode.edition2023.day12

import com.riccardo.adventofcode.toIntList

fun solvePart1(input: String): Long = input.lineSequence().sumOf { line ->
    val (arrangement, groupsStr) = line.split(" ")
    val groups = groupsStr.toIntList(separator = ",")
    countPossibilities(arrangement = arrangement, groups = groups)
}

fun solvePart2(input: String): Long = input.lineSequence().sumOf { line ->
        val (arrangement, groupsStr) = line.split(" ")
        val groups = groupsStr.toIntList(separator = ",")

        val repeatedArrangements = List(5) { arrangement }.joinToString(separator = "?")
        val repeatedGroups = List(5) { groups }.flatten()

        countPossibilities(arrangement = repeatedArrangements, groups = repeatedGroups)
    }

fun countPossibilities(arrangement: String, groups: List<Int>, state: State = State(), cache: MutableMap<State, Long> = mutableMapOf()): Long =
    cache.getOrPut(state) {
        if (state.currentIdx == arrangement.length) { // Reached the end of the string
            // Check if we successfully finished all the groups
            if (state.currentGroupIdx == groups.size && state.groupCount <= 0) 1 else 0
        } else {
            val maxGroupCount = if (state.currentGroupIdx < groups.size) groups[state.currentGroupIdx] else 0
            when (arrangement[state.currentIdx]) {
                '.' -> handleDot(arrangement, groups, state, maxGroupCount, cache)
                '#' -> if (state.currentGroupIdx < groups.size) handleHash(arrangement, groups, state, maxGroupCount, cache) else 0
                '?' -> handleDot(arrangement, groups, state, maxGroupCount, cache) +
                        if (state.currentGroupIdx < groups.size) handleHash(arrangement, groups, state, maxGroupCount, cache) else 0
                else -> error("Unknown char: ${arrangement[state.currentIdx]}")
            }
        }
    }

private fun handleDot(arrangement: String, groups: List<Int>, state: State, maxGroupCount: Int, cache: MutableMap<State, Long>) =
    if (state.groupCount in 1..<maxGroupCount) 0 // Last group was unfinished, so we should not have a dot here
    else countPossibilities(
        arrangement = arrangement,
        groups = groups,
        state = state.copy(
            currentIdx = state.currentIdx + 1,
            currentGroupIdx = state.currentGroupIdx,
            groupCount = 0,
        ),
        cache = cache,
    )

private fun handleHash(arrangement: String, groups: List<Int>, state: State, maxGroupCount: Int, cache: MutableMap<State, Long>) =
    if (state.groupCount == -1 || state.groupCount >= maxGroupCount) 0
    else {
        val newGroupCount = state.groupCount + 1
        val groupFinished = newGroupCount == maxGroupCount
        countPossibilities(
            arrangement = arrangement,
            groups = groups,
            state = state.copy(
                currentIdx = state.currentIdx + 1,
                currentGroupIdx = if (groupFinished) state.currentGroupIdx + 1 else state.currentGroupIdx,
                groupCount = if (groupFinished) -1 else newGroupCount,
            ),
            cache = cache,
        )
    }

data class State(val currentIdx: Int = 0, val currentGroupIdx: Int = 0, val groupCount: Int = 0)

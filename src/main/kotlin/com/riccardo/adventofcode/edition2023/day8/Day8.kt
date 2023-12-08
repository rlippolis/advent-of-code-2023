package com.riccardo.adventofcode.edition2023.day8

import com.riccardo.adventofcode.Utils.lcm
import com.riccardo.adventofcode.Utils.repeat

fun solvePart1(input: String): Int = input.parseInput().let { (instructions, nodes) ->
    instructions.follow(start = "AAA", nodes = nodes).takeWhile { it != "ZZZ" }.count()
}

fun solvePart2(input: String): Long = input.parseInput().let { (instructions, nodes) ->
    nodes.keys.filter { it.endsWith('A') }
        .map { node -> instructions.follow(start = node, nodes = nodes).takeWhile { it.last() != 'Z' }.count().toLong() }
        .lcm()
}

fun Sequence<Char>.follow(start: String, nodes: Map<String, Node>): Sequence<String> =
    runningFold(start) { current, instruction -> nodes.getValue(current)[instruction] }

fun String.parseInput(): Pair<Sequence<Char>, Map<String, Node>> = lines().let { lines ->
    val instructions = lines.first().asSequence().repeat()
    val nodes = lines.drop(2).map(String::toNode).associateBy(Node::id)
    instructions to nodes
}

val nodeRegex = Regex("""(\w+) = \((\w+), (\w+)\)""")
fun String.toNode() = nodeRegex.matchEntire(this)?.destructured
    ?.let { (id, left, right) -> Node(id = id, left = left, right = right) } ?: error("Invalid input: $this")

data class Node(val id: String, val left: String, val right: String) {
    operator fun get(instruction: Char) = when (instruction) {
        'L' -> left
        'R' -> right
        else -> error("Unknown instruction: $instruction")
    }
}

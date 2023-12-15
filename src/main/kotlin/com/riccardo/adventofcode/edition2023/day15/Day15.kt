package com.riccardo.adventofcode.edition2023.day15

fun solvePart1(input: String): Int = input.splitToSequence(',').sumOf(String::hash)

fun solvePart2(input: String): Int = input.splitToSequence(',')
    .map(String::toOperation)
    .fold(mutableMapOf<Int, MutableMap<String, Int>>()) { acc, op ->
        when (op) {
            is AddOperation -> acc.computeIfAbsent(op.boxId) { mutableMapOf() }[op.box] = op.lens
            is RemoveOperation -> acc[op.boxId]?.remove(op.box)
        }
        acc
    }.entries.sumOf { (idx, lenses) ->
        (idx + 1) * lenses.values.mapIndexed { lensIdx, lens -> (lensIdx + 1) * lens }.sum()
    }

fun String.hash(): Int = this.asSequence().map(Char::code).fold(0) { acc, x -> ((acc + x) * 17) % 256 }

fun String.toOperation(): Operation = when {
    contains('=') -> split('=').let { (box, lens) -> AddOperation(box = box, lens = lens.toInt()) }
    contains('-') -> RemoveOperation(box = dropLast(1))
    else -> error("Invalid operation: $this")
}

sealed class Operation(val box: String) {
    val boxId = box.hash()
}
class AddOperation(box: String, val lens: Int): Operation(box)
class RemoveOperation(box: String): Operation(box)

package com.riccardo.adventofcode.edition2023.day5

import com.riccardo.adventofcode.Utils.overlapsWith
import kotlin.math.max
import kotlin.math.min

fun solvePart1(input: String): Long {
    val (seeds, almanac) = input.parseMaps()
    return seeds.minOf { almanac.translate(it) }
}

fun solvePart2(input: String): Long {
    val (seedRanges, almanac) = input.parseMaps()

    return seedRanges.asSequence()
        .chunked(2)
        .map { (start, len) -> start..<start + len }
        .flatMap { almanac.translate(it) }
        .minOf { it.first() }
}

private fun String.parseMaps(): Pair<List<Long>, Almanac> {
    val lines = lines()
    val seeds = lines.first().substringAfter("seeds: ").toLongList()

    val maps = lines.drop(1).fold(mutableListOf<MutableList<String>>()) { acc, line ->
        if (line.isNotBlank()) {
            if (line.first().isDigit()) {
                acc.last().add(line)
            } else {
                acc.add(mutableListOf())
            }
        }
        acc
    }.map { mapLines ->
        AlmanacMap(offsets = mapLines
            .map { it.toLongList() }
            .map { (dest, src, len) -> MapOffset(sourceRange = src..<src + len, offset = dest - src) }
        )
    }

    return seeds to Almanac(maps)
}

private fun String.toLongList() = split(' ').map { it.toLong() }

private data class Almanac(val maps: List<AlmanacMap>) {

    fun translate(number: Long) = maps.fold(number) { current, map -> map.translate(current) }

    fun translate(range: LongRange): List<LongRange> = maps.fold(listOf(range)) { ranges, map ->
        ranges.flatMap { range -> map.translate(range) }
    }
}

private data class AlmanacMap(val offsets: List<MapOffset>) {
    fun translate(number: Long): Long {
        for (offset in offsets) {
            if (number in offset.sourceRange) {
                return offset.translate(number)
            }
        }
        return number
    }

    fun translate(range: LongRange): List<LongRange> = offsets.fold(listOf(range) to mutableListOf<LongRange>()) { (ranges, translatedRanges), offset ->
        val newRanges = mutableListOf<LongRange>()
        val srcRange = offset.sourceRange

        ranges.forEach { range ->
            when {
                !range.overlapsWith(srcRange) -> newRanges.add(range)
                else -> {
                    if (range.first < srcRange.first) newRanges.add(range.first..<srcRange.first)
                    if (range.last > srcRange.last) newRanges.add((srcRange.last + 1)..range.last)
                    translatedRanges.add(offset.translate(max(srcRange.first, range.first))..offset.translate(min(srcRange.last, range.last)))
                }
            }
        }
        newRanges to translatedRanges
    }.let { (ranges, translatedRanges) -> ranges + translatedRanges }
}

private data class MapOffset(val sourceRange: LongRange, val offset: Long) {
    fun translate(number: Long) = number + offset
}

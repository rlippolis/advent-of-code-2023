package com.riccardo.adventofcode.edition2023.day19

import java.util.*

fun solvePart1(input: String): Int {
    val (workflows, parts) = input.parseInput()
    return parts.filter { part ->
        var workflow = workflows.getValue("in")
        var target: WorkflowTarget
        do {
            target = workflow.firstNotNullOf { it.check(part) }
            if (target is NextWorkflow) {
                workflow = workflows.getValue(target.id)
            }
        } while (target is NextWorkflow)
        target is Accepted
    }.sumOf { it.value() }
}

fun solvePart2(input: String): Long {
    val (workflows, _) = input.parseInput()

    val startWorkflow = workflows.getValue("in")
    val startRange = XmasPartRange(x = 1..4000L, m = 1..4000L, a = 1..4000L, s = 1..4000L)
    val states = ArrayDeque<Pair<XmasPartRange, List<WorkflowRule>>>().apply { add(startRange to startWorkflow) }

    val acceptedRanges = mutableSetOf<XmasPartRange>()

    while (states.isNotEmpty()) {
        val (range, workflow) = states.removeFirst()
        workflow.fold<WorkflowRule, XmasPartRange?>(range) { acc, rule ->
            acc?.let {
                val (ifTrue, ifFalse) = rule.split(acc)
                ifTrue?.also {
                    when (val target = rule.target) {
                        is NextWorkflow -> states.add(it to workflows.getValue(target.id))
                        is Accepted -> acceptedRanges.add(it)
                        is Rejected -> {}
                    }
                }
                ifFalse
            }
        }
    }

    return acceptedRanges.sumOf { it.nrOfPossibilities() }
}

fun String.parseInput(): Pair<Map<String, List<WorkflowRule>>, List<XmasPart>> {
    val (workflowsStr, partsStr) = split("${System.lineSeparator()}${System.lineSeparator()}")
    return workflowsStr.lines().associate(String::toWorkflow) to partsStr.lines().map(String::toXmasPart)
}

fun String.toWorkflow(): Pair<String, List<WorkflowRule>> {
    val (id, rules) = workflowRegex.matchEntire(this)?.destructured ?: error("Invalid workflow: $this")
    return id to rules.split(',')
        .map { rule ->
            val (input, sign, threshold, target) = workflowRuleRegex.matchEntire(rule)?.destructured
                ?: return@map AlwaysTrueWorkflowRule(target = rule.toWorkflowTarget())
            ConditionalWorkflowRule(
                input = input,
                sign = when (sign) {
                    "<" -> -1
                    ">" -> 1
                    else -> error("Unknown sign: $sign")
                },
                threshold = threshold.toInt(),
                target = target.toWorkflowTarget(),
            )
        }
}

fun String.toWorkflowTarget() = when (this) {
    "A" -> Accepted
    "R" -> Rejected
    else -> NextWorkflow(this)
}

fun String.toXmasPart(): XmasPart = xmasPartRegex.matchEntire(this)?.destructured?.let { (x, m, a, s) ->
    XmasPart(x = x.toInt(), m = m.toInt(), a = a.toInt(), s = s.toInt())
} ?: error("Invalid Xmas part: #$this")

val workflowRegex = Regex("""(\w+)\{(.+?)}""")
val workflowRuleRegex = Regex("""(\w+)(.)(\d+):(\w+)""")

val xmasPartRegex = Regex("""\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""")

data class XmasPart(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun value(): Int = x + m + a + s
}

data class XmasPartRange(val x: LongRange, val m: LongRange, val a: LongRange, val s: LongRange) {
    fun nrOfPossibilities(): Long = x.size() * m.size() * a.size() * s.size()

    private fun LongRange.size() = (last - first) + 1
}

sealed interface WorkflowRule {
    val target: WorkflowTarget
    fun check(xmasPart: XmasPart): WorkflowTarget?
    fun split(range: XmasPartRange): Pair<XmasPartRange?, XmasPartRange?>
}

data class ConditionalWorkflowRule(
    val input: String,
    val sign: Int,
    val threshold: Int,
    override val target: WorkflowTarget
): WorkflowRule {
    override fun check(xmasPart: XmasPart): WorkflowTarget? = if (xmasPart.getValue().compareTo(threshold) == sign) target else null
    override fun split(range: XmasPartRange): Pair<XmasPartRange?, XmasPartRange?> {
        val r = range.getRange()
        if (threshold < r.first || threshold > r.last) return null to range
        return if (sign < 0) {
            range.withRange(r.first..<threshold) to range.withRange(threshold..r.last)
        } else {
            range.withRange(threshold+1..r.last) to range.withRange(r.first..threshold)
        }
    }

    private fun XmasPart.getValue() = when (input) {
        "x" -> x
        "m" -> m
        "a" -> a
        "s" -> s
        else -> error("Invalid input: $input")
    }

    private fun XmasPartRange.getRange() = when (input) {
        "x" -> x
        "m" -> m
        "a" -> a
        "s" -> s
        else -> error("Invalid input: $input")
    }

    private fun XmasPartRange.withRange(range: LongRange) = when (input) {
        "x" -> copy(x = range)
        "m" -> copy(m = range)
        "a" -> copy(a = range)
        "s" -> copy(s = range)
        else -> error("Invalid input: $input")
    }
}

data class AlwaysTrueWorkflowRule(override val target: WorkflowTarget): WorkflowRule {
    override fun check(xmasPart: XmasPart): WorkflowTarget = target
    override fun split(range: XmasPartRange): Pair<XmasPartRange?, XmasPartRange?> = range to null
}

sealed interface WorkflowTarget
data class NextWorkflow(val id: String): WorkflowTarget
data object Accepted: WorkflowTarget
data object Rejected: WorkflowTarget

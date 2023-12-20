package com.riccardo.adventofcode.edition2023.day20

import com.riccardo.adventofcode.Utils.lcm

fun solvePart1(input: String): Long {
    val moduleConfig = ModuleConfiguration(input = input)
    repeat(1000) { moduleConfig.pressButton() }
    return moduleConfig.nrOfLowPulses * moduleConfig.nrOfHighPulses
}

fun solvePart2(input: String): Long {
    val moduleConfig = ModuleConfiguration(input = input, moduleToTrack = "rx")
    while (true) {
        if (moduleConfig.pressButton()) break
    }
    return moduleConfig.getNrOfButtonPressesNeededForTrackedModule()
}

enum class ModuleType { Broadcast, FlipFlop, Conjunction }

data class Pulse(val from: String, val to: String, val pulse: Boolean)

class ModuleConfiguration(input: String, moduleToTrack: String? = null) {
    private val modules = mutableMapOf<String, ModuleType>()
    private val inputs = mutableMapOf<String, MutableSet<String>>()
    private val outputs = mutableMapOf<String, MutableSet<String>>()
    private val flipFlopStates = mutableMapOf<String, Boolean>()
    private val conjunctionStates = mutableMapOf<String, MutableMap<String, Boolean>>()
    private val inputsToCheck = mutableMapOf<String, MutableList<Long>>()

    private var buttonPresses = 0L

    var nrOfLowPulses = 0L
        private set
    var nrOfHighPulses = 0L
        private set

    init {
        input.lineSequence().forEach { line ->
            val (id, outputList) = line.split(" -> ")
            val (name, type) = when (id.first()) {
                '%' -> id.drop(1) to ModuleType.FlipFlop
                '&' -> id.drop(1) to ModuleType.Conjunction
                else -> id to ModuleType.Broadcast
            }
            modules[name] = type
            outputList.split(", ").forEach { output ->
                outputs.getOrPut(name) { mutableSetOf() }.add(output)
                inputs.getOrPut(output) { mutableSetOf() }.add(name)
            }
        }

        modules.filterValues { it == ModuleType.FlipFlop }.keys.forEach { flipFlopStates[it] = false }

        modules.filterValues { it == ModuleType.Conjunction }.keys.forEach { conjunction ->
            val conjunctionInputs = inputs.getValue(conjunction)
            val conjunctionState = mutableMapOf<String, Boolean>()
            conjunctionInputs.forEach { conjunctionState[it] = false }
            conjunctionStates[conjunction] = conjunctionState
        }

        if (moduleToTrack != null) {
            // Gets the inputs for the module we need to track.
            // Assumes the module is dependent on a single Conjunction that's waiting for its inputs to be false
            val inputForModule = inputs.getValue(moduleToTrack).single()
            check(modules[inputForModule] == ModuleType.Conjunction)
            inputs.getValue(inputForModule).forEach { inputModule -> inputsToCheck[inputModule] = mutableListOf() }
        }
    }

    fun pressButton(): Boolean {
        buttonPresses++
        val pulses = mutableListOf(Pulse(from = "button", to = "broadcaster", pulse = false))
        while (pulses.isNotEmpty()) {
            val (origin, module, pulse) = pulses.removeFirst()

            // Register stats
            if (pulse) nrOfHighPulses++ else nrOfLowPulses++

            when (modules[module]) {
                ModuleType.Broadcast -> {
                    // Broadcast sends the received pulse to all its outputs
                    outputs.getValue(module).forEach { pulses.add(Pulse(from = module, to = it, pulse = pulse).track()) }
                }
                ModuleType.FlipFlop -> {
                    // FlipFlop does nothing when a high pulse is received
                    if (!pulse) {
                        // Flip state and send new state when a low pulse is received
                        val newState = !flipFlopStates.getValue(module)
                        flipFlopStates[module] = newState
                        outputs.getValue(module).forEach { pulses.add(Pulse(from = module, to = it, pulse = newState).track()) }
                    }
                }
                ModuleType.Conjunction -> {
                    // First update internal state
                    val conjunctionState = conjunctionStates.getValue(module)
                    conjunctionState[origin] = pulse
                    // If all states are high, send low, otherwise send high
                    val pulseToSend = conjunctionState.values.any { !it }
                    outputs.getValue(module).forEach { pulses.add(Pulse(from = module, to = it, pulse = pulseToSend).track()) }
                }
                null -> {}
            }

            if (endStateForTrackedModuleReached()) {
                return true
            }
        }
        return false
    }

    fun getNrOfButtonPressesNeededForTrackedModule(): Long {
        check(endStateForTrackedModuleReached())
        return inputsToCheck.values.map { it.last() - it.first() }.lcm()
    }

    private fun Pulse.track(): Pulse = this.also { p ->
        if (!p.pulse && p.to in inputsToCheck) {
            inputsToCheck.getValue(p.to).add(buttonPresses)
        }
    }

    private fun endStateForTrackedModuleReached() =
        inputsToCheck.isNotEmpty() && inputsToCheck.values.all { it.size == 2 }
}

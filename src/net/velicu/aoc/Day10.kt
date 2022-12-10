package net.velicu.aoc

import java.io.File

class Day10 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {

      val states =
        File("inputs/10.input.txt").bufferedReader().lineSequence()
          .map(::parseInstruction)
          .scan(State()) { state, instruction -> instruction.execute(state) }
          .map { state -> state.cycle to state }
          .toMap()

      // println(states.values)

      val stateList = states.keys.toList()
      val interestingStates =
        listOf(20, 60, 100, 140, 180, 220)
          .map { it to states[stateList.atOrBefore(it)]!! }
      // println(interestingStates)
      println(interestingStates.sumOf { it.first * it.second.x })
    }

    private fun parseInstruction(instruction: String): Instruction {
      val split = instruction.split(" ")
      return when (split[0]) {
        "noop" -> Instruction.Noop()
        "addx" -> Instruction.Addx(split[1].toInt())
        else -> throw IllegalArgumentException()
      }
    }

    private fun <T : Comparable<T>> List<T>.atOrBefore(key: T): T = binarySearch(key).let {
      if (it >= 0) {
        return get(it)
      } else {
        return get(-(it + 1) - 1)
      }
    }

    private data class State(val x: Int = 1, val cycle: Int = 1) {
      fun signalStrength(): Int = x * cycle
      override fun toString(): String = "[$cycle] $x"
    }

    private sealed interface Instruction {
      fun execute(state: State): State

      class Noop : Instruction {
        override fun execute(state: State): State = State(state.x, state.cycle + 1)
        override fun toString(): String = "Noop"
      }

      class Addx(val arg: Int) : Instruction {
        override fun execute(state: State): State = State(state.x + arg, state.cycle + 2)
        override fun toString(): String = "Addx($arg)"
      }
    }
  }
}
package net.velicu.aoc

import PeekingIterator
import java.io.File
import kotlin.math.abs

class Day10Problem2 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {

      val stateIterator =
        File("inputs/10.input.txt").bufferedReader().lineSequence()
          .map(::parseInstruction)
          .scan(State()) { state, instruction -> instruction.execute(state) }
          .iterator()
          .let { PeekingIterator(it) }

      var lastStateBeforeBusting: State? = null
      val displayMatrix: List<List<Char>> = List(6) { row -> List(40) { col ->
        val clock = (row * 40 + col) + 1

        // println("row $row col $col advancing to clock $clock")
        while (stateIterator.peek().cycle <= clock) {
          lastStateBeforeBusting = stateIterator.next()
        }
        // println("got to $lastStateBeforeBusting (next would be ${stateIterator.peek()}")

        lastStateBeforeBusting!!.let {
          if (abs(it.x - col) <= 1) {
            '#'
          } else {
            '.'
          }
        }
      } }

      displayMatrix.forEach { row ->
        row.forEach { column -> print(column) }
        println()
      }
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
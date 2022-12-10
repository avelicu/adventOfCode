package net.velicu.aoc

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import net.velicu.aoc.Day9Problem1.Companion.Direction.Companion.toDirection

class Day9Problem1 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      var state =
        SnakeState(
          headPosition = Position(0, 0),
          tailPosition = Position(0, 0))

      val tailTouchedPositions = mutableSetOf(state.tailPosition)

      File("inputs/9.input.txt").bufferedReader().lineSequence()
        .map(::parseInstruction)
        .forEach { instruction ->
          println("$instruction")
          repeat(instruction.steps) {
            state = state.inDirection(instruction.direction)
            tailTouchedPositions.add(state.tailPosition)
            println("$state")
          }
        }

      println(tailTouchedPositions.size)
    }


    private fun parseInstruction(line: String) =
      line.split(" ").let { Instruction(it[0][0].toDirection(), it[1].toInt()) }

    private data class SnakeState(val headPosition: Position, val tailPosition: Position) {
      fun inDirection(direction: Direction): SnakeState {
        val newHeadPosition =
          Position(headPosition.x + direction.dx, headPosition.y + direction.dy)

        val newTailPosition = run {
          if (newHeadPosition biggestLineDistance tailPosition <= 1) {
            tailPosition
          } else if (headPosition.x == newHeadPosition.x) {
            Position(headPosition.x, headPosition.y + (newHeadPosition.y - headPosition.y) / 2)
          } else if (headPosition.y == newHeadPosition.y) {
            Position(headPosition.x + (newHeadPosition.x - headPosition.x) / 2, headPosition.y)
          } else {
            throw IllegalStateException(
              "Illegal head move in both x and y axes, from $headPosition to $newHeadPosition")
          }
        }

        return SnakeState(newHeadPosition, newTailPosition)
      }
    }

    private data class Position(val x: Int, val y: Int) {
      infix fun biggestLineDistance(that: Position): Int =
        max(abs(that.x - x), abs(that.y - y))
    }
    private data class Instruction(val direction: Direction, val steps: Int)
    private enum class Direction(val dx: Int, val dy: Int) {
      L(-1, 0),
      R(1, 0),
      U(0, -1),
      D(0, 1);

      companion object {
        fun Char.toDirection(): Direction {
          return when (this) {
            'L' -> L
            'R' -> R
            'U' -> U
            'D' -> D
            else -> throw IllegalArgumentException()
          }
        }
      }
    }
  }
}
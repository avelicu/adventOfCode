package net.velicu.aoc

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import net.velicu.aoc.Day9Problem2.Companion.Direction.Companion.toDirection

class Day9Problem2 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      listOf(2, 10).forEach { solveForSize(it) }
    }

    private fun solveForSize(size: Int) {
      var state = SnakeState.initialSnakeOf(size)

      val tailTouchedPositions = mutableSetOf(state.tailPosition)

      File("inputs/9.input.txt").bufferedReader().lineSequence()
        .map(::parseInstruction)
        .forEach { instruction ->
          // println()
          // println("$instruction")
          repeat(instruction.steps) {
            state = state.inDirection(instruction.direction)
            tailTouchedPositions.add(state.tailPosition)
            // println(prettyPrint(state))
            // println()
          }
        }

      println("$size: ${tailTouchedPositions.size}")
    }

    private fun prettyPrint(state: SnakeState): String {
      val maxx = 15 //state.positions.maxOf { it.x }
      val minx = -15 // state.positions.minOf { it.x }
      val maxy = 15 // state.positions.maxOf { it.y }
      val miny = -15 // state.positions.minOf { it.y }
      val flattenedPositionMap =
        state.positions.mapIndexed { i, pos -> pos to i }.let {
          sequenceOf(Position(0, 0) to "s").plus(it)
        }.toMap()

      val b = StringBuilder().append(" ".repeat(3))
      for (x in minx..maxx) {
        abs(x).toString()
          .let { if (it.length<=1) it else " " }
          .let { b.append(it) }
      }
      b.appendLine()

      for (y in miny..maxy) {
        b.append(y.toString().padStart(3))
        for (x in minx..maxx) {
          b.append(flattenedPositionMap.get(Position(x, y)) ?: ".")
        }
        b.appendLine()
      }

      return b.toString()
    }


    private fun parseInstruction(line: String) =
      line.split(" ").let { Instruction(it[0][0].toDirection(), it[1].toInt()) }

    private data class SnakeState(val positions: List<Position>) {
      fun inDirection(direction: Direction): SnakeState {
        val transformedPositions = sequence {
          var head = transformHead(positions.first(), direction).also { yield(it) }

          for (oldTail in positions.drop(1)) {
            head = transformTail(oldTail, head).also { yield(it) }
          }
        }

        return SnakeState(transformedPositions.toList())
      }

      val tailPosition: Position get() = positions.last()

      private fun transformHead(headPosition: Position, direction: Direction) =
        Position(headPosition.x + direction.dx, headPosition.y + direction.dy)

      private fun transformTail(tail: Position, head: Position): Position {
        return if (head biggestLineDistance tail <= 1) {
          tail
        } else if (head.x == tail.x) {
          Position(tail.x, tail.y + sign(head.y - tail.y))
        } else if (head.y == tail.y) {
          Position(tail.x + sign(head.x - tail.x), tail.y)
        } else {
          Position(tail.x + sign(head.x - tail.x), tail.y + sign(head.y - tail.y))
        }
      }

      companion object {
        fun initialSnakeOf(snakeSize: Int) = SnakeState(List(snakeSize) { Position(0, 0) })
      }
    }

    private fun sign(i: Int): Int {
      return when {
        i > 0 -> 1
        i == 0 -> 0
        i < 0 -> -1
        else -> throw IllegalStateException()
      }
    }
    private data class Position(val x: Int, val y: Int) {
      infix fun biggestLineDistance(that: Position): Int =
        max(abs(that.x - x), abs(that.y - y))

      override fun toString(): String = "($x, $y)"
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
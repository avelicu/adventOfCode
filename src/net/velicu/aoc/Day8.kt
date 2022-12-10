package net.velicu.aoc

import java.io.File
import kotlin.math.max
import net.velicu.aoc.Day8.Companion.Sweeper.BottomToTop
import net.velicu.aoc.Day8.Companion.Sweeper.LeftToRight
import net.velicu.aoc.Day8.Companion.Sweeper.RightToLeft
import net.velicu.aoc.Day8.Companion.Sweeper.TopToBottom

class Day8 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val arr = File("inputs/8.small.txt")
        .readLines()
        .map { line -> line.toCharArray().map { char -> char.toString().toInt() }.toTypedArray() }
        .toTypedArray()

      val sweepers = listOf(LeftToRight, RightToLeft, TopToBottom, BottomToTop)

      val visibilitiesBySweeper =
        sweepers.map { sweeper ->
          val visible: Array<Array<Boolean>> = Array(arr.size) { Array(arr[0].size) { false } }
          var tallest = 0
          sweeper.sweep(arr,
                        onPrimaryIteration = { tallest = -1 },
                        onSecondaryIteration = { i, j, v ->
                          if (v > tallest) {
                            visible[i][j] = true
                            tallest = v
                          }
                        })
          visible
        }

      val oredVisibilities = visibilitiesBySweeper.reduce { first, second ->
        Array(first.size) { i -> Array(first[i].size) { j -> first[i][j] || second[i][j] } }
      }

      val visiblesCount = oredVisibilities.map { row -> row.count { it } }.sum()
      println(visiblesCount)

      val scoresBySweeper =
        sweepers.map { sweeper ->
          val sightedTreeCount: Array<Array<Int>> = Array(arr.size) { Array(arr[0].size) { 0 } }
          val stack = mutableListOf<Int>()

          println(sweeper)
          sweeper.sweep(arr,
                        onPrimaryIteration = {
                          stack.clear()
                        },
                        onSecondaryIteration = { i, j, v ->
                            while (stack.isNotEmpty() && stack.last() < v) {
                              stack.removeLast()
                            }
                            sightedTreeCount[i][j] = stack.size
                            println("[$i][$j] = ${sightedTreeCount[i][j]}")
                            stack.add(v)
                        })
          sightedTreeCount
        }

      val multipliedScores = scoresBySweeper.reduce { first, second ->
        Array(first.size) { i -> Array(first[i].size) { j -> first[i][j] * second[i][j] } }
      }
      val maxScore = multipliedScores.map { row -> row.maxOf { it } }.maxOf { it }
      println(maxScore)
    }

    private sealed interface Sweeper {
      fun <T> sweep(
        array: Array<Array<T>>,
        onPrimaryIteration: () -> Unit,
        onSecondaryIteration: (Int, Int, T) -> Unit
      )

      object LeftToRight : Sweeper {
        override fun <T> sweep(
          array: Array<Array<T>>,
          onPrimaryIteration: () -> Unit,
          onSecondaryIteration: (Int, Int, T) -> Unit,
        ) {
          array.indices.forEach { i ->
            onPrimaryIteration()
            array[i].indices.forEach { j -> onSecondaryIteration(i, j, array[i][j]) }
          }
        }
      }

      object RightToLeft : Sweeper {
        override fun <T> sweep(
          array: Array<Array<T>>,
          onPrimaryIteration: () -> Unit,
          onSecondaryIteration: (Int, Int, T) -> Unit,
        ) {
          array.indices.forEach { i ->
            onPrimaryIteration()
            array[i].indices.reversed().forEach { j -> onSecondaryIteration(i, j, array[i][j]) }
          }
        }
      }

      object TopToBottom : Sweeper {
        override fun <T> sweep(
          array: Array<Array<T>>,
          onPrimaryIteration: () -> Unit,
          onSecondaryIteration: (Int, Int, T) -> Unit,
        ) {
          array[0].indices.forEach { j ->
            onPrimaryIteration()
            array.indices.forEach { i -> onSecondaryIteration(i, j, array[i][j]) }
          }
        }
      }

      object BottomToTop : Sweeper {
        override fun <T> sweep(
          array: Array<Array<T>>,
          onPrimaryIteration: () -> Unit,
          onSecondaryIteration: (Int, Int, T) -> Unit,
        ) {
          array[0].indices.forEach { j ->
            onPrimaryIteration()
            array.indices.reversed().forEach { i -> onSecondaryIteration(i, j, array[i][j]) }
          }
        }
      }

    }
  }
}
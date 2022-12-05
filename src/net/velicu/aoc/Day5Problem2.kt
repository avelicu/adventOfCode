package net.velicu.aoc

import java.io.File

class Day5Problem2 {
  companion object {

    fun readInitialState(lines: Iterator<String>): Map<Int, List<Char>> {
      var stackCount: Int? = null
      val reversedStacks: MutableMap<Int, MutableList<Char>> = mutableMapOf()

      //  !! withDefault kinda returns the default value when you use getValue (not when you
      //  use get or []), but it doesn't store it in the map, whomp whomp.
      // .withDefault { mutableListOf() }

      for (line in lines) {
        if (line == "") break
        if (stackCount == null) {
          stackCount = line.length / 4
        }
        assert(line.length / 4 == stackCount)
        assert(line.length % 4 == 0)
        if (line.startsWith(" 1 ")) continue // consume useless line at the end
        for (i in 0..stackCount) {
          val ch = line[i*4+1]
          if (ch != ' ') {
            reversedStacks.getOrPut(i + 1, ::mutableListOf).add(ch)
          }
        }
      }

      return reversedStacks.map { (k, v) -> k to v.reversed() }.toMap()
    }

    @JvmStatic
    fun main(args: Array<String>) {
      val lines = File("inputs/5.input.txt").bufferedReader().lineSequence()
      val lineIterator = lines.iterator()
      val initialState = readInitialState(lineIterator)
      val finalState = runOperations(lineIterator, initialState)
      val topsForPresentStacks =
        finalState.keys.sorted().map { finalState[it]?.lastOrNull() ?: "" }
          .joinToString(separator = "")
      println(topsForPresentStacks)
    }

    private fun runOperations(lines: Iterator<String>, initialState: Map<Int, List<Char>>): Map<Int, List<Char>> {
      val state = initialState.map { (k, v) -> k to v.toMutableList() }.toMap().toMutableMap()
      for (line in lines) {
        val op = parseLine(line)
        val destStack = state.getOrPut(op.toStack, ::mutableListOf)
        val srcStack = state[op.fromStack]!!
        println("operation $op")
        println("               source stack $srcStack dest $destStack")
        val tmpStack = mutableListOf<Char>()
        for (i in 0 until op.moveCount) {
          tmpStack.add(srcStack.removeLast())
        }
        destStack.addAll(tmpStack.reversed())
        println("  after:       source stack $srcStack dest $destStack")
      }
      return state.map { (k, v) -> k to v.toList() }.toMap()
    }

    private val lineMatcher = Regex("""^move (\d+) from (\d+) to (\d+)$""")
    private data class Operation(val moveCount: Int, val fromStack: Int, val toStack: Int)
    private fun parseLine(line: String): Operation {
      val (moveCount, fromStack, toStack) =
        lineMatcher.matchEntire(line)!!.groupValues.drop(1).map(String::toInt)
      return Operation(moveCount, fromStack, toStack)
    }
  }
}
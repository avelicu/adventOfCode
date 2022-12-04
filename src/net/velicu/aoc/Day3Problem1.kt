package net.velicu.aoc

import java.io.File

class Day3Problem1 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val sumOfCommons =
        File("input.txt").bufferedReader().lineSequence().map { line: String ->
          val left = line.substring(0, line.length / 2).toSet()
          val right = line.substring(line.length / 2, line.length).toSet()
          val common = left intersect right
          assert(common.size == 1)
          common.first()
        }.map(::charVal).sum()
      println(sumOfCommons)
    }
  }
}
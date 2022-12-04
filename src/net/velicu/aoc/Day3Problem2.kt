package net.velicu.aoc

import java.io.File

class Day3Problem2 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val sumOfCommons =
        File("input.txt").bufferedReader().lineSequence().chunked(3) {
          // Why do I need to specify the types, i.e. String::, Set<Char>::?
          val common = it.map(String::toSet).reduce(Set<Char>::intersect)
          assert(common.size == 1)
          common.first()
        }.map(::charVal).sum()
      println(sumOfCommons)
    }
  }
}
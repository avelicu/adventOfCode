package net.velicu.aoc

import java.io.File

class Day4Problem2 {
  companion object {

    private val lineRe = Regex("""^(\d+)-(\d+),(\d+)-(\d+)$""")

    @JvmStatic
    fun main(args: Array<String>) {
      val partialOverlapCount =
        File("input.txt").bufferedReader().lineSequence().map { line ->
          val (leftFrom, leftTo, rightFrom, rightTo) =
            lineRe.matchEntire(line)!!.groupValues.drop(1).map(String::toInt)
          rightFrom in leftFrom..leftTo || rightTo in leftFrom..leftTo
            || leftFrom in rightFrom..rightTo || leftTo in rightFrom..rightTo
        }.count { it }
      println(partialOverlapCount)
    }
  }
}
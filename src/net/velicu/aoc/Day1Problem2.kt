package net.velicu.aoc

import java.io.File

class Day1Problem2 {
  companion object {

    fun extractElfCalories(input: Sequence<String>) = sequence {
      var current = 0;
      for (line in input) {
        if (line == "") {
          yield(current);
          current = 0;
        } else {
          current += line.toInt()
        }
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      val calories = extractElfCalories(File("inputs/1.input.txt").bufferedReader().lineSequence())
      println(calories.sortedDescending().take(3).sum())
    }
  }
}
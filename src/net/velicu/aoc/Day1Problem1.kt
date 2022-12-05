package net.velicu.aoc

import java.io.File
import kotlin.math.max

class Day1Problem1 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {

      var currentElfCalories = 0;
      var maxElfCalories = 0;
      File("inputs/1.input.txt").forEachLine {
        if (it == "") {
          maxElfCalories = max(maxElfCalories, currentElfCalories);
          currentElfCalories = 0;
        } else {
          currentElfCalories += it.toInt()
        }
      }

      println(maxElfCalories)
    }
  }
}
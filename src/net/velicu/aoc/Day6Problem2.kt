package net.velicu.aoc

import java.io.File

class Day6Problem2 {
  companion object {

    const val MARKER_LEN = 14

    @JvmStatic
    fun main(args: Array<String>) {
      val message = File("inputs/6.input.txt").readText()
      val firstMarkerPos =
        message
          .windowed(MARKER_LEN)
          .map { it.toSet().size == MARKER_LEN }
          .withIndex()
          .filter { (_, chunkStart) -> chunkStart }
          .minOf { (index, _) -> index }
      println(firstMarkerPos + MARKER_LEN)
    }
  }
}
package net.velicu.aoc

import java.io.File

class Day6Problem2 {
  companion object {

    const val MARKER_LEN = 14

    private class Multiset<T> {
      private val map: MutableMap<T, Int> = mutableMapOf()

      fun add(v: T) {
        map[v] = map.getOrDefault(v, 0).inc()
      }

      fun remove(v: T) {
        map.getOrDefault(v, 0).dec().let {
          if (it == 0) {
            map.remove(v)
          } else {
            map[v] = it
          }
        }
      }

      fun asSet() : Set<T> {
        return map.keys
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      val message = File("inputs/6.input.txt").readText()
      val runningSet = Multiset<Char>()
      val deque = ArrayDeque<Char>()

      val firstMarker = run {
        for ((idx, c) in message.withIndex()) {
          runningSet.add(c)
          deque.add(c)

          if (deque.size > MARKER_LEN) {
            val expunged = deque.removeFirst()
            runningSet.remove(expunged)
          }

          if (deque.size == MARKER_LEN && runningSet.asSet().size == MARKER_LEN) {
            return@run idx + 1
          }
        }
        return@run null
      }
      println(firstMarker)
    }
  }
}
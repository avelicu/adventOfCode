package net.velicu.aoc

import PeekingIterator
import java.io.File

class Day13 {
  companion object {

    @JvmStatic
    fun main(args: Array<String>) {
      val inOrderIdx =
        File("inputs/13.input.txt").bufferedReader().lineSequence()
          .chunked(3)
          .map { it.slice(0..1) }
          .map { scan(it[0]) to scan(it[1]) }
          .mapIndexed { idx, pair -> (idx + 1) to (ordering(pair.first, pair.second)) }
          .filter { it.second == Ordering.LT }
          .sumOf { it.first }
      println(inOrderIdx)
    }

    fun ordering(first: ListEl, second: ListEl): Ordering {
      if (first is Number && second is Number) {
        if (first.v < second.v) {
          return Ordering.LT
        } else if (first.v == second.v) {
          return Ordering.EQ
        } else {
          return Ordering.GT
        }
      }

      if (first is Number && second is Group) {
        return ordering(Group(listOf(first)), second)
      }

      if (first is Group && second is Number) {
        return ordering(first, Group(listOf(second)))
      }

      if (first is Group && second is Group) {
        val firstIt = first.es.iterator()
        val secondIt = second.es.iterator()

        while (firstIt.hasNext() && secondIt.hasNext()) {
          val potOrd = ordering(firstIt.next(), secondIt.next())
          if (potOrd != Ordering.EQ) {
            return potOrd
          }
        }

        if (firstIt.hasNext()) {
          return Ordering.GT;
        }

        if (secondIt.hasNext()) {
          return Ordering.LT;
        }

        return Ordering.EQ;
      }

      throw IllegalStateException("Uncomparable $first and $second")
    }

    enum class Ordering {
      LT, EQ, GT
    }

    fun scan(s: String): Group {
      val iter = PeekingIterator(s.iterator())
      assert(iter.next() == '[') // consume
      return scanInternal(iter)
    }

    fun scanInternal(cit: PeekingIterator<Char>): Group {
      val vals = mutableListOf<ListEl>()

      while (cit.hasNext()) {
        val c = cit.peek()
        when (c) {
          '[' -> {
            cit.next()
            vals.add(scanInternal(cit))
          }
          ']' -> {
            cit.next()
            return Group(vals)
          }
          ',' -> {
            cit.next() // Consume
          }
          else -> vals.add(Number(readNextInt(cit)))
        }
      }

      throw IllegalStateException("Reached end of line without ], unbalanced parens?")
    }

    fun readNextInt(cit: PeekingIterator<Char>): Int {
      val s = mutableListOf<Char>()

      while (cit.hasNext()) {
        val c = cit.peek()
        if (c == ',' || c == ']' || c == '[') {
          return s.joinToString(separator = "").toInt()
        }
        s.add(cit.next())
      }

      throw IllegalStateException("Reached end of line while reading int token, unbalanced parens?")
    }

    sealed class ListEl
    data class Number(val v: Int): ListEl() {
      override fun toString(): String = v.toString()
    }
    data class Group(val es: List<ListEl>): ListEl() {
      override fun toString(): String = es.toString()
    }
  }
}
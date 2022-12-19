package net.velicu.aoc

import PeekingIterator
import java.io.File

class Day13Problem2 {
  companion object {

    @JvmStatic
    fun main(args: Array<String>) {
      val sep1 = Group(Group(Number(2)))
      val sep2 = Group(Group(Number(6)))

      val groups =
        File("inputs/13.input.txt").bufferedReader().lineSequence()
          .filter { it != "" }
          .map { scan(it) }
          .plus(sep1)
          .plus(sep2)

      val sorted = groups.sortedWith(ListElComparator).toList()
      val idx1 = sorted.indexOf(sep1) + 1
      val idx2 = sorted.indexOf(sep2) + 1

      println(idx1 * idx2)
    }

    private object ListElComparator : Comparator<ListEl> {
      override fun compare(o1: ListEl?, o2: ListEl?): Int {
        val ord = ordering(o1!!, o2!!)
        return when (ord) {
          Ordering.LT -> -1
          Ordering.EQ -> 0
          Ordering.GT -> 1
        }
      }
    }

    private fun ordering(first: ListEl, second: ListEl): Ordering {
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

    private enum class Ordering {
      LT, EQ, GT
    }

    private fun scan(s: String): Group {
      val iter = PeekingIterator(s.iterator())
      assert(iter.next() == '[') // consume
      return scanInternal(iter)
    }

    private fun scanInternal(cit: PeekingIterator<Char>): Group {
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

    private fun readNextInt(cit: PeekingIterator<Char>): Int {
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

    private sealed class ListEl
    private data class Number(val v: Int): ListEl() {
      override fun toString(): String = v.toString()
    }
    private data class Group(val es: List<ListEl>): ListEl() {
      constructor(e: ListEl) : this(listOf(e))
      override fun toString(): String = es.toString()
    }
  }
}
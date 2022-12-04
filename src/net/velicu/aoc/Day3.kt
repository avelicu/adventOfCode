package net.velicu.aoc

fun charVal(c: Char): Int {
  return if (c in 'a'..'z') {
    c - 'a' + 1
  } else if (c in 'A'..'Z') {
    c - 'A' + 27
  } else {
    throw IllegalArgumentException()
  }
}

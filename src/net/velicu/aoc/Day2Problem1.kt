package net.velicu.aoc

import java.io.File

class Day2Problem1 {
  companion object {

    enum class Outcome(val score: Int) {
      Win(6),
      Draw(3),
      Lose(0)
    }

    enum class RPS(val score: Int) {
      Rock(1),
      Paper(2),
      Scissors(3);

      fun outcomeAgainst(other: RPS): Outcome {
        return if (other == this) {
          Outcome.Draw;
        } else if (this == Rock && other == Scissors
          || this == Paper && other == Rock
          || this == Scissors && other == Paper) {
          Outcome.Win;
        } else {
          Outcome.Lose;
        }
      }

      companion object {
        fun fromOpponent(c: Char): RPS {
          return when (c) {
            'A' -> Rock
            'B' -> Paper
            'C' -> Scissors
            else -> throw IllegalArgumentException("parsing opponent RPS $c")
          }
        }

        fun fromMe(c: Char): RPS {
          return when (c) {
            'X' -> Rock
            'Y' -> Paper
            'Z' -> Scissors
            else -> throw IllegalArgumentException("parsing my RPS $c")
          }
        }
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      var score = 0;
      File("inputs/2.input.txt").forEachLine {
        val split = it.split(" ")
        val opponent = RPS.fromOpponent(split[0].first())
        val me = RPS.fromMe(split[1].first())
        score += me.outcomeAgainst(opponent).score + me.score;
      }
      println(score)
    }
  }
}
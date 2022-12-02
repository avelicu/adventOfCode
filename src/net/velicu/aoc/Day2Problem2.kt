package net.velicu.aoc

import java.io.File

class Day2Problem2 {
  companion object {

    enum class Outcome(val score: Int) {
      Win(6),
      Draw(3),
      Lose(0);

      companion object {
        fun fromDesiredOutcome(c: Char): Outcome {
          return when (c) {
            'X' -> Lose
            'Y' -> Draw
            'Z' -> Win
            else -> throw IllegalArgumentException()
          }
        }
      }
    }

    enum class RPS(val score: Int) {
      Rock(1),
      Paper(2),
      Scissors(3);

      val beats: RPS
        get() = when (this) {
          Rock -> Scissors
          Paper -> Rock
          Scissors -> Paper
        }

      val beatenBy: RPS
        get() = when (this) {
          Scissors -> Rock
          Rock -> Paper
          Paper -> Scissors
        }

      fun playForOutcome(desiredOutcome: Outcome): RPS {
        return when (desiredOutcome) {
          Outcome.Draw -> this
          Outcome.Win -> this.beatenBy
          Outcome.Lose -> this.beats
        }
      }

      companion object {
        fun fromOpponent(c: Char): RPS {
          return when (c) {
            'A' -> Rock
            'B' -> Paper
            'C' -> Scissors
            else -> throw IllegalArgumentException()
          }
        }
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      val score =
        File("input.txt").bufferedReader().lineSequence().sumOf {
          val split = it.split(" ")
          val opponent = RPS.fromOpponent(split[0].first())
          val desiredOutcome = Outcome.fromDesiredOutcome(split[1].first())
          desiredOutcome.score + opponent.playForOutcome(desiredOutcome).score
        }
      println(score)
    }
  }
}
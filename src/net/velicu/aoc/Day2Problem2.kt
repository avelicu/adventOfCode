package net.velicu.aoc

import java.io.File
import net.velicu.aoc.Day2Problem2.Companion.Outcome.Companion.parseDesiredOutcome
import net.velicu.aoc.Day2Problem2.Companion.RPS.Companion.parseOpponentPlay

class Day2Problem2 {
  companion object {

    enum class Outcome(val score: Int) {
      Win(6),
      Draw(3),
      Lose(0);

      companion object {
        fun String.parseDesiredOutcome(): Outcome? {
          return when (this) {
            "X" -> Lose
            "Y" -> Draw
            "Z" -> Win
            else -> null
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
        fun String.parseOpponentPlay(): RPS? {
          return when (this) {
            "A" -> Rock
            "B" -> Paper
            "C" -> Scissors
            else -> null
          }
        }
      }
    }

    @JvmStatic
    fun main(args: Array<String>) {
      val score =
        File("input.txt").bufferedReader().lineSequence().sumOf {
          val split = it.split(" ")
          val opponent = split[0].parseOpponentPlay()!!
          val desiredOutcome = split[1].parseDesiredOutcome()!!
          desiredOutcome.score + opponent.playForOutcome(desiredOutcome).score
        }
      println(score)
    }
  }
}
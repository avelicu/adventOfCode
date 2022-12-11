package net.velicu.aoc

class Day11 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val monkeys = listOf(
        Monkey(
          listOf(66, 79),
          { old -> old * 11 },
          { v -> v % 7 == 0 },
          6, 7),
        Monkey(
          listOf(84, 94, 94, 81, 98, 75),
          { old -> old * 17 },
          { v -> v % 13 == 0 },
          5, 2),
        Monkey(
          listOf(85, 79, 59, 64, 79, 95, 67),
          { old -> old + 8 },
          { v -> v % 5 == 0 },
          4, 5),
        Monkey(
          listOf(70),
          { old -> old + 3 },
          { v -> v % 19 == 0 },
          6, 0),
        Monkey(
          listOf(57, 69, 78, 78),
          { old -> old + 4 },
          { v -> v % 2 == 0 },
          0, 3),
        Monkey(
          listOf(65, 92, 60, 74, 72),
          { old -> old + 7 },
          { v -> v % 11 == 0 },
          3, 4),
        Monkey(
          listOf(77, 91, 91),
          { old -> old * old },
          { v -> v % 17 == 0 },
          1, 7),
        Monkey(
          listOf(76, 58, 57, 55, 67, 77, 54, 99),
          { old -> old + 6 },
          { v -> v % 3 == 0 },
          2, 1))

      repeat(20) {
        println("Round $it:")

        for (monkey in monkeys) {
          for (item in monkey.objects) {
            val newWorryLevel = monkey.operation.invoke(item) / 3
            val destinationMonkey =
              if (monkey.test.invoke(newWorryLevel)) monkey.trueMonkeyId else monkey.falseMonkeyId
            monkeys[destinationMonkey].objects.add(newWorryLevel)
          }
          monkey.inspectionCount += monkey.objects.count()
          monkey.objects.clear()
        }

        monkeys.forEachIndexed() { i, monkey -> println("Monkey $i: $monkey") }
        println()
      }

      val monkeyBusiness =
        monkeys
          .map { it.inspectionCount }
          .sortedDescending()
          .slice(0..1)
          .reduce { l, r -> l * r }
      println(monkeyBusiness)
    }

    private class Monkey (
      startingObjects: List<Int>,
      val operation: (Int) -> Int,
      val test: (Int) -> Boolean,
      val trueMonkeyId: Int,
      val falseMonkeyId: Int) {
      val objects = startingObjects.toMutableList()
      var inspectionCount = 0

      override fun toString() = "$objects"
    }
  }
}
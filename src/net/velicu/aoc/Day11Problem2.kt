package net.velicu.aoc

class Day11Problem2 {
  companion object {

    @JvmStatic
    fun main(args: Array<String>) {

      val monkeyDefs = listOf(
        MonkeyDef(
          listOf(66, 79),
          { it * 11 },
          7,
          6, 7),
        MonkeyDef(
          listOf(84, 94, 94, 81, 98, 75),
          { it * 17 },
          13,
          5, 2),
        MonkeyDef(
          listOf(85, 79, 59, 64, 79, 95, 67),
          { it + 8 },
          5,
          4, 5),
        MonkeyDef(
          listOf(70),
          { it + 3 },
          19,
          6, 0),
        MonkeyDef(
          listOf(57, 69, 78, 78),
          { it + 4 },
          2,
          0, 3),
        MonkeyDef(
          listOf(65, 92, 60, 74, 72),
          { it + 7 },
          11,
          3, 4),
        MonkeyDef(
          listOf(77, 91, 91),
          { it * it },
          17,
          1, 7),
        MonkeyDef(
          listOf(76, 58, 57, 55, 67, 77, 54, 99),
          { it + 6 },
          3,
          2, 1))

      val interestingMods = monkeyDefs.map { it.divisibleByTest }
      val monkeys = monkeyDefs.map { Monkey(it, interestingMods) }

      repeat(10000) {
        // println("Round $it:")

        for (monkey in monkeys) {
          for (item in monkey.objects) {
            val newItem = monkey.operation.invoke(item)
            val destinationMonkey =
              if (newItem % monkey.divisibleByTest == 0)
                monkey.trueMonkeyId else monkey.falseMonkeyId
            monkeys[destinationMonkey].objects.add(newItem)
          }
          monkey.inspectionCount += monkey.objects.count()
          monkey.objects.clear()
        }

        // monkeys.forEachIndexed { i, monkey -> println("Monkey $i: $monkey") }
        // println()
      }

      val monkeyBusiness =
        monkeys
          .map { it.inspectionCount }
          .sortedDescending()
          .slice(0..1)
          .reduce { l, r -> l * r }
      println(monkeyBusiness)
    }

    private data class MonkeyDef (
      val startingObjects: List<Int>,
      val operation: (ModuloHolder) -> ModuloHolder,
      val divisibleByTest: Int,
      val trueMonkeyId: Int,
      val falseMonkeyId: Int)

    private class Monkey(monkeyDef: MonkeyDef, interestingMods: List<Int>) {
      val objects = monkeyDef.startingObjects
        .map { ModuloHolder(it, interestingMods) }.toMutableList()

      val operation: (ModuloHolder) -> ModuloHolder = monkeyDef.operation
      val divisibleByTest: Int = monkeyDef.divisibleByTest
      val trueMonkeyId: Int = monkeyDef.trueMonkeyId
      val falseMonkeyId: Int = monkeyDef.falseMonkeyId

      var inspectionCount = 0L
      override fun toString() = "$objects"
    }

    private class ModuloHolder(private val modMap: Map<Int, Int>) {

      constructor(initialValue: Int, interestingMods: List<Int>):
        this(interestingMods.associateWith { initialValue % it })

      private fun modHolderWithOperationApplied(op: (Int, Int) -> Int): ModuloHolder =
        modMap.map { (modulo, value) -> modulo to (op.invoke(modulo, value)) % modulo }.toMap()
          .let { ModuloHolder(it) }

      operator fun plus(v: Int): ModuloHolder =
        modHolderWithOperationApplied { _, oldv -> oldv + v }

      operator fun times(v: Int): ModuloHolder =
        modHolderWithOperationApplied { _, oldv -> oldv * v }

      operator fun times(v: ModuloHolder): ModuloHolder =
        modHolderWithOperationApplied { mod, oldv -> oldv * v.getModuloDangerously(mod) }

      operator fun rem(mod: Int): Int {
        return getModuloDangerously(mod) % mod
      }

      private fun getModuloDangerously(mod: Int): Int {
        return modMap[mod] ?: throw IllegalArgumentException("Modulo $mod not being tracked")
      }

      override fun toString(): String = "($modMap)"
    }
  }
}
package net.velicu.aoc

import java.io.File

class Day7 {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val lineIterator = File("inputs/7.input.txt")
        .bufferedReader().lineSequence().iterator()
      val root = parseDirStructure(lineIterator)
      println(flatListOfDirectories(root).filter { it.size <= 100000 }.sumOf { it.size })

      val usedSpace = root.size
      val totalSpace = 70000000
      val freeSpace = totalSpace - usedSpace
      val neededSpace = 30000000
      val freeUpRequirement = neededSpace - freeSpace
      println(flatListOfDirectories(root).filter { it.size >= freeUpRequirement }.minOf { it.size })
    }

    private fun parseDirStructure(lineIterator: Iterator<String>): Directory {
      val root = Directory("", null)
      var cwd = root

      val peekingIterator = PeekingIterator(lineIterator)
      var currentLine: String = peekingIterator.next()

      do {
        assert(currentLine.startsWith("$ "))
        val fullCommandLine = currentLine.drop(2).split(" ")
        val command = fullCommandLine[0]
        if (command == "cd") {
          val arg = fullCommandLine[1]
          if (arg == "/") {
            cwd = root
          } else if (arg == "..") {
            cwd = cwd.parent!!
          } else {
            cwd = cwd.children[arg] as Directory
          }
        } else if (command == "ls") {
          do {
            val currentOutputLine = peekingIterator.peek()
            if ((currentOutputLine == null) || currentOutputLine.startsWith("$")) {
              break
            }
            peekingIterator.next() // consume what we just peeked at

            val (dirOrSize, name) = currentOutputLine.split(" ")
            if (dirOrSize == "dir") {
              Directory(name, cwd).also { cwd.add(it) }
            } else {
              File(name, dirOrSize.toInt()).also { cwd.add(it) }
            }
          } while (true)
        }

        if (!peekingIterator.hasNext()) break;
        currentLine = peekingIterator.next()
      } while (true)

      return root
    }

    private fun flatListOfDirectories(root: Directory): Sequence<Directory> = sequence {
      yield(root)
      root.children.values
        .filterIsInstance<Directory>()
        .forEach { yieldAll(flatListOfDirectories(it)) }
    }

    private class PeekingIterator<T>(val wrappedIterator: Iterator<T>): Iterator<T> {
      private var peeked: T? = null;

      override fun hasNext(): Boolean = wrappedIterator.hasNext() || peeked != null
      override fun next(): T {
        peeked.let {
          if (it != null) {
            peeked = null
            return it
          } else {
            return wrappedIterator.next()
          }
        }
      }
      fun peek(): T? {
        peeked.let {
          if (it == null) {
            if (!wrappedIterator.hasNext()) {
              return null
            }

            wrappedIterator.next().let {
              peeked = it
              return it
            }
          }
          return it
        }
      }
    }

    private sealed interface FilesystemObject {
      val name: String
      val size: Int
    }
    private data class File(override val name: String, override val size: Int): FilesystemObject
    private class Directory(override val name: String, val parent: Directory?): FilesystemObject {
      val children: MutableMap<String, FilesystemObject> = mutableMapOf()

      override val size: Int get() = children.values.sumOf { it.size }

      fun add(obj: FilesystemObject) {
        children[obj.name] = obj
      }

      fun get(name: String): FilesystemObject? = children[name]

      override fun toString(): String = "Directory($name) [${children.values}]"
    }
  }
}
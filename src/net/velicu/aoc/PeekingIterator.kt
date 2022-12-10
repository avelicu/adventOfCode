class PeekingIterator<T>(private val wrappedIterator: Iterator<T>): Iterator<T> {
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

  fun peek(): T {
    peeked.let {
      if (it == null) {
        if (!wrappedIterator.hasNext()) {
          throw NoSuchElementException()
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


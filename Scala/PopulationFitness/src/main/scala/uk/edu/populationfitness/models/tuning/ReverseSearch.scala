package uk.edu.populationfitness.models.tuning

class ReverseSearch extends Search {
  override def findNext(comparison: Comparison.Comparison): Option[Search] = {
    val next = new ReverseSearch()
    next.increment = this.increment
    next.max = this.max
    next.min = this.min

    comparison match {
      case Comparison.TooLow => next.max = current
      case Comparison.TooHigh => next.min = current
    }

    next.current = next.centre

    if (math.abs(next.max - current) < increment) return None
    if (math.abs(next.min - current) < increment) return None
    Some(next)
  }
}

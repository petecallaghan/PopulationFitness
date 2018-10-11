package uk.edu.populationfitness.models.tuning

class Search extends FitnessRange {
  private var _increment = .0
  private var _is_current_set = false
  private var _current = .0

  def this(source: Search) {
    this()
    increment = source.increment
    min = source.min
    max = source.max
  }

  def this(source: Search, c: Double) {
    this(source)
    _is_current_set = true
    _current = c
  }

  def increment_=(increment: Double): Unit = {
    _increment = increment
  }

  private[tuning] def increment = _increment

  private[tuning] def centre = (((min + max) / _increment).asInstanceOf[Long] / 2) * _increment

  def current: Double = {
    if (!_is_current_set) {
      current = centre
    }
    _current
  }

  private[tuning] def current_=(value: Double): Unit = {
    _current = value
    _is_current_set = true
  }

  def findNext(comparison: Comparison.Comparison): Option[Search] = {
    val next = new Search(this)
    comparison match {
      case Comparison.TooLow => next.min = current
      case Comparison.TooHigh => next.max = current
    }

    next.current = next.centre

    if (math.abs(next.max - next.current) < increment) return None
    if (math.abs(next.min - next.current) < increment) return None
    Some(next)
  }
}

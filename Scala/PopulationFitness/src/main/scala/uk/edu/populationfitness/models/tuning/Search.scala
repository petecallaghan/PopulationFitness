package uk.edu.populationfitness.models.tuning

class Search extends FitnessRange {
  private var _increment = .0
  private var is_current_set = false
  private var _current = .0

  def increment(increment: Double): Search = {
    _increment = increment
    this
  }

  private[tuning] def increment = _increment

  private def centre = (((min + max) / _increment).asInstanceOf[Long] / 2) * _increment

  def current(current: Double): Unit = {
    is_current_set = true
    _current = current
  }

  def current: Double = {
    if (!is_current_set) current(centre)
    _current
  }

  def findNext(comparison: Comparison.Comparison): Option[Search] = {
    val next = new Search
    next.increment(increment).max(max).min(min)
    comparison match {
      case Comparison.TooLow => next.min(current)
      case Comparison.TooHigh => next.max(current)
      case _ => return None
    }
    if (math.abs(next.max - next.current) < increment) return None
    if (math.abs(next.min - next.current) < increment) return None
    Some(next)
  }
}

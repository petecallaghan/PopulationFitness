package uk.edu.populationfitness.models.maths

/**
  * Maintains a cache of values identified by longs. Appropriate for values that are expensive to calculate.
  *
  * @param calculate a function that calculates A from Long
  * @tparam A
  */
class ExpensiveCalculatedValues[A] ( var calculate : (Long) => A) {
  private var _values = Map[Long, A]() withDefault insertNewCalculatedValue

  private def insertNewCalculatedValue(index: Long): A = {
    val value = calculate(index)
    _values += index -> value
    value
  }

  /**
    * Finds the value if it has been previously calculated. Otherwise calculates it and stores it in the cache.
    *
    * @param index
    * @return the value corresponding to the index
    */
  def findOrCalculate(index: Long) : A = _values(index)
}

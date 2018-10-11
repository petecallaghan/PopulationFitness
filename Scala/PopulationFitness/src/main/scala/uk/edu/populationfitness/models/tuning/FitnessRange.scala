package uk.edu.populationfitness.models.tuning

class FitnessRange private[tuning]() {

  // Defines the minimum fitness expected from the fitness function
  private var _min = .0
  // Defines the max fitness expected from the fitness function
  private var _max = 1.0
  private var _range = 1.0

  protected def max_= (max: Double): Unit = {
    _max = max
    _range = _max - _min
  }

  protected def min_= (min: Double): Unit = {
    _min = min
    _range = _max - _min
  }

  def max: Double = _max

  def min: Double = _min
}

package uk.edu.populationfitness.models.tuning

class FitnessRange private[tuning]() {

  // Defines the minimum fitness expected from the fitness function
  private var _min = .0
  // Defines the max fitness expected from the fitness function
  private var _max = 1.0
  private var _range = 1.0

  def max(max: Double): FitnessRange = {
    _max = max
    _range = _max - _min
    this
  }

  def min(min: Double): FitnessRange = {
    _min = min
    _range = _max - _min
    this
  }

  def max: Double = _max

  def min: Double = _min

  // returns the range as a scale. S(f) = (f - min) / (max - min)
  def toScale(fitness: Double): Double = {
    (fitness - _min) / _range
  }
}

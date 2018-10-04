package uk.edu.populationfitness.models.maths

object FastMaths {
  def pow(base: Double, power: Long): Double = {
    var result = 1.0
    var currentPower = power
    var currentBase = base
    while ( currentPower > 0) {
      if ((currentPower & 1) == 1) result *= currentBase
      currentPower >>= 1
      currentBase *= currentBase
    }
    result
  }
}

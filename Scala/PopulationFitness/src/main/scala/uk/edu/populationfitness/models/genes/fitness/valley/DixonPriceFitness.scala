package uk.edu.populationfitness.models.genes.fitness.valley

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.{ExpensiveCalculatedValues, FastMaths}

object DixonPriceFitness {
  private val TwoTenSquared = FastMaths.pow(210.0, 2)

  def calculateValue(n: Long): Double = 121.0 + TwoTenSquared * ((2.0 + n) / 2.0) * (n - 1)

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class DixonPriceFitness(config: Config) extends NormalizingFitness(config, 10.0) {
  protected def calculateNormalizationRatio(n: Int): Double = DixonPriceFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
     f left (x right ) = {left ({x} rsub {1} -1 right )} ^ {2} +  sum from {i=2} to {n} {i {left ({2x} rsub {i} rsup {2} - {x} rsub {i-1} right )} ^ {2}}
    */
    var fitness = if (values.length > 0) FastMaths.pow(values(0) - 1.0, 2) else 0.0
    for(i <- 1 to values.length - 1) {
      val xIMinus1 = values(i - 1)
      val x = values(i)
      val value = 2.0 * x * x - xIMinus1
      fitness += (i + 1) * value * value
    }
    fitness
  }
}

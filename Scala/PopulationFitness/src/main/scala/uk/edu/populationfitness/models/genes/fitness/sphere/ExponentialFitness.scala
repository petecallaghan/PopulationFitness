package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.ExpensiveCalculatedValues

object ExponentialFitness {
  def calculateValue(n: Long): Double = 1.0 - Math.exp(-0.5 * n)

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class ExponentialFitness(config: Config) extends NormalizingFitness(config, 1.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = ExponentialFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) =- exp left (-0.5 sum from {i=2} to {n} {{x} rsub {i} rsup {2}} right )
            */
    val fitness = values.foldLeft(0.0) ((f, x) => f + x * x)

    1.0 - Math.exp(-0.5 * fitness)
  }
}

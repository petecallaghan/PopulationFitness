package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.{ExpensiveCalculatedValues, FastMaths}

object SchumerSteiglitzFitness {
  def calculateValue(n: Long): Double = 100000000.0 * n

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class SchumerSteiglitzFitness(config: Config) extends NormalizingFitness(config, 100.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = SchumerSteiglitzFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {4}}
            */
    values.foldLeft (0.0) ((fitness, x) => fitness + FastMaths.pow(x, 4))
  }
}

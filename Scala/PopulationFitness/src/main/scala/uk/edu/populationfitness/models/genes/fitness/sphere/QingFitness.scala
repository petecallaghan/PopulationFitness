package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.ExpensiveCalculatedValues

object QingFitness {

  def calculateValue(n: Long): Double = {
    var ratio = 0.0
    for(i <- 1 to n.toInt) {
      val value = 250000.0 - i
      ratio += value * value
    }
    if (n > 0) ratio else 1.0
  }

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class QingFitness(config: Config) extends NormalizingFitness(config, 500.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = QingFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} rsup {2} -i right )} ^ {2}}
            */
    var fitness = 0.0
    for(i <- 0 to values.length - 1) {
      val x = values(i)
      val value = x * x - (i + 1)
      fitness += value * value
    }
    fitness
  }
}

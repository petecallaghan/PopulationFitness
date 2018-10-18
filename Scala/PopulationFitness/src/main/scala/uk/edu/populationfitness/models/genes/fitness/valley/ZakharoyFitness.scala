package uk.edu.populationfitness.models.genes.fitness.valley

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.{ExpensiveCalculatedValues, FastMaths}

object ZakharoyFitness {
  def calculateValue(n: Long): Double = {
    val value = (n * (n + 1.0)) / 2.0
    100 * n + 25 * (value * value + 35.0 * FastMaths.pow(value, 4))
  }

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class ZakharoyFitness(config: Config) extends NormalizingFitness(config, 10.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = ZakharoyFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {2}} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {2} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {4}
            */
    var fitness = 0.0
    var sum = 0.0
    for(i <- 1 to values.length){
      val x = values(i - 1)
      fitness += x * x
      sum += 0.5 * i * x
    }
    fitness += (sum * sum) + (sum * sum * sum * sum)
    fitness
  }
}

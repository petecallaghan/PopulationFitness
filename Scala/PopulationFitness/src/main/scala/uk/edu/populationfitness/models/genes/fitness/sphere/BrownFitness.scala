package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

class BrownFitness(config: Config) extends NormalizingFitness(config, 1.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = if (n > 1) 2.0 * (n - 1) else 1.0

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n-1} {left [{left ({x} rsub {i} rsup {2} right )} ^ {{x} rsub {i+1} rsup {2} +1} + {left ({x} rsub {i+1} rsup {2} right )} ^ {{x} rsub {i} rsup {2} +1} right ]}
            */
    if (values.length < 1)
      return 0.0

    var fitness = 0.0
    val xN = values(0)
    var xNSquared = xN * xN
    for(i <- 1 to values.length - 1)
    {
      val xNPlus1 = values(i)
      val xNPlus1Squared = xNPlus1 * xNPlus1
      fitness += Math.pow(xNSquared, xNPlus1Squared + 1.0) + Math.pow(xNPlus1Squared, xNSquared + 1.0)
      xNSquared = xNPlus1Squared
    }
    fitness
  }
}

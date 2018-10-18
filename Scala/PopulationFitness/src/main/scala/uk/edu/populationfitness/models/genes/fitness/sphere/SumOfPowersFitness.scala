package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.FastMaths

class SumOfPowersFitness(config: Config) extends NormalizingFitness(config, 1.0) {
  override protected def calculateNormalizationRatio(n: Int) = 1.0

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             http://www.sfu.ca/~ssurjano/sumpow.html

             f(x) = sum{i=1 to d}[ abs(x{i}) ^ i+1]

             Dimensions: d

            The function is unimodal.

            Input Domain:

            The function is usually evaluated on the hypercube xi ∈ [-1, 1], for all i = 1, …, d.

            Global Minimum:

            f(x) = 0, at x = (0,...,0)

            */
    var fitness = 0.0
    for(i <- 0 to values.length - 1) {
      val x = values(i)
      fitness += FastMaths.pow(Math.abs(x), i + 2)
    }
    fitness
  }
}
